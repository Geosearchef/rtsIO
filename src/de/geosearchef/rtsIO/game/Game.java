package de.geosearchef.rtsIO.game;

import de.geosearchef.rtsIO.IDFactory;
import lombok.NonNull;
import org.eclipse.jetty.websocket.api.Session;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Game {

    public static Logger logger = LoggerFactory.getLogger(Game.class);

    public static final Set<Player> connectingPlayers = new HashSet<Player>();
    public static final Set<Player> players = new HashSet<Player>();








    public static void broadcastPlayers(String message) {
        synchronized (players) {
            players.forEach(p -> p.send(message));
        }
    }


    public static void attemptLogin(@NonNull String username, @NonNull String token, Session session) {
        Optional<Player> player = null;
        synchronized (connectingPlayers) {
             player = connectingPlayers.stream().filter(p -> p.getUsername().equals(username)).findAny();
        }
        player.ifPresent(p -> p.login(token, session));
    }

    public static Optional<Player> getPlayerByID(int id) {
        return players.stream().filter(p -> p.getId() == id).findAny();
    }

    public static Optional<Player> getPlayerByUsername(String username) {
        return players.stream().filter(p -> p.getUsername().equals(username)).findAny();
    }

    public static void userConnected(@NonNull String username, String token) {
        Player newPlayer = new Player(IDFactory.generatePlayerID(), username, token);
        synchronized (connectingPlayers) {
            if(isUsernameConnecting(username)) {
                logger.error("Lost player during connection process due to duplicate username: " + username);
                return;
            }
            connectingPlayers.add(newPlayer);
        }
        //TODO: only demo
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
