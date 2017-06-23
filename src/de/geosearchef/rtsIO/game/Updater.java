package de.geosearchef.rtsIO.game;

public class Updater {

    public static final int UPDATES_PER_SECOND = 60;
    public static final int UPDATE_TIME = 1000 / UPDATES_PER_SECOND;

    private static void update(float d) {

        for(Unit unit : Game.units) {
            unit.update(d);
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
