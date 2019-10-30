package is.hi.hbv501.pirategame.pirategame.game.datastructures;

import is.hi.hbv501.pirategame.pirategame.game.GameObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A data structure to represent a state of the game
 */
public class GameState {
    private World world;
    private Map<Long, GameObject> gameObjects;
    private List<Long> removedGameObjectIDs = new ArrayList<>();

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
        this.gameObjects = resolveDifferences(gameObjects);
    }

    private Map<Long, GameObject> resolveDifferences(Map<Long, GameObject> gameObjects){
        gameObjects.values().removeIf(g -> !g.isModified());
        return gameObjects;
    }

    public void addRemovedGameObjectID(long ID){
        removedGameObjectIDs.add(ID);
    }

    public void clearRemovedGameObjectIDs(){
        removedGameObjectIDs.clear();
    }

    public List<Long> getRemovedGameObjectIDs(){
        return removedGameObjectIDs;
    }
}
