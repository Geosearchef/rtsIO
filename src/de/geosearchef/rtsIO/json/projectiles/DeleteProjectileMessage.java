package de.geosearchef.rtsIO.json.projectiles;

import de.geosearchef.rtsIO.json.Message;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteProjectileMessage extends Message {

    private final String type = "deleteProjectile";
    private final long projectileID;
}
