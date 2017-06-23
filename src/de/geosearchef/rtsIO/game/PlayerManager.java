package de.geosearchef.rtsIO.game;

import de.geosearchef.rtsIO.IDFactory;
import de.geosearchef.rtsIO.json.Message;
import de.geosearchef.rtsIO.json.PlayerConnectMessage;
import de.geosearchef.rtsIO.json.PlayerDisconnectMessage;
import de.geosearchef.rtsIO.websocket.WebSocket;
import lombok.NonNull;
import org.eclipse.jetty.websocket.api.Session;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class PlayerManager {

    public static Logger logger = LoggerFactory.getLogger(PlayerManager.class);

    public static final Set<Player> connectingPlayers = new HashSet<Player>();
    public static final Set<Player> players = new HashSet<Player>();



    public static void broadcastPlayers(Message message) {broadcastPlayers(message.toJson());}
    public static void broadcastPlayers(JSONObject message) {broadcastPlayers(message.toJSONString());}
    public static void broadcastPlayers(String message) {
        synchronized (players) {
            players.forEach(p -> p.send(message));
        }
    }


    //Called from websocket, after user tries to login
    public static void attemptLogin(@NonNull String username, @NonNull String token, Session session) {
        Optional<Player> player = null;
        synchronized (connectingPlayers) {
            player = connectingPlayers.stream().filter(p -> p.getUsername().equals(username)).findAny();
        }
        if (player.isPresent()) {
            player.get().login(token, session);
            if (player.get().isLoggedIn()) {
                //Broadcast logged in player to other players (including the player itself) TODO: logging in player is contained 2 times????
                broadcastPlayers(new PlayerConnectMessage(player.get().getPlayerID(), player.get().getUsername()));
            }
        } else {
            //Wrong username, token or timed out
            WebSocket.INSTANCE.redirectToLoginPage(session);
        }
    }

    //on connect to websocket, user is not yet logged in
    public static void userConnected(@NonNull String username, String token) {
        Player newPlayer = new Player(IDFactory.generatePlayerID(), username, token);
        synchronized (connectingPlayers) {
            if (isUsernameConnecting(username)) {
                logger.error("Lost player during connection process due to duplicate username: " + username);
                return;
            }
            connectingPlayers.add(newPlayer);
        }
    }


    public static void playerDisconnected(Player player) {
        synchronized (players) {
            players.remove(player);
        }

        //Remove all units of this player
        synchronized (Game.units) {
            Game.units.stream().filter(u -> u.getPlayer() == player).forEach(u -> Game.units.remove(u));
        }

        logger.info(player.getUsername() + " disconnected");
        broadcastPlayers(new PlayerDisconnectMessage(player.getPlayerID()));
    }

    public static void sessionClosed(Session session) {
        synchronized (players) {
            getPlayerBySession(session).ifPresent(p -> playerDisconnected(p));
        }
    }

    public static Optional<Player> getPlayerByID(int id) {
        synchronized (players) {
            return players.stream().filter(p -> p.getPlayerID() == id).findAny();
        }
    }

    public static Optional<Player> getPlayerByUsername(String username) {
        synchronized (players) {
            return players.stream().filter(p -> p.getUsername().equals(username)).findAny();
        }
    }

    public static Optional<Player> getPlayerBySession(Session session) {
        synchronized (players) {
            return players.stream().filter(p -> p.getSession().equals(session)).findAny();
        }
    }

    public static boolean isUsernameInUse(String username) {
        return isUsernameConnecting(username) || isUsernameConnected(username);
    }

    private static boolean isUsernameConnecting(String username) {
        synchronized (connectingPlayers) {
            return connectingPlayers.stream().anyMatch(p -> p.getUsername().equals(username));
        }
    }

    private static boolean isUsernameConnected(String username) {
        synchronized (players) {
            return players.stream().anyMatch(p -> p.getUsername().equals(username));
        }
    }
}
