package is.hi.hbv501.pirategame.pirategame.services;

import is.hi.hbv501.pirategame.pirategame.game.GameObject;
import is.hi.hbv501.pirategame.pirategame.game.datastructures.GameState;
import is.hi.hbv501.pirategame.pirategame.game.util.Input;
import is.hi.hbv501.pirategame.pirategame.game.datastructures.Vector2;
import is.hi.hbv501.pirategame.pirategame.game.datastructures.World;
import is.hi.hbv501.pirategame.pirategame.game.objects.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class GameService {
    private Map<Long, GameObject> gameObjects = new HashMap<>();
    private World world = new World();
    private Map<String, User> users = new HashMap<>();

    private GameState gameState;

    @Autowired
    private UserService userService;

    public void Initialize(){
        Start();
        Update();
    }

    private void Start(){
        gameState = new GameState(world, gameObjects);
    }

    private void Update(){
        while(true) {
            for (GameObject go : gameObjects.values()) {
                go.Update();
            }

            users.values().forEach(u -> {
                GameObject obj = gameObjects.get(u.getPlayerObjectID());
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

    public Map<String, User> getUsers() {
        return users;
    }

    public void addUser(String sessionID, User user) {
        GameObject go = addGameObject();
        user.setPlayerObjectID(go.getID());
        users.put(sessionID, user);
    }

    public void removeUser(String sessionID){
        User removedUser = users.remove(sessionID);
        userService.insertUser(removedUser);
        removeGameObject(removedUser.getPlayerObjectID());
    }

    private GameObject addGameObject(){
        GameObject go = new GameObject();
        gameObjects.put(go.getID(), go);
        return go;
    }

    private void removeGameObject(long ID){
        gameState.addRemovedGameObjectID(ID);
        gameObjects.remove(ID);
    }

    public void addKeysToUser(String keys, String sessionID){
        users.get(sessionID).setKeyPresses(keys);
    }
}
