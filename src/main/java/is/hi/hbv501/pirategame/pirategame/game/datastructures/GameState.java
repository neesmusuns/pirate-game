package is.hi.hbv501.pirategame.pirategame.game.datastructures;

import is.hi.hbv501.pirategame.pirategame.game.GameObject;

import java.util.List;
import java.util.Map;

/**
 * A data structure to represent a state of the game
 */
public class GameState {
    private World world;
    private Map<Long, GameObject> gameObjects;

    public GameState(World world, Map<Long, GameObject> gameObjects) {
        this.world = world;
        this.gameObjects = gameObjects;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public Map<Long, GameObject> getGameObjects() {
        return gameObjects;
    }

    public void setGameObjects(Map<Long, GameObject> gameObjects) {
        this.gameObjects = gameObjects;
    }
}
