package is.hi.hbv501.pirategame.pirategame.services;

import com.sun.tools.javac.util.Pair;
import is.hi.hbv501.pirategame.pirategame.game.GameObject;
import is.hi.hbv501.pirategame.pirategame.game.datastructures.GameState;
import is.hi.hbv501.pirategame.pirategame.game.datastructures.Vector2;
import is.hi.hbv501.pirategame.pirategame.game.datastructures.World;
import is.hi.hbv501.pirategame.pirategame.game.objects.*;
import is.hi.hbv501.pirategame.pirategame.game.util.Input;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GameService {
    private Map<Long, GameObject> gameObjects = new HashMap<>();
    int playerLimit = 10;
    private World[] worlds = new World[10];
    private Map<String, User> users = new HashMap<>();
    private Queue<Pair<String, User>> userQueue = new LinkedList<>();
    private Queue<String> removedUserQueue = new LinkedList<>();
    private Vector2 defaultScale = new Vector2(2, 2);

    private GameState gameState;

    @Autowired
    private UserService userService;

    public void Initialize(){
        Start();
        Update();
    }

    private void Start(){
        worlds[0] = new World(this);
        worlds[0].generateWorld("world.txt", 0);
        worlds[1] = new World(this);
        worlds[1].generateWorld("divingworld.txt", 1);

        TreasureMarker marker = (TreasureMarker) addGameObject(new TreasureMarker(this));
        marker.setPosition(new Vector2(1520, 1320));

        gameState = new GameState(worlds, gameObjects);
        System.out.println("Finished generating world");
    }

    private void Update(){
        while(true) {
            for (GameObject go : gameObjects.values()) {
                go.Update();
            }

            users.values().forEach(u -> {
                Pirate obj = (Pirate) gameObjects.get(u.getPlayerObjectID());
                obj.setTooltip("");
                Vector2 prevPos = new Vector2(obj.getPosition());
                int moveDirX = 0;
                int moveDirY = 0;
                boolean isNearBoat = false;
                GameObject foundBoat = null;
                boolean isNearShop = false;
                Shop foundShop = null;
                boolean isNearMarker = false;
                TreasureMarker foundMarker;


                /*
                 * CHECK OBJECTS IN PROXIMITY
                 */
                if(!obj.isInBoat()) {
                    //If near boat
                    for (GameObject o : getGameObjectsInRange(obj, 60, u.getWorldIndex())) {
                        if (o instanceof Boat) {
                            isNearBoat = true;
                            foundBoat = o;
                            obj.setTooltip("Press 'E' to enter boat");
                        }
                        if(o instanceof TreasureMarker){
                            isNearMarker = true;
                            foundMarker = (TreasureMarker) o;
                            obj.setTooltip("Press 'E' to enter dive");
                        }
                    }

                    //If near shop
                    for(GameObject tile : obj.GetTilesInRange(1)){
                        if(tile instanceof Shop) {
                            obj.setTooltip("Press 'E' to enter shop");
                            isNearShop = true;
                            foundShop = (Shop) tile;
                            break;
                        }
                    }
                }

                /*
                 * CHECK INPUT
                 */

                if(Input.GetKey("W", u.getKeyPresses())){
                    moveDirY += obj.getMoveSpeed();
                }

                if(Input.GetKey("A", u.getKeyPresses())){
                    moveDirX += -obj.getMoveSpeed();
                }

                if(Input.GetKey("S", u.getKeyPresses())){
                    moveDirY += -obj.getMoveSpeed();
                }

                if(Input.GetKey("D", u.getKeyPresses())){
                    moveDirX += obj.getMoveSpeed();
                }

                if(Input.GetKey("E", u.getKeyPresses())){
                    if(isNearBoat){
                        obj.enterBoat(foundBoat);
                    } else if(isNearShop){
                        obj.enterShop(foundShop);
                        GameObject boat = addGameObject(new Boat(this));
                        boat.setPosition( new Vector2(1520, 1320));
                    } else if(isNearMarker){
                        obj.dive();
                        u.setWorldIndex(1);
                        obj.setWorldIndex(1);
                        obj.setPosition(new Vector2(120, 20));
                    } else if(obj.isDiving()){
                        obj.exitDive();
                        u.setWorldIndex(0);
                    }
                    //If in boat and press E
                    else {
                        obj.exitBoat();
                    }
                }

                if(Input.GetKey("F", u.getKeyPresses())){
                    //if another player near
                    //fight player
                }

                /*
                 * OUTPUT MOVEMENT
                 */

                if(obj.isDiving())
                    moveDirY -= 1;

                //Perform movement
                if(!obj.isInBoat()) {
                    obj.translate(moveDirX, 0);
                    obj.translate(0, -moveDirY);
                }
                else {
                    obj.getBoat().translate(0, -moveDirY);
                    obj.getBoat().translate(moveDirX, 0);
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

    public List<GameObject> getGameObjectsInRange(GameObject gameObject, int range, int worldIndex){
        double rangeSquared = range*range;
        ArrayList<GameObject> foundObjects = new ArrayList<>();
        for (GameObject go : getGameObjects().values()){
            if(go.getWorldIndex() == worldIndex) {
                if (Vector2.DistanceSquared(go.getPosition(), gameObject.getPosition()) <= rangeSquared)
                    foundObjects.add(go);
            }
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

        go.setPosition( new Vector2(1360, 1360));
        user.setDeltaMovement(new Vector2(-(1360-400), -(1360-300)));
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


    public Vector2 getDefaultScale() {
        return defaultScale;
    }
}
