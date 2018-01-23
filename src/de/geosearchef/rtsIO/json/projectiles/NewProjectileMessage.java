package de.geosearchef.rtsIO.json.projectiles;

import de.geosearchef.rtsIO.json.Message;
import de.geosearchef.rtsIO.util.Vector;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NewProjectileMessage extends Message {

    private final String type = "newProjectile";
    private final int playerID;
    private final long projectileID;
    private final int projectileType;
    private final Vector pos;
    private final Vector vel;
    private final String targetType;
    private final int targetID;
}
