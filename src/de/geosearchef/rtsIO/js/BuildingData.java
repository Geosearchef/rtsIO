package de.geosearchef.rtsIO.js;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BuildingData {
    private String name;
    private String[] files;
    private float maxHp;
    private float cost;
}
