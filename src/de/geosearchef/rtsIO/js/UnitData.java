package de.geosearchef.rtsIO.js;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UnitData {
    private String name;
    private String[] files;
    private float maxHp;
    private float movementSpeed;
    private int projectile;
    private int attackCooldown;
    private float attackRange;
}
