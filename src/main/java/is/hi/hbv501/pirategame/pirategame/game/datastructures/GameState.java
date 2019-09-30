package is.hi.hbv501.pirategame.pirategame.game.datastructures;

import is.hi.hbv501.pirategame.pirategame.game.GameObject;

import java.util.List;

public class GameState {
    private World world;
    private List<GameObject> gameObjects;

    public GameState(World world, List<GameObject> gameObjects) {
        this.world = world;
        this.gameObjects = gameObjects;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public List<GameObject> getGameObjects() {
        return gameObjects;
    }

    public void setGameObjects(List<GameObject> gameObjects) {
        this.gameObjects = gameObjects;
    }
}
