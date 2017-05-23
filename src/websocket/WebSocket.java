package websocket;

import lombok.Getter;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static spark.Spark.*;


@org.eclipse.jetty.websocket.api.annotations.WebSocket
public enum WebSocket {
    INSTANCE;

    public Logger logger = LoggerFactory.getLogger(WebSocket.class);

    @Getter
    private Set<Session> sessions = new HashSet<Session>();

    @OnWebSocketConnect
    public void onConnect(Session session) {
        logger.debug("Session connected from " + session.getRemoteAddress().getAddress().getHostAddress());
        synchronized (sessions) {
            sessions.add(session);
        }
    }

    @OnWebSocketClose
    public void onDisconnect(Session session) {
        logger.debug("Session to " + session.getRemoteAddress().getAddress().getHostAddress() + " closed");
        synchronized (sessions) {
            sessions.remove(session);
        }
        //TODO: inform game
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        //TODO: login from player
        //TODO: interpret
    }

    public void send(Session session, String message) throws IOException {
        if (session.isOpen())
            session.getRemote().sendStringByFuture(message);
    }

    public static void init() {
        webSocket("/play/websocket", WebSocket.INSTANCE);
    }
}
