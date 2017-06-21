package de.geosearchef.rtsIO.game;

import de.geosearchef.rtsIO.IDFactory;
import de.geosearchef.rtsIO.util.Vector;
import lombok.Data;

@Data
public class Unit {
    private Player player;
    private int unitID;
    private int unitType;
    private Vector pos;
    private Vector vel;
    private float hp;

    public Unit(Player player, int unitType, Vector pos, float hp) {
        this.player = player;
        this.unitID = IDFactory.generateUnitID();
        this.unitType = unitType;
        this.pos = new Vector(pos);
        this.hp = hp;
    }
}
