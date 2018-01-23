package de.geosearchef.rtsIO.json.projectiles;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteProjectileMessage {

    private final String type = "deleteProjectile";
    private final long projectileID;
}
