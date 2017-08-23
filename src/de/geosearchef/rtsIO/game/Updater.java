package de.geosearchef.rtsIO.game;

import de.geosearchef.rtsIO.game.gems.Gem;
import de.geosearchef.rtsIO.json.gems.NewGemMessage;

import java.util.HashSet;
import java.util.Set;

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
            newGems.forEach(gem -> {
                Game.gems.add(gem);
                PlayerManager.broadcastPlayers(new NewGemMessage(gem.getId(), gem.getPos(), gem.isSpawner()));
            });
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
