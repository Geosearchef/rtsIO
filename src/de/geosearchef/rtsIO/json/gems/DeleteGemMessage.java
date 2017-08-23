package de.geosearchef.rtsIO.json.gems;

import de.geosearchef.rtsIO.json.Message;
import de.geosearchef.rtsIO.util.Vector;
import lombok.Data;

/**
 * Created by Geosearchef on 23.05.2017.
 */

@Data
public class DeleteGemMessage extends Message {
    private final String type = "deleteGem";
    private final int id;
}
