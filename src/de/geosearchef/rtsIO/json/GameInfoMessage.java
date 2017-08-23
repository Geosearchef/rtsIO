package de.geosearchef.rtsIO.json;

import de.geosearchef.rtsIO.util.Vector;
import lombok.Data;

/**
 * Created by Geosearchef on 23.05.2017.
 */

@Data
public class GameInfoMessage extends Message {
    private final String type = "gameInfo";
    private final Vector mapSize;
}
