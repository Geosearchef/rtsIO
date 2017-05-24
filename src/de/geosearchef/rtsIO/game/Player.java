package de.geosearchef.rtsIO.game;

import de.geosearchef.rtsIO.json.LoginSuccessMessage;
import de.geosearchef.rtsIO.json.Message;
import de.geosearchef.rtsIO.json.PlayerConnectMessage;
import lombok.Getter;
import org.eclipse.jetty.websocket.api.Session;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.geosearchef.rtsIO.websocket.WebSocket;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Player {

    private static final int LOGIN_TIME_OUT = 10 * 1000;//name reserve time
    private static final Logger logger = LoggerFactory.getLogger(Player.class);
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

    @Getter private final int id;
    @Getter private final long loginTime;
    @Getter private final String username;
    @Getter private final String loginToken;
    @Getter private boolean connected;//needs to be logged in, else false

    @Getter private Session session;

    public Player(int id, String username, String loginToken) {
        this.id = id;
        this.username = username;
        this.loginToken = loginToken;
        this.loginTime = System.currentTimeMillis();

        scheduleLoginTimeout();
    }

    public void onMessage(JSONObject message) {
        //TODO: process
        switch((String)message.get("type")) {

            case "whatever": {


                break;
            }

        }
    }

    //attempts login of player via token over websocket
    public void login(String token, Session session) {
        if(! token.equals(this.loginToken)) {
            WebSocket.INSTANCE.redirectToLoginPage(session);
            synchronized (Game.connectingPlayers) {
                Game.connectingPlayers.remove(this);
            }
            return;
        }

        connected = true;
        this.session = session;
        synchronized (Game.players) {
            Game.players.add(this);
        }
        synchronized (Game.connectingPlayers) {
            Game.connectingPlayers.remove(this);
        }

        this.send(new LoginSuccessMessage(this.username, this.id));

        //Send game information to new connecting player

        //Send all players to this player
        synchronized (Game.players) {
            Game.players.forEach(p -> this.send(new PlayerConnectMessage(p.getId(), p.getUsername())));
        }

        logger.info("User " + this.username + " connected");
    }

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
        synchronized (Game.connectingPlayers) {
            if(!this.isConnected() && Game.connectingPlayers.contains(this)) {
                logger.info("Player " + username + ": login timed out");
                Game.connectingPlayers.remove(this);
            }
        }
    }
}
