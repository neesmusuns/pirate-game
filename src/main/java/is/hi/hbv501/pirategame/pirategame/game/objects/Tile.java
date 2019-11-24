package is.hi.hbv501.pirategame.pirategame.game.objects;

import is.hi.hbv501.pirategame.pirategame.game.GameObject;
import is.hi.hbv501.pirategame.pirategame.services.GameService;

public class Tile extends GameObject {
    /**
     * This boolean controls whether or not an entity
     * can traverse the tile.
     */
    private boolean isPassable;

    /**
     * This boolean control whether or not the tile
     * is a land tile. <br>
     * Land tiles cannot be traversed by boats.
     */
    private boolean isLand;

    public Tile(GameService gameService) {
        super(gameService);
        setStatic(true);
        setHasCollider(true);
    }

    public Tile(GameService gameService, int worldIndex) {
        super(gameService);
        super.setWorldIndex(worldIndex);
    }


    public boolean isPassable() {
        return isPassable;
    }

    public void setPassable(boolean passable) {
        isPassable = passable;
        setLayer("impassable");
    }

    public boolean isLand() {
        return isLand;
    }

    public void setLand(boolean land) {
        isLand = land;
        if(isLand) setLayer("land");
        else setLayer("water");
    }
}
