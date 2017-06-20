package de.geosearchef.rtsIO.json.units;

import de.geosearchef.rtsIO.json.Message;
import de.geosearchef.rtsIO.json.Vector;
import lombok.Data;

@Data
public class NewUnitMessage extends Message {

    private final String type = "newUnit";
    private final int playerID;
    private final int unitID;
    private final int unitType;
    private final Vector pos;
    private final Vector vel;
    private final int hp;
}
