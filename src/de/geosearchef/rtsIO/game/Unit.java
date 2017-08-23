package de.geosearchef.rtsIO.game;

import de.geosearchef.rtsIO.IDFactory;
import de.geosearchef.rtsIO.json.units.UpdateUnitMessage;
import de.geosearchef.rtsIO.util.Vector;
import lombok.Data;

@Data
public class Unit {

    private Player player;
    private int unitID;
    private int unitType;
    private Vector pos;
    private Vector vel;
    private Vector dest;
    private float hp;

    public Unit(Player player, int unitType, Vector pos, float hp) {
        this.player = player;
        this.unitID = IDFactory.generateUnitID();
        this.unitType = unitType;
        this.pos = new Vector(pos);
        this.vel = new Vector();
        this.dest = new Vector();
        this.hp = hp;
    }

    public void update(float d) {

        Vector travel = this.vel.scale(d);

        if(travel.lengthSquared() >= dest.sub(pos).lengthSquared()) {
            this.vel = new Vector();
            this.pos = new Vector(this.dest);
        } else {
            this.pos = this.pos.add(travel);
        }

    }


    public void move(Vector dest) {
        this.dest = dest;

        if(dest.sub(pos).lengthSquared() == 0)
            return;

        this.vel = dest.sub(pos).normalise().scale(this.getMoveSpeed());
        PlayerManager.broadcastPlayers(new UpdateUnitMessage(this.getUnitID(), this.getPos(), this.getVel(), this.getDest(), this.getHp()));
    }


    @Override
    public int hashCode() {
        return unitID;
    }

    public float getMoveSpeed() {return 3f;}
}