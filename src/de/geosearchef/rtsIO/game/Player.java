package de.geosearchef.rtsIO.game;

import de.geosearchef.rtsIO.json.LoginSuccessMessage;
import de.geosearchef.rtsIO.json.Message;
import lombok.Getter;
import org.eclipse.jetty.websocket.api.Session;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.geosearchef.rtsIO.websocket.WebSocket;

import java.io.IOException;

public class Player {

    private static final int CONNECT_TIME_OUT = 10000;//name reserve time
    private static final Logger logger = LoggerFactory.getLogger(Player.class);

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
    }

    public void onMessage(JSONObject message) {
        //todo: process
        switch((String)message.get("type")) {

            case "whatever": {


                break;
            }

        }
    }


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
            Game.players.forEach(p -> this.send(p.constructConnectMessage()));
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

    public JSONObject constructConnectMessage() {
        JSONObject connectMessage = new JSONObject();
        connectMessage.put("type", "playerConnect");
        connectMessage.put("id", getId());
        connectMessage.put("username", getUsername());
        return connectMessage;
    }

    public boolean isLoggedIn() {
        return isConnected();
    }

    public boolean loginTimedOut() {//TODO: login timeout,  scheduled event instead
        return !this.isConnected() && (System.currentTimeMillis() - this.loginTime) >= CONNECT_TIME_OUT;
    }
}
