package is.hi.hbv501.pirategame.pirategame.game.datastructures;

import is.hi.hbv501.pirategame.pirategame.game.objects.Tile;

public class World {

    private Tile[][] tiles;

    private int seed;

    public Tile[][] getTiles() {
        return tiles;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }
}
