package is.hi.hbv501.pirategame.pirategame.services;

import com.sun.tools.javac.util.Pair;
import is.hi.hbv501.pirategame.pirategame.game.GameObject;
import is.hi.hbv501.pirategame.pirategame.game.datastructures.GameState;
import is.hi.hbv501.pirategame.pirategame.game.objects.*;
import is.hi.hbv501.pirategame.pirategame.game.util.Input;
import is.hi.hbv501.pirategame.pirategame.game.datastructures.Vector2;
import is.hi.hbv501.pirategame.pirategame.game.datastructures.World;

import is.hi.hbv501.pirategame.pirategame.game.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GameService {
    private Map<Long, GameObject> gameObjects = new HashMap<>();
    private World world = new World(this);
    private Map<String, User> users = new HashMap<>();
    private Queue<Pair<String, User>> userQueue = new LinkedList<>();
    private Queue<String> removedUserQueue = new LinkedList<>();
    private Vector2 defaultScale = new Vector2(2, 2);

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
                Pirate obj = (Pirate) gameObjects.get(u.getPlayerObjectID());
                int moveDirX = 0;
                int moveDirY = 0;

                if(Input.GetKey("W", u.getKeyPresses())){
                    moveDirY = obj.getMoveSpeed();
                }

                if(Input.GetKey("A", u.getKeyPresses())){
                    moveDirX = -obj.getMoveSpeed();
                }

                if(Input.GetKey("S", u.getKeyPresses())){
                    moveDirY = -obj.getMoveSpeed();
                }

                if(Input.GetKey("D", u.getKeyPresses())){
                    moveDirX = obj.getMoveSpeed();
                }

                if(Input.GetKey("E", u.getKeyPresses())){
                    //if shop near
                    //enter shop
                    if(!obj.isInBoat()) {
                        for (GameObject o : getGameObjectsInRange(obj, 60)) {
                            if (o instanceof Boat) {
                                obj.enterBoat(o);
                                break;
                            }
                        }

                        for(GameObject tile : obj.GetTilesInRange(1)){
                            if(tile instanceof Shop) {
                                obj.enterShop((Shop) tile);
                                GameObject boat = addGameObject(new Boat(this));
                                boat.setPosition( new Vector2(1440, 680));
                                break;
                            }
                        }
                    }
                    else {
                        Map<Double, GameObject> tileDistances = new HashMap<>();

                        for(GameObject tile : obj.GetTilesInRange(1)){
                            if(((Tile) tile).isLand())
                                tileDistances.put(Vector2.DistanceSquared(obj.getPosition(), tile.getPosition()),
                                                  tile);
                        }

                        if(!tileDistances.isEmpty()){
                            Double[] keys = tileDistances.keySet().toArray(new Double[tileDistances.size()]);
                            Arrays.sort(keys);
                            obj.setPosition(tileDistances.get(keys[0]).getPosition());
                            obj.setInBoat(false);
                        }
                    }
                }

                if(Input.GetKey("F", u.getKeyPresses())){
                    //if another player near
                    //fight player
                }

                Vector2 prevPos = new Vector2(obj.getPosition());

                //Perform movement
                if(!obj.isInBoat())
                    obj.translate(moveDirX, - moveDirY);
                else {
                    obj.getBoat().translate(moveDirX, -moveDirY);
                    obj.moveRelativeToBoat();
                }

                //Check delta position since last iteration
                u.setDeltaMovement(Vector2.Add(u.getDeltaMovement(), Vector2.Sub(prevPos, obj.getPosition())));

                u.clearKeyPresses();
            });

            //Add connected users
            while(!userQueue.isEmpty()){
                Pair<String, User> user = userQueue.remove();
                addUser(user.fst, user.snd);
            }

            //Remove disconnected users
            while(!removedUserQueue.isEmpty()){
                String sessionID = removedUserQueue.remove();
                removeUser(sessionID);

            }

            //Iterate with a 16ms delay (60 FPS)
            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public List<GameObject> getGameObjectsInRange(GameObject gameObject, int range){
        double rangeSquared = range*range;
        ArrayList<GameObject> foundObjects = new ArrayList<>();
        for (GameObject go : getGameObjects().values()){
            if(Vector2.DistanceSquared(go.getPosition(), gameObject.getPosition()) <= rangeSquared)
                foundObjects.add(go);
        }

        return foundObjects;
    }

    public GameState getGameState() {
        return gameState;
    }

    public Map<String, User> getUsers() {
        return users;
    }

    public void enqueueUser(String sessionID, User user){
        userQueue.add(new Pair<>(sessionID, user));
    }

    private void addUser(String sessionID, User user) {
        GameObject go = addGameObject(new Pirate(this));
        go.setPosition(new Vector2(400,300)); //Center pirate on screen
        user.setPlayerObjectID(go.getID());

        go.setPosition( new Vector2(1252, 894));
        user.setDeltaMovement(new Vector2(-(1252-400), -(894-300)));
        users.put(sessionID, user);
    }

    public void requestRemoveUser(String sessionID){
        removedUserQueue.add(sessionID);
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


    public Vector2 getDefaultScale() {
        return defaultScale;
    }
}
