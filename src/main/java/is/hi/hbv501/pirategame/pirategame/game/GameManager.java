package is.hi.hbv501.pirategame.pirategame.game;

import is.hi.hbv501.pirategame.pirategame.game.datastructures.GameState;
import is.hi.hbv501.pirategame.pirategame.game.util.Input;
import is.hi.hbv501.pirategame.pirategame.game.datastructures.Vector2;
import is.hi.hbv501.pirategame.pirategame.game.datastructures.World;
import is.hi.hbv501.pirategame.pirategame.game.objects.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameManager {
    private List<GameObject> gameObjects = new ArrayList<>();
    private World world = new World();
    private Map<String, User> users = new HashMap<>();

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
        go.setScale(new Vector2(3, 3));
        go = new GameObject();
        gameObjects.add(go);
        go.setID(1);
        gameState = new GameState(world, gameObjects);
    }

    private double dirY = 2;
    private double dirX = 2;
    private double scaleF = 0;

    public void Update(){
        while(true) {
            gameObjects.forEach(GameObject::Update);

            gameObjects.forEach(obj -> users.forEach((id, u) -> {
                if(obj.getID() == 0){
                    int moveDirX = 0;
                    int moveDirY = 0;

                    if(Input.GetKey("W", u.getKeyPresses())){
                        moveDirY = 1;
                    }

                    if(Input.GetKey("A", u.getKeyPresses())){
                        moveDirX = -1;
                    }

                    if(Input.GetKey("S", u.getKeyPresses())){
                        moveDirY = -1;
                    }

                    if(Input.GetKey("D", u.getKeyPresses())){
                        moveDirX = 1;
                    }

                    obj.setPosition(new Vector2(obj.getPosition().getX() + moveDirX,
                            obj.getPosition().getY() - moveDirY));
                }
                else{
                    if (obj.getPosition().getX() > 1280 || obj.getPosition().getX() < 0)
                        dirX *= -1;

                    if (obj.getPosition().getY() > 720 || obj.getPosition().getY() < 0)
                        dirY *= -1;

                    obj.setPosition(new Vector2(obj.getPosition().getX() + dirX,
                            obj.getPosition().getY() + dirY));
                }
            }));

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

    public Map<String, User> getUsers() {
        return users;
    }

    public void addUser(String sessionID, User user) {
        users.put(sessionID, user);
    }

    public void addKeysToUser(String keys, String sessionID){
        users.get(sessionID).setKeyPresses(keys);
    }
}
