package de.geosearchef.rtsIO.json;

import lombok.Data;

@Data
public class PlayerDisconnectMessage extends Message {
    private final String type = "playerDisconnect";
    private final int id;
}
