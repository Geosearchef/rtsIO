package de.geosearchef.rtsIO.game.gems;

import de.geosearchef.rtsIO.IDFactory;
import de.geosearchef.rtsIO.game.Game;
import de.geosearchef.rtsIO.util.Vector;
import lombok.Generated;
import lombok.Getter;

public class Gem {

    private static final int NUMBER_OF_SPAWNERS = 20;
    private static final float MIN_DISTANCE = 10;

    @Getter private int id;
    @Getter private boolean spawner;
    @Getter private Vector pos;

    public Gem(boolean spawner, Vector pos) {
        this.spawner = spawner;
        this.pos = pos;

        this.id = IDFactory.generateGemID();
    }

    @Override
    public int hashCode() {
        return id;
    }


    //TODO guarantee this to terminate
    public static void generateGemSpawners() {
        while(Game.gems.size() < NUMBER_OF_SPAWNERS) {
            Vector pos = new Vector((float) (Math.random() * Game.MAP_SIZE.getX()), (float) (Math.random() * Game.MAP_SIZE.getY()));
            double minDistance = Game.gems.stream().mapToDouble(gem -> gem.pos.sub(pos).length()).min().orElse(Double.MAX_VALUE);
            if(minDistance > MIN_DISTANCE) {
                Game.gems.add(new Gem(true, pos));
            }
        }
    }
}
