package de.geosearchef.rtsIO.game;

import de.geosearchef.rtsIO.game.gems.Gem;
import de.geosearchef.rtsIO.json.units.DeleteUnitMessage;
import de.geosearchef.rtsIO.json.units.NewUnitMessage;
import de.geosearchef.rtsIO.util.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class Game {

    public static Logger logger = LoggerFactory.getLogger(Game.class);

    public static final Vector MAP_SIZE = new Vector(100, 100);//TODO: SEND TO PLAYERS

    public static Set<Unit> units = new HashSet<Unit>();
    public static Set<Gem> gems = new HashSet<Gem>();


    public static void addUnit(Unit unit) {
        synchronized (units) {
            units.add(unit);
        }
        PlayerManager.broadcastPlayers(new NewUnitMessage(unit.getPlayer().getPlayerID(), unit.getUnitID(), unit.getUnitType(), unit.getPos(), unit.getVel(), unit.getDest(), unit.getHp()));
    }

    public static void removeUnit(Unit unit) {
        synchronized (units) {
            units.remove(unit);
        }
        PlayerManager.broadcastPlayers(new DeleteUnitMessage(unit.getUnitID()));
    }


    public static void init() {
        Gem.generateGemSpawners();
    }


    public static Optional<Unit> getUnitByID(int unitID) {
        synchronized (units) {
            return units.stream().filter(u -> u.getUnitID() == unitID).findAny();
        }
    }

    public static List<Unit> getUnitsByPlayer(Player player) {
        synchronized (units) {
            return units.stream().filter(u -> u.getPlayer() == player).collect(Collectors.toList());
        }
    }
}
