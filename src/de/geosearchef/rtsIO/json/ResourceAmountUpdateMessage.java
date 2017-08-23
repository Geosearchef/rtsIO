package de.geosearchef.rtsIO.json;

import lombok.Data;

/**
 * Created by Geosearchef on 23.05.2017.
 */

@Data
public class ResourceAmountUpdateMessage extends Message {
    private final String type = "resourceAmountUpdate";
    private final int resourceAmount;
}
