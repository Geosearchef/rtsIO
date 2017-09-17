package de.geosearchef.rtsIO.game;

import de.geosearchef.rtsIO.IDFactory;
import de.geosearchef.rtsIO.game.buildings.Building;
import de.geosearchef.rtsIO.game.gems.Gem;
import de.geosearchef.rtsIO.js.Data;
import de.geosearchef.rtsIO.json.units.UpdateUnitMessage;
import de.geosearchef.rtsIO.util.Vector;

import java.util.HashSet;
import java.util.stream.Collectors;

@lombok.Data
public class Unit extends Targetable {

    private Player player;
    private int unitID;
    private int unitType;
    private Vector pos;
    private Vector vel;
    private Vector dest;
    private BuildingTask buildingsTask = null;

    public Unit(Player player, int unitType, Vector pos, float hp) {
        super(((Number)Data.getUnitData(unitType).get("maxHp")).floatValue());

        this.player = player;
        this.unitID = IDFactory.generateUnitID();
        this.unitType = unitType;
        this.pos = new Vector(pos);
        this.vel = new Vector();
        this.dest = new Vector();
    }

    public void update(float d) {

        Vector travel = this.vel.scale(d);

        if(travel.lengthSquared() >= dest.sub(pos).lengthSquared()) {
            this.vel = new Vector();
            this.pos = new Vector(this.dest);

            if(buildingsTask != null) {
                //Create new building
                Game.addBuilding(new Building(this.getPlayer(), this.buildingsTask.buildingType, new Vector(this.dest), Data.get));
            }
        } else {
            this.pos = this.pos.add(travel);
        }

        //Check for collision with gem
        synchronized (Game.gems) {
            Game.gems.stream()
                    .filter(g -> ! g.isSpawner())
                    .filter(g -> this.getCenter().sub(g.getCenter()).length() <= (this.getSize() + Gem.SMALL_GEM_SIZE) / 2f)//this radius is based on the entities sides, not diagonals
                    .collect(Collectors.toCollection(HashSet::new))//avoid concurrent modification
                    .forEach(gem -> {
                        Game.consumeGem(gem, this);
                    });
        }
    }


    public void move(Vector dest) {
        this.dest = dest;
        this.buildingsTask = null;

        if(dest.sub(pos).lengthSquared() == 0)
            return;

        this.vel = dest.sub(pos).normalise().scale(this.getMoveSpeed());
        PlayerManager.broadcastPlayers(new UpdateUnitMessage(this.getUnitID(), this.getPos(), this.getVel(), this.getDest(), this.getHp()));
    }

    public void build(Vector dest, int buildingType) {
        move(dest);
        this.buildingsTask = new BuildingTask(buildingType);
    }


    @Override
    public int hashCode() {
        return unitID;
    }

    //TODO
    public float getMoveSpeed() {return 3f;}

    public float getRadius() {return (float)Math.sqrt(2) / 2f * this.getSize();}

    public Vector getCenter() {
        return pos.add(new Vector(0.5f, 0.5f));
    }


    @lombok.Data
    public static class BuildingTask {
        private final int buildingType;
    }
}
