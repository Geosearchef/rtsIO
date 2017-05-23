package de.geosearchef.rtsIO.game;

import lombok.Getter;
import org.eclipse.jetty.websocket.api.Session;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import websocket.WebSocket;

import java.io.IOException;

public class Player {

    private static final int CONNECT_TIME_OUT = 10000;//name reserve time
    private static final Logger logger = LoggerFactory.getLogger(Player.class);

    @Getter private final int id;
    @Getter private final long loginTime;
    @Getter private final String username;
    @Getter private final String loginToken;
    @Getter private boolean connected;

    private Session session;

    public Player(int id, String username, String loginToken) {
        this.id = id;
        this.username = username;
        this.loginToken = loginToken;
        this.loginTime = System.currentTimeMillis();
    }


    public void login(String token, Session session) {
        if(! token.equals(this.loginToken))
            return;

        connected = true;
        this.session = session;
        synchronized (Game.players) {
            Game.players.add(this);
        }
        synchronized (Game.connectingPlayers) {
            Game.connectingPlayers.remove(this);
        }

        JSONObject loginSuccessMessage = new JSONObject();
        loginSuccessMessage.put("type", "loginSuccess");
        loginSuccessMessage.put("username", this.username);
        this.send(loginSuccessMessage);
    }

    public void send(JSONObject message) {send(message.toJSONString());}
    public void send(String message) {
        if(!this.isConnected())
            return;

        try {
            WebSocket.INSTANCE.send(this.session, message);
        } catch(IOException e) {logger.warn("Could not send message to player: " + message, e);}
    }

    public boolean loginTimedOut() {//TODO: login timeout,  scheduled event instead
        return !this.isConnected() && (System.currentTimeMillis() - this.loginTime) >= CONNECT_TIME_OUT;
    }
}
