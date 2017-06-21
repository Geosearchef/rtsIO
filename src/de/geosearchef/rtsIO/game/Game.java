package de.geosearchef.rtsIO.game;

import de.geosearchef.rtsIO.json.units.DeleteUnitMessage;
import de.geosearchef.rtsIO.json.units.NewUnitMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class Game {

    public static Logger logger = LoggerFactory.getLogger(Game.class);

    public static ArrayList<Unit> units = new ArrayList<Unit>();



    public static void addUnit(Unit unit) {
        synchronized (units) {
            units.add(unit);
        }
        PlayerManager.broadcastPlayers(new NewUnitMessage(unit.getPlayer().getPlayerID(), unit.getUnitID(), unit.getUnitType(), unit.getPos(), unit.getVel(), unit.getHp()));
    }

    public static void removeUnit(Unit unit) {
        synchronized (units) {
            units.remove(unit);
        }
        PlayerManager.broadcastPlayers(new DeleteUnitMessage(unit.getUnitID()));
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
