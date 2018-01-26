package de.geosearchef.rtsIO.game;

import de.geosearchef.rtsIO.game.buildings.Building;
import de.geosearchef.rtsIO.game.gems.Gem;
import de.geosearchef.rtsIO.json.projectiles.DeleteProjectileMessage;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

public class Updater {

    public static final int UPDATES_PER_SECOND = 60;
    public static final int UPDATE_TIME = 1000 / UPDATES_PER_SECOND;

    private static void update(float d) {

        synchronized (Game.units) {
            for(Unit unit : Game.units) {
                unit.update(d);
            }
        }

        Set<Gem> newGems = new HashSet<Gem>();
        synchronized (Game.gems) {
            for(Gem gem : Game.gems) {
                if(gem.isSpawner()) {
                    gem.generateNewGem(d).ifPresent(newGems::add);
                }
            }
        }
        newGems.forEach(Game::addGem);

        synchronized (Game.buildings) {
            for(Building building : Game.buildings) {
                building.update(d);
            }
        }

        synchronized (Game.projectiles) {
            for(Projectile projectile : Game.projectiles) {
                projectile.update(d);
            }

//            Iterator<Projectile> iter = Game.projectiles.iterator();
//            while(iter.hasNext()) {
//                Projectile projectile = iter.next();
//                if(projectile.isDestroyed()) {
//                    PlayerManager.broadcastPlayers(new DeleteProjectileMessage(projectile.getProjectileID()));
//                    iter.remove();
//                }
//            }
            Game.projectiles.stream()
                    .filter(Projectile::isDestroyed).collect(Collectors.toCollection(HashSet::new))
                    .forEach(Game::removeProjectile);
        }


    }

    private static long lastFrame = 0;
    private static void gameLoop() {
        while(true) {
            while(lastFrame + UPDATE_TIME > System.currentTimeMillis()) {try {Thread.sleep(Math.max(1, (int)((lastFrame + UPDATE_TIME - System.currentTimeMillis()) * 0.8f)));} catch (InterruptedException e) {}}

            int delta = (int) (System.currentTimeMillis() - lastFrame);
            lastFrame += delta;

            update(delta / 1000f);
        }
    }

    public static void init() {
        lastFrame = System.currentTimeMillis();
        new Thread(() -> gameLoop()).start();
    }

}
