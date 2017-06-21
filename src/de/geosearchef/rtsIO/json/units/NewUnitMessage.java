package de.geosearchef.rtsIO.json.units;

import de.geosearchef.rtsIO.json.Message;
import de.geosearchef.rtsIO.util.Vector;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class NewUnitMessage extends Message {

    private final String type = "newUnit";
    private final int playerID;
    private final int unitID;
    private final int unitType;
    private final Vector pos;
    private final Vector vel;
    private final float hp;
}
