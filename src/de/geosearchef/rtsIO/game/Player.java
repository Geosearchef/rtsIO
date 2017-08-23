package de.geosearchef.rtsIO.game;

import de.geosearchef.rtsIO.json.*;
import de.geosearchef.rtsIO.json.gems.NewGemMessage;
import de.geosearchef.rtsIO.json.units.NewUnitMessage;
import de.geosearchef.rtsIO.util.Vector;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.jetty.websocket.api.Session;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.geosearchef.rtsIO.websocket.WebSocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Player {

    private static final int LOGIN_TIME_OUT = 10 * 1000;//name reserve time
    private static final Logger logger = LoggerFactory.getLogger(Player.class);
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

    @Getter private final int playerID;
    @Getter private final long loginTime;
    @Getter private final String username;
    @Getter private final String loginToken;
    @Getter private boolean connected;//needs to be logged in, else false

    @Getter private Session session;

    @Getter private int resourceAmount = 0;

    public Player(int id, String username, String loginToken) {
        this.playerID = id;
        this.username = username;
        this.loginToken = loginToken;
        this.loginTime = System.currentTimeMillis();

        scheduleLoginTimeout();
    }


    //TODO this is called async and doesn't throw any exceptions, FIX!!!
    public void onMessage(JSONObject message) {
        switch((String)message.get("type")) {
            case "moveUnits": {System.out.println(message.toJSONString());
                JSONArray unitIDArray = (JSONArray) message.get("unitIDs");
                HashSet<Integer> unitIDs = unitIDArray.stream()
                        .mapToInt(o -> Integer.parseInt(o.toString()))
                        .boxed()
                        .collect(Collectors.toCollection(HashSet::new));
                
                Vector dest = new Vector((JSONObject) message.get("dest"));

                synchronized (Game.units) {
                    Game.units.stream()
                            .filter(u -> u.getPlayer() == this && unitIDs.contains(u.getUnitID()))
                            .forEach(u -> u.move(dest));
                }

                break;
            }

        }
    }




    public void addResources(int amount) {setResourceAmount(this.resourceAmount + amount);}
    public void removeResources(int amount) {setResourceAmount(this.resourceAmount - amount);}
    public void setResourceAmount(int amount) {
        this.resourceAmount = amount;
        send(new ResourceAmountUpdateMessage(this.resourceAmount));
    }





    //attempts login of player via token over websocket
    public void login(String token, Session session) {
        if(! token.equals(this.loginToken)) {
            WebSocket.INSTANCE.redirectToLoginPage(session);
            synchronized (PlayerManager.connectingPlayers) {
                PlayerManager.connectingPlayers.remove(this);
            }
            return;
        }

        connected = true;
        this.session = session;
        synchronized (PlayerManager.players) {
            PlayerManager.players.add(this);
        }
        synchronized (PlayerManager.connectingPlayers) {
            PlayerManager.connectingPlayers.remove(this);
        }

        this.send(new LoginSuccessMessage(this.username, this.playerID));

        this.sendGameInfo();
        Game.addUnit(new Unit(this, 0, new Vector(50, 50), 100));

        logger.info("User " + this.username + " connected");
    }

    private void sendGameInfo() {
        //Send game information to new connecting player
        this.send(new GameInfoMessage(Game.MAP_SIZE));

        //Send all players to this player
        synchronized (PlayerManager.players) {
            PlayerManager.players.stream()
                    .filter(p -> p != this) //own player will broadcast later on to everyone
                    .forEach(p -> this.send(new PlayerConnectMessage(p.getPlayerID(), p.getUsername())));
        }

        //Send all units to this player
        synchronized (Game.units) {
            Game.units.forEach(u -> this.send(new NewUnitMessage(u.getPlayer().getPlayerID(), u.getUnitID(), u.getUnitType(), u.getPos(), u.getVel(), u.getDest(), u.getHp())));
        }

        //Send all gems to this player
        synchronized (Game.gems) {
            Game.gems.forEach(g -> this.send(new NewGemMessage(g.getId(), g.getPos(), g.isSpawner())));
        }
    }

    //This is async
    public void send(Message message) {send(message.toJson());}
    public void send(JSONObject message) {send(message.toJSONString());}
    public void send(String message) {
        if(!this.isConnected())
            return;

        try {
            WebSocket.INSTANCE.send(this.session, message);
        } catch(IOException e) {logger.warn("Could not send message to player: " + message, e);}
    }


    public boolean isLoggedIn() {
        return isConnected();
    }

    public void scheduleLoginTimeout() {
        scheduler.schedule(this::checkLoginTimeout, LOGIN_TIME_OUT, TimeUnit.MILLISECONDS);
    }

    //checks if the player logged in and if not frees the name reservation
    public void checkLoginTimeout() {
        synchronized (PlayerManager.connectingPlayers) {
            if(!this.isConnected() && PlayerManager.connectingPlayers.contains(this)) {
                logger.info("Player " + username + ": login timed out");
                PlayerManager.connectingPlayers.remove(this);
            }
        }
    }

    @Override
    public int hashCode() {
        return playerID;
    }
}
