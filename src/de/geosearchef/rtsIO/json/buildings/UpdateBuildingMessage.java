package de.geosearchef.rtsIO.json.buildings;

import de.geosearchef.rtsIO.json.Message;
import de.geosearchef.rtsIO.util.Vector;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UpdateBuildingMessage extends Message {

    private final String type = "updateBuilding";
    private final int buildingID;
    private final float hp;
    private final boolean inBuildingProcess;
}
