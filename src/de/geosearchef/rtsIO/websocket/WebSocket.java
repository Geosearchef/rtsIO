package de.geosearchef.rtsIO.websocket;

import de.geosearchef.rtsIO.game.Game;
import de.geosearchef.rtsIO.game.Player;
import lombok.Getter;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static spark.Spark.*;


@org.eclipse.jetty.websocket.api.annotations.WebSocket
public enum WebSocket {
    INSTANCE;

    public static final String WEB_SOCKET_ROUTE = "/play/socket";
    public static final int SOCKET_IDLE_TIMEOUT = 120 * 1000;//2 mins

    private Logger logger = LoggerFactory.getLogger(WebSocket.class);

    @Getter
    private Set<Session> sessions = new HashSet<Session>();

    @OnWebSocketConnect
    public void onConnect(Session session) {
        logger.info("Session connected from " + session.getRemoteAddress().getAddress().getHostAddress());
        synchronized (sessions) {
            sessions.add(session);
        }
        session.setIdleTimeout(SOCKET_IDLE_TIMEOUT);
    }

    @OnWebSocketClose
    public void onDisconnect(Session session, int statusCode, String reason) {
        logger.info("Session to " + session.getRemoteAddress().getAddress().getHostAddress() + " closed for reason: " + reason);
        synchronized (sessions) {
            sessions.remove(session);
        }
        Game.sessionClosed(session);
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String msg) {
        try {
            final JSONObject message = (JSONObject) new JSONParser().parse(msg);
            System.out.println(msg);

            if (Objects.equals((String) message.get("type"), "login")) {
                //Login
                try {
                    Game.attemptLogin((String) message.get("username"), (String) message.get("token"), session);
                } catch (Exception e) {
                    redirectToLoginPage(session);
                }
            } else {
                //Default message parsing
                Optional<Player> player = Game.getPlayerBySession(session);
                player.ifPresent(p -> CompletableFuture.runAsync(() -> p.onMessage(message)));
            }

        } catch (ParseException e) {
            logger.warn("Could not parse websocket message!", e);
            return;
        }
    }

    public void send(Session session, String message) throws IOException {
//        if (session.isOpen())
        session.getRemote().sendStringByFuture(message);
    }

    public void redirectToLoginPage(Session session) {
        logger.info("Redirecting user to login");
        try {
            send(session, "{type:\"loginFailed\"}");//TODO: fix this, seems to be not working
        } catch (IOException e) {
            logger.warn("Error while sending user back to login.", e);
        }
    }

    public static void init() {
        webSocket(WEB_SOCKET_ROUTE, WebSocket.INSTANCE);
    }
}