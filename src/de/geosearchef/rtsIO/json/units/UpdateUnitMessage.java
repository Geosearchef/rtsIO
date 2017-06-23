package de.geosearchef.rtsIO.json.units;

import de.geosearchef.rtsIO.json.Message;
import de.geosearchef.rtsIO.util.Vector;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UpdateUnitMessage extends Message {

    private final String type = "updateUnit";
    private final int unitID;
    private final Vector pos;
    private final Vector vel;
    private final Vector dest;
    private final float hp;
}
