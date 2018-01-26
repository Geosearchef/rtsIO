package de.geosearchef.rtsIO.game;

import de.geosearchef.rtsIO.game.buildings.Building;
import de.geosearchef.rtsIO.game.gems.Gem;
import de.geosearchef.rtsIO.json.buildings.DeleteBuildingMessage;
import de.geosearchef.rtsIO.json.buildings.NewBuildingMessage;
import de.geosearchef.rtsIO.json.gems.DeleteGemMessage;
import de.geosearchef.rtsIO.json.gems.NewGemMessage;
import de.geosearchef.rtsIO.json.projectiles.DeleteProjectileMessage;
import de.geosearchef.rtsIO.json.projectiles.NewProjectileMessage;
import de.geosearchef.rtsIO.json.units.DeleteUnitMessage;
import de.geosearchef.rtsIO.json.units.NewUnitMessage;
import de.geosearchef.rtsIO.util.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Game {

    public static Logger logger = LoggerFactory.getLogger(Game.class);

    public static final Vector MAP_SIZE = new Vector(100, 100);//TODO: SEND TO PLAYERS

    public static Set<Unit> units = new HashSet<Unit>();
    public static Set<Gem> gems = new HashSet<Gem>();
    public static Set<Building> buildings = new HashSet<Building>();
    public static Set<Projectile> projectiles = new HashSet<>();


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

    public static void addGem(Gem gem) {
        synchronized (gems) {
            Game.gems.add(gem);
        }
        PlayerManager.broadcastPlayers(new NewGemMessage(gem.getId(), gem.getPos(), gem.isSpawner()));
    }

    public static void consumeGem(Gem gem, Unit unit) {
        synchronized (gems) {
            Game.gems.remove(gem);
        }
        PlayerManager.broadcastPlayers(new DeleteGemMessage(gem.getId()));

        unit.getPlayer().addResources(gem.getResourceAmount());
    }

    public static void addBuilding(Building building) {
        synchronized (buildings) {
            buildings.add(building);
        }
        PlayerManager.broadcastPlayers(new NewBuildingMessage(building.getPlayer().getPlayerID(), building.getBuildingID(), building.getBuildingType(), building.getPos(), building.getHp(), building.isInBuildingProcess()));
    }

    public static void removeBuilding(Building building) {
        synchronized (buildings) {
            buildings.remove(building);
        }
        PlayerManager.broadcastPlayers(new DeleteBuildingMessage(building.getBuildingID()));
    }

    public static void addProjectile(Projectile projectile) {
        synchronized (projectiles) {
            projectiles.add(projectile);
        }
        PlayerManager.broadcastPlayers(new NewProjectileMessage(projectile.getPlayer().getPlayerID(), projectile.getProjectileID(), projectile.getProjectileType(), projectile.getPos(), projectile.getVel(), projectile.getTarget().getTargetType(), projectile.getTarget().getTargetID()));
    }

    public static void removeProjectile(Projectile projectile) {
        synchronized (projectiles) {
            projectiles.remove(projectile);
        }
        PlayerManager.broadcastPlayers(new DeleteProjectileMessage(projectile.getProjectileID()));
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

    public static Collection<Targetable> getTargetables() {
        return Stream.concat(units.stream(), buildings.stream()).collect(Collectors.toCollection(HashSet::new));
    }
}
