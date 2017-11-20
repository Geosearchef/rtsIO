package de.geosearchef.rtsIO.json;

import lombok.Data;

@Data
public class LatencyReply extends Message {
    private final String type = "pong";
    private final long t;
}
