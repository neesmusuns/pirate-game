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

    private double dirY = 2;
    private double dirX = 2;
    private double scaleF = 0;

    public void Update(){
        while(true) {
            gameObjects.forEach(GameObject::Update);

            gameObjects.forEach(obj -> {

                //Move all objects around the screen and hit the sides
                if (obj.getPosition().getX() > 1280 || obj.getPosition().getX() < 0)
                    dirX *= -1;

                if (obj.getPosition().getY() > 720 || obj.getPosition().getY() < 0)
                    dirY *= -1;

                obj.setPosition(new Vector2(obj.getPosition().getX() + dirX,
                        obj.getPosition().getY() + dirY));

                //Scale object according to a sine function
                scaleF += 0.01f;
                Vector2 tempScale = new Vector2(1, 1);
                tempScale.mult(10*Math.sin(Math.PI*scaleF));
                obj.setScale(tempScale);

            });

            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public GameState getGameState() {
        return gameState;
    }
}
