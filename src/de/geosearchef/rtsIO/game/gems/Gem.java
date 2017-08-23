package de.geosearchef.rtsIO.game.gems;

import de.geosearchef.rtsIO.IDFactory;
import de.geosearchef.rtsIO.game.Game;
import de.geosearchef.rtsIO.util.Vector;
import lombok.Getter;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Gem {

    public static final float LARGE_GEM_SIZE = 3.0f;
    public static final float SMALL_GEM_SIZE = 1.0f;

    private static final int NUMBER_OF_SPAWNERS = 20;
    private static final float MIN_DISTANCE = 12;

    @Getter private int id;
    @Getter private boolean spawner;
    @Getter private Vector pos;

    public Gem(Vector pos, boolean spawner) {
        this.pos = pos;
        this.spawner = spawner;

        this.id = IDFactory.generateGemID();
    }


    private Set<Gem> children = new HashSet<Gem>();
    //is synchronized in update
    public Optional<Gem> generateNewGem(float d) {

        children.stream().filter(gem -> !Game.gems.contains(gem)).forEach(children::remove);

        if(children.size() < 5 && Math.random() < (0.2 - 0.04 * children.size()) * d) {
            double angle = Math.random() * Math.PI * 2.0;
            float radius = LARGE_GEM_SIZE / 2.0f + SMALL_GEM_SIZE / 2.0f + 0.5f;
            Gem gem = new Gem(this.getPos().add(new Vector((float)Math.cos(angle), (float)Math.sin(angle)).scale(radius)).add(new Vector(LARGE_GEM_SIZE / 2f - SMALL_GEM_SIZE / 2f, LARGE_GEM_SIZE / 2f - SMALL_GEM_SIZE / 2f)), false);
            children.add(gem);

            return Optional.of(gem);
        }

        return Optional.empty();
    }


    @Override
    public int hashCode() {
        return id;
    }


    //TODO guarantee this to terminate
    public static void generateGemSpawners() {
        while(Game.gems.size() < NUMBER_OF_SPAWNERS) {
            final Vector pos = new Vector((float) (Math.random() * Game.MAP_SIZE.getX()), (float) (Math.random() * Game.MAP_SIZE.getY()));
            double minDistance = Game.gems.stream().mapToDouble(gem -> gem.pos.sub(pos).length()).min().orElse(Double.MAX_VALUE);
            if(minDistance > MIN_DISTANCE) {
                Game.gems.add(new Gem(pos, true));
            }
        }
    }
}
