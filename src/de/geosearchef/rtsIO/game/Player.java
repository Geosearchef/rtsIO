package de.geosearchef.rtsIO.game;

import de.geosearchef.rtsIO.Main;
import de.geosearchef.rtsIO.json.*;
import de.geosearchef.rtsIO.json.gems.NewGemMessage;
import de.geosearchef.rtsIO.json.units.NewUnitMessage;
import de.geosearchef.rtsIO.util.Pair;
import de.geosearchef.rtsIO.util.Vector;
import lombok.Getter;
import org.eclipse.jetty.websocket.api.Session;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.geosearchef.rtsIO.websocket.WebSocket;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    @Getter private double resourceAmount = 0;

    public Player(int id, String username, String loginToken) {
        this.playerID = id;
        this.username = username;
        this.loginToken = loginToken;
        this.loginTime = System.currentTimeMillis();

        scheduleLoginTimeout();
    }


    public void onMessage(JSONObject message) {
        System.out.println(message.toJSONString());
        switch((String)message.get("type")) {
            case "moveUnits": {
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


            case "createBuilding": {

                Vector pos = new Vector((JSONObject) message.get("pos"));
                int buildingType = ((Long)message.get("typeID")).intValue();

                //TODO: check building typeID
                //TODO: check resources

                //TODO: synchronize?? so no update can change during checks

                JSONArray unitIDArray = (JSONArray) message.get("unitIDs");
                HashSet<Integer> unitIDs = unitIDArray.stream()
                        .mapToInt(o -> Integer.parseInt(o.toString()))
                        .boxed()
                        .collect(Collectors.toCollection(HashSet::new));

                //Find closest selected unit
                List<Unit> selectedUnits = unitIDs.stream()
                        .map(Game::getUnitByID)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .filter(u -> u.getPlayer() == this && u.getBuildingTask() == null)
                        .collect(Collectors.toList());

                if(selectedUnits.isEmpty()) {
                    selectedUnits.addAll(Game.getUnitsByPlayer(this).stream().filter(u -> u.getBuildingTask() == null).collect(Collectors.toList()));
                }

                Optional<Unit> closestUnit = selectedUnits.stream()
                        .map(u -> new Pair<Unit, Float>(u, new Float(u.getPos().sub(pos.add(new Vector(0.5f, 0.5f /* TODO*/))).lengthSquared())))
                        .min((l, r) -> l.getSecond() < r.getSecond() ? -1 : 1)
                        .map(p -> p.getFirst());

                if(! closestUnit.isPresent()) {
                    logger.error("No existing units for player " + username + " to execute building command.");
                    break;
                }

                closestUnit.get().build(pos, buildingType);

                break;
            }

            case "ping": {
                this.send(new LatencyReply(((Number)message.get("t")).longValue()));
                break;
            }
        }
    }




    public void addResources(double amount) {setResourceAmount(this.resourceAmount + amount);}
    public void removeResources(double amount) {setResourceAmount(this.resourceAmount - amount);}
    public void setResourceAmount(double amount) {
        this.resourceAmount = amount;
        this.sendResourceAmount();
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
        IntStream.range(0, 10).forEach(i -> {
            Game.addUnit(new Unit(this, 0, new Vector(50, 50), 100));
            Game.addUnit(new Unit(this, 0, new Vector(52, 52), 100));
        });

        if(! Main.PRODUCTION)
            this.setResourceAmount(200);

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

    public void sendResourceAmount() {
        this.send(new ResourceAmountUpdateMessage((int)this.resourceAmount));
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
