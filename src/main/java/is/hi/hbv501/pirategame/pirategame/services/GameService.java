package is.hi.hbv501.pirategame.pirategame.services;

import com.sun.tools.javac.util.Pair;
import is.hi.hbv501.pirategame.pirategame.game.GameObject;
import is.hi.hbv501.pirategame.pirategame.game.datastructures.GameState;
import is.hi.hbv501.pirategame.pirategame.game.datastructures.Vector2;
import is.hi.hbv501.pirategame.pirategame.game.datastructures.World;
import is.hi.hbv501.pirategame.pirategame.game.objects.*;
import is.hi.hbv501.pirategame.pirategame.game.statics.Wearables;
import is.hi.hbv501.pirategame.pirategame.game.util.Input;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GameService {
    private Map<Long, GameObject> gameObjects = new HashMap<>();
    private int playerLimit = 10;
    private Stack<Integer> freeWorlds = new Stack<>();
    private World[] worlds = new World[playerLimit];

    private Map<String, User> users = new HashMap<>();
    private Queue<Pair<String, User>> userQueue = new LinkedList<>();
    private Queue<String> removedUserQueue = new LinkedList<>();
    private Vector2 defaultScale = new Vector2(2, 2);
    long last_time = System.nanoTime();

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

        //Set free world values to be 1, 2, 3, ..., playerLimit -1
        for(int i = 1; i < playerLimit; i++){
            freeWorlds.push(i);
        }

        gameState = new GameState(worlds, gameObjects);
        System.out.println("Finished generating world");
    }

    private void Update(){
        while(true) {
            long time = System.nanoTime();
            double deltaTime = ((time - last_time) / 1000000000d);


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
                TreasureMarker foundMarker = null;


                /*
                 * CHECK OBJECTS IN PROXIMITY
                 */

                //If near boat
                for (GameObject o : getGameObjectsInRange(obj, 60, u.getWorldIndex())) {
                    if(!obj.isInBoat()) {
                        if (o instanceof Boat) {
                            isNearBoat = true;
                            foundBoat = o;
                            obj.setTooltip("Press 'E' to enter boat");
                        }
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
                        if(!obj.isInShop())
                            obj.setTooltip("Press 'E' to enter shop");
                        isNearShop = true;
                        foundShop = (Shop) tile;
                        break;
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
                    } else if(isNearMarker){
                        removeGameObject(foundMarker.getID());
                        obj.dive();
                        int diveWorldIndex = requestDiveWorld();
                        u.setWorldIndex(diveWorldIndex);
                        obj.setWorldIndex(diveWorldIndex);
                        obj.setPosition(new Vector2(480, 0));
                    }
                    //If in boat and press E
                    else {
                        obj.exitBoat();
                    }
                }

                if(Input.GetKey("F", u.getKeyPresses())){
                    obj.drink();
                }

                /*
                 * General updates
                 */


                // DIVING
                if(obj.isDiving()) {
                    obj.setHasMap(false);
                    moveDirY -= 1;
                    double posY = obj.getPosition().getY();
                    if(posY < 0){
                        if(obj.isHoldingTreasure()){
                            obj.setHasTreasure(true);
                            freeWorlds.push(obj.getWorldIndex());
                            obj.exitDive();
                            u.setWorldIndex(0);
                        }
                        if(obj.getBreath() < obj.getMaxBreath() - 0.1){
                            if(obj.getBreath() < 0) obj.setBreath(0);
                            obj.setBreath(obj.getBreath() + deltaTime*0.5);
                        } else{
                            obj.setBreath(10);
                        }
                        if(posY < -20){
                            moveDirY -= 1;
                        }
                    } else {
                        obj.setBreath(obj.getBreath() - deltaTime*0.3);
                    }

                    if(obj.getBreath() <= 0){
                        //TODO: KILL!!!!
                        obj.setHealth(obj.getHealth() - deltaTime*0.5);
                        if(obj.getHealth() <= 0) {
                            obj.setHoldingTreasure(false);
                            freeWorlds.push(obj.getWorldIndex());
                            obj.exitDive();
                            u.setWorldIndex(0);
                        }
                    }
                }

                /*
                 * UPDATE MOVEMENT
                 */
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


            last_time = time;
        }
    }


    /**
     * Gets all non-tile game objects within a given range and world
     * @param gameObject The origin game object we are looking at
     * @param range The range in distance units
     * @param worldIndex The world index we are looking in
     * @return All game objects within the range and world
     */
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

    public void addClothing(Pirate p, String type){
        String sprite = "";
        if(type.equals("headwear"))
            sprite = Wearables.getHeadwear(p.getHat());
        if(type.equals("shirt"))
            sprite = Wearables.getShirt(p.getShirt());
        if(type.equals("pants"))
            sprite = Wearables.getPants(p.getPants());

        addGameObject(new Clothing(this, p, sprite));
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

        if(user.getUsername().equals("irma")){
            ((Pirate) go).setHat(2);
            ((Pirate) go).setShirt(1);
            addClothing(((Pirate) go), "headwear");
            addClothing(((Pirate) go), "shirt");
        }
        user.setPlayerObjectID(go.getID());

        go.setPosition( new     Vector2(32*40, 15*40));
        user.setDeltaMovement(new Vector2(-(32*40-400), -(15*40-300)));
        users.put(sessionID, user);
    }

    public void requestRemoveUser(String sessionID){
        removedUserQueue.add(sessionID);
    }

    private void removeUser(String sessionID){
        User removedUser = users.remove(sessionID);
        userService.insertUser(removedUser);
        removeGameObject(removedUser.getPlayerObjectID());
    }

    public void generateTreasureMarker(User u){
        Pirate p = (Pirate) gameObjects.get(u.getPlayerObjectID());
        boolean hasFoundSuitableTile = false;
        int x = 0;
        int y = 0;
        while(!hasFoundSuitableTile){
            x = (int) (Math.random() * (worlds[0].getWidth() - 1));
            y = (int) (Math.random() * (worlds[0].getHeight() - 1));

            if(!worlds[0].getTiles()[x][y].isLand()){
                hasFoundSuitableTile = true;
            }
        }

        GameObject t = addGameObject(new TreasureMarker(this));
        t.setPosition(new Vector2(x*40, y*40));

        p.setTreasureMarker((TreasureMarker) t);
        p.setHasMap(true);

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

    /**
     * Generates a diving world at an unoccupied world-index and returns the world's index
     */
    private int requestDiveWorld(){
        int worldIndex = freeWorlds.pop();
        clearWorld(worldIndex);
        worlds[worldIndex] = new World(this);
        worlds[worldIndex].generateWorld("divingworld.txt", worldIndex);
        GameObject treasure = addGameObject(new Treasure(this, worldIndex));
        treasure.setPosition(new Vector2(840, 260));

        return worldIndex;
    }

    /**
     * Clears all gameObjects tied to a world index
     * @param worldIndex the world index to be cleared
     */
    public void clearWorld(int worldIndex){
        Stack<Long> objectsToRemove = new Stack<>();
        gameObjects.values().forEach(gameObject -> {
            if(gameObject.getWorldIndex() == worldIndex){
                objectsToRemove.push(gameObject.getID());
            }
        });

        while(!objectsToRemove.isEmpty()){
            removeGameObject(objectsToRemove.pop());
        }
    }


    public Vector2 getDefaultScale() {
        return defaultScale;
    }

    public void addBoat() {
        GameObject boat = addGameObject(new Boat(this));
        boat.setPosition( new Vector2(-20+39*40, -20+15*40));
    }
}
