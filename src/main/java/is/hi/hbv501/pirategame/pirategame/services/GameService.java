package is.hi.hbv501.pirategame.pirategame.services;

import com.sun.tools.javac.util.Pair;
import is.hi.hbv501.pirategame.pirategame.game.GameObject;
import is.hi.hbv501.pirategame.pirategame.game.datastructures.GameState;
import is.hi.hbv501.pirategame.pirategame.game.objects.Pirate;
import is.hi.hbv501.pirategame.pirategame.game.util.Input;
import is.hi.hbv501.pirategame.pirategame.game.datastructures.Vector2;
import is.hi.hbv501.pirategame.pirategame.game.datastructures.World;
import is.hi.hbv501.pirategame.pirategame.game.objects.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GameService {
    private Map<Long, GameObject> gameObjects = new HashMap<>();
    private World world = new World(this);
    private Map<String, User> users = new HashMap<>();
    private sun.misc.Queue<Pair<String, User>> userQueue = new sun.misc.Queue<>();
    private sun.misc.Queue<String> removedUserQueue = new sun.misc.Queue<>();

    private GameState gameState;

    private int worldSize = 100;

    @Autowired
    private UserService userService;

    public void Initialize(){
        Start();
        Update();
    }

    private void Start(){
        gameState = new GameState(world, gameObjects);
        world.generateWorld(worldSize, worldSize);
        System.out.println("Finished generating world");
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
                    moveDirY = 3;
                }

                if(Input.GetKey("A", u.getKeyPresses())){
                    moveDirX = -3;
                }

                if(Input.GetKey("S", u.getKeyPresses())){
                    moveDirY = -3;
                }

                if(Input.GetKey("D", u.getKeyPresses())){
                    moveDirX = 3;
                }

                Vector2 prevPos = new Vector2(obj.getPosition());

                //Perform movement
                obj.translate(moveDirX, - moveDirY);

                //Check delta position since last iteration
                u.setDeltaMovement(Vector2.Add(u.getDeltaMovement(), Vector2.Sub(prevPos, obj.getPosition())));

                u.clearKeyPresses();
            });

            //Add connected users
            while(!userQueue.isEmpty()){
                try {
                    Pair<String, User> user = userQueue.dequeue();
                    addUser(user.fst, user.snd);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //Remove disconnected users
            while(!removedUserQueue.isEmpty()){
                try {
                    String sessionID = removedUserQueue.dequeue();
                    removeUser(sessionID);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //Iterate with a 16ms delay (60 FPS)
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

    public void enqueueUser(String sessionID, User user){
        userQueue.enqueue(new Pair<>(sessionID, user));
    }

    private void addUser(String sessionID, User user) {
        GameObject go = addGameObject(new Pirate(this));
        go.setPosition(new Vector2(400,300));
        go.setScale(new Vector2(2,2));
        user.setPlayerObjectID(go.getID());

        go.setPosition( new Vector2(1252, 894));
        user.setDeltaMovement(new Vector2(-(1252-400), -(894-300)));
        users.put(sessionID, user);
    }

    public void requestRemoveUser(String sessionID){
        removedUserQueue.enqueue(sessionID);
    }

    public void removeUser(String sessionID){
        User removedUser = users.remove(sessionID);
        userService.insertUser(removedUser);
        removeGameObject(removedUser.getPlayerObjectID());
    }

    private GameObject addGameObject(GameObject go){
        go.setService(this);
        gameObjects.put(go.getID(), go);
        return go;
    }

    private void removeGameObject(long ID){
        gameState.addRemovedGameObjectID(ID);
        gameObjects.remove(ID);
    }

    public HashMap<Long, GameObject> getGameObjects(){
        return new HashMap<>(gameObjects);
    }

    public void addKeysToUser(String keys, String sessionID){
        users.get(sessionID).setKeyPresses(keys);
    }

    public int getWorldSize() {
        return worldSize;
    }

    public void setWorldSize(int worldSize) {
        this.worldSize = worldSize;
    }


}
