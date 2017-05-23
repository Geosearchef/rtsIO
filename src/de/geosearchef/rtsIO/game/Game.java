package de.geosearchef.rtsIO.game;

import de.geosearchef.rtsIO.IDFactory;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class Game {

    public static Logger logger = LoggerFactory.getLogger(Game.class);

    public static final Set<Player> connectingPlayers = new HashSet<Player>();
    public static final Set<Player> players = new HashSet<Player>();









    public static void broadcastPlayers(String message) {
        synchronized (players) {
            players.forEach(p -> p.sendMessage(message));
        }
    }


    public static void logon(@NonNull String username) {
        Player newPlayer = new Player(IDFactory.generatePlayerID(), username);
        synchronized (connectingPlayers) {
            if(isUsernameConnecting(username)) {
                logger.error("Lost player during connection process due to duplicate username: " + username);
                return;
            }
            connectingPlayers.add(newPlayer);
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
