package de.geosearchef.rtsIO.json;

import lombok.Data;

@Data
public class PlayerConnectMessage extends Message {
    private final String type = "playerConnect";
    private final int id;
    private final String username;
}
