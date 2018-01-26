package de.geosearchef.rtsIO.game;

import de.geosearchef.rtsIO.util.Vector;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public abstract class Targetable {

    protected float hp;

    public abstract void damage(float amount, Player source);
    public abstract Vector getPos();
    public abstract Vector getCenter();
    public abstract String getTargetType();
    public abstract int getTargetID();
    public abstract Player getPlayer();
    //TODO DODODODO
    public float getSize() {
        return 1.0f;
    }
}
