package de.geosearchef.rtsIO.json.units;

import de.geosearchef.rtsIO.json.Message;
import lombok.Data;

@Data
public class DeleteUnitMessage extends Message {

    private final String type = "deleteUnit";
    private final int unitID;

}
