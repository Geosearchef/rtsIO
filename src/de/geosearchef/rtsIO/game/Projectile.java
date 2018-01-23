package de.geosearchef.rtsIO.game;

import de.geosearchef.rtsIO.IDFactory;
import de.geosearchef.rtsIO.json.projectiles.NewProjectileMessage;
import de.geosearchef.rtsIO.util.Vector;
import lombok.Data;

@Data
public class Projectile {

    private long projectileID;
    private int projectileType;
    private Player player;
    private Vector pos;
    private Vector vel;
    private Targetable target;
    private int directDamage;

    private boolean destroyed = false;

    public Projectile(Player player, Vector pos, Vector vel, Targetable target, int directDamage) {
        this.projectileID = IDFactory.generateProjectileID();
        this.pos = pos;
        this.vel = vel;
        this.target = target;
        this.directDamage = directDamage;

        PlayerManager.broadcastPlayers(new NewProjectileMessage(player.getPlayerID(), projectileID, projectileType, pos, vel, target.getTargetType(), target.getTargetID()));
    }

    public void update(float d) {

        if(this.target != null) {
            Vector toTarget = target.getPos().sub(this.pos);
            this.vel = toTarget.normalise();

            if(toTarget.length() <= vel.length() * d) {
                //Hit
                target.damage(directDamage);
                this.destroy();
            }
        }

        this.pos = this.pos.add(vel.scale(d));
    }

    private void destroy() {
        destroyed = false;
        //send destroy
    }
}
