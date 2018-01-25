package de.geosearchef.rtsIO.js;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProjectileData {
    private float speed;
    private float damage;
    private String[] files;
}
