package de.geosearchef.rtsIO.game;

import de.geosearchef.rtsIO.IDFactory;
import de.geosearchef.rtsIO.game.buildings.Building;
import de.geosearchef.rtsIO.js.Data;
import de.geosearchef.rtsIO.js.ProjectileData;
import de.geosearchef.rtsIO.json.projectiles.DeleteProjectileMessage;
import de.geosearchef.rtsIO.json.projectiles.NewProjectileMessage;
import de.geosearchef.rtsIO.util.Vector;

@lombok.Data
public class Projectile {

    private long projectileID;
    private int projectileType;
    private Player player;
    private Vector pos;
    private Vector vel;
    private Targetable target;

    private boolean destroyed = false;

    public Projectile(Player player, Vector pos, Vector vel, Targetable target) {
        this.projectileID = IDFactory.generateProjectileID();
        this.player = player;
        this.pos = pos;
        this.vel = vel;
        this.target = target;
    }

    public void update(float d) {
        if(this.target != null) {
            if(((target instanceof Unit) && !Game.units.contains((target))) || ((target instanceof Building) && !Game.buildings.contains((target)))) {
                this.destroy();
                return;
            }
            //TODO: division by zero?
            Vector toTarget = target.getCenter().sub(this.pos);
            this.vel = toTarget.normalise();

            if(toTarget.length() <= getProjectileData().getSpeed() * d) {
                //Hit
                target.damage(getProjectileData().getDamage(), this.getPlayer());
                this.destroy();
                return;
            }
        }

        this.pos = this.pos.add(vel.scale(d * getProjectileData().getSpeed()));
    }

    private void destroy() {
        destroyed = true;
    }

    public ProjectileData getProjectileData() {
        return Data.getProjectileData(this.projectileType);
    }

    @Override
    public int hashCode() {
        return (int) (this.projectileID % Integer.MAX_VALUE);
    }
}
