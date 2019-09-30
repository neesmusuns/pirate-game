package is.hi.hbv501.pirategame.pirategame.game;

import is.hi.hbv501.pirategame.pirategame.game.datastructures.GameState;
import is.hi.hbv501.pirategame.pirategame.game.datastructures.Vector2;
import is.hi.hbv501.pirategame.pirategame.game.datastructures.World;

import java.util.ArrayList;
import java.util.List;

public class GameManager {
    private List<GameObject> gameObjects = new ArrayList<>();
    private World world = new World();

    private GameState gameState;

    public static GameManager gm;

    public static void Initialize(){
        gm = new GameManager();
        gm.Start();
        gm.Update();
    }

    public void Start(){
        GameObject go = new GameObject();
        go.setSprite("pirate");
        gameObjects.add(go);
        gameState = new GameState(world, gameObjects);
    }

    public void Update(){
        //gameObjects.forEach(GameObject::Update);

        gameObjects.forEach(obj -> {
            obj.setPosition(new Vector2(obj.getPosition().getX() + 1f,
                                        obj.getPosition().getY() + 1f));
        });

        try {
            Thread.sleep(16);
            Update();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public GameState getGameState() {
        return gameState;
    }
}
