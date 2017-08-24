package de.geosearchef.rtsIO.json.buildings;

import de.geosearchef.rtsIO.json.Message;
import de.geosearchef.rtsIO.util.Vector;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NewBuildingMessage extends Message {

    private final String type = "newBuilding";
    private final int playerID;
    private final int buildingID;
    private final int buildingType;
    private final Vector pos;
    private final float hp;
}
