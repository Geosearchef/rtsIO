package de.geosearchef.rtsIO.game;

import lombok.Getter;
import org.eclipse.jetty.websocket.api.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import websocket.WebSocket;

import java.io.IOException;

public class Player {

    private static final int CONNECT_TIME_OUT = 10000;//name reserve time
    private static final Logger logger = LoggerFactory.getLogger(Player.class);

    @Getter private int id;
    @Getter private long loginTime;
    @Getter private String username;
    @Getter private boolean connected;

    private Session session;

    public Player(int id, String username) {
        this.id = id;
        this.username = username;
        this.loginTime = System.currentTimeMillis();
    }


    public void connected() {

        connected = true;
    }

    public void sendMessage(String message) {
        if(!this.isConnected())
            return;

        try {
            WebSocket.INSTANCE.send(this.session, message);
        } catch(IOException e) {logger.warn("Could not send message to player: " + message, e);}
    }

    public boolean loginTimedOut() {
        return !this.isConnected() && (System.currentTimeMillis() - this.loginTime) >= CONNECT_TIME_OUT;
    }
}
