package de.geosearchef.rtsIO.game;

import de.geosearchef.rtsIO.util.Vector;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public abstract class Targetable {

    protected float hp;

    public abstract void damage(float amount);
    public abstract Vector getPos();
    public abstract String getTargetType();
    public abstract int getTargetID();
    //TODO DODODODO
    public float getSize() {
        return 1.0f;
    }
}
