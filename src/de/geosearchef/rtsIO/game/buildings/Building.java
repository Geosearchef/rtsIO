package de.geosearchef.rtsIO.game.buildings;

import de.geosearchef.rtsIO.IDFactory;
import de.geosearchef.rtsIO.game.Player;
import de.geosearchef.rtsIO.game.Targetable;
import de.geosearchef.rtsIO.util.Vector;
import lombok.Data;

@Data
public class Building extends Targetable {

    private Player player;
    private int buildingID;
    private int buildingType;
    private Vector pos;

    public Building(Player player, int buildingType, Vector pos, float hp) {
        super(hp);
        this.player = player;
        this.buildingType = buildingType;
        this.pos = pos;

        this.buildingID = IDFactory.generateBuildingID();
    }


    public void update(float d) {

    }
}
