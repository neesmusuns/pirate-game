package is.hi.hbv501.pirategame.pirategame.game.datastructures;

import is.hi.hbv501.pirategame.pirategame.game.objects.Tile;

public class World {

    /**
     * A 2D array containing all world tiles
     */
    private Tile[][] tiles;

    /**
     * The seed for the world's generation algorithm
     */
    private int seed;

    public Tile[][] getTiles() {
        return tiles;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }
}
