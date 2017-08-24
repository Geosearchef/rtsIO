package de.geosearchef.rtsIO.json.buildings;

import de.geosearchef.rtsIO.json.Message;
import de.geosearchef.rtsIO.util.Vector;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteBuildingMessage extends Message {

    private final String type = "deleteBuilding";
    private final int buildingID;
}
