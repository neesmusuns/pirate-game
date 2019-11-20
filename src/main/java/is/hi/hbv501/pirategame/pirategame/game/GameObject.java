package is.hi.hbv501.pirategame.pirategame.game;

import is.hi.hbv501.pirategame.pirategame.game.datastructures.Vector2;
import is.hi.hbv501.pirategame.pirategame.game.util.Util;
import is.hi.hbv501.pirategame.pirategame.services.GameService;

import java.util.ArrayList;
import java.util.Collection;

public class GameObject {
    /**
     * The game object's position in 2D space.
     */
    private Vector2 position = new Vector2();

    /**
     * The scale of the game object as a Vector2
     */
    private Vector2 scale = new Vector2(1, 1);

    /**
     * The path to the game object's sprite
     */
    private String sprite;

    /**
     * The global ID used to refer to the game object
     */
    private long ID;

    /**
     * Is set to true when game object is modified
     */
    private boolean isModified;

    /**
     * Is set to true if a collider is to be simulated for the game object
     */
    private boolean hasCollider;

    private GameService gameService;

    public GameObject(GameService gameService){
        this.gameService = gameService;
        this.ID = InstanceHandler.GetNextID();
        position = new Vector2();
        sprite = "";
        this.scale = gameService.getDefaultScale();
        Start();
    }

    /**
     * Start is called on object initialization
     */
    public void Start(){

    }

    /**
     * Update is called once every frame.
     */
    public void Update(){
        isModified = false;
    }

    public Vector2 getPosition() {
        return position;
    }

    public boolean translate(int x, int y){
        Vector2 translation = new Vector2(x, y);
        if(hasCollider) {
            /*
             * TILE COLLISIONS
             */
            for (GameObject go : GetTilesInRange(1)) {
                if (CheckCollision(go, new Vector2())) {
                    Unstick();
                    OnCollision(go);
                    return false;
                }
                if (CheckCollision(go, translation)) {
                    OnCollision(go);
                    return false;
                }
            }

            /*
             * OTHER COLLISIONS
             */
            Collection<GameObject> gameObjects = gameService.getGameObjects().values();
            gameObjects.remove(this);
            for(GameObject go : gameObjects){
                if (CheckCollision(go, new Vector2())) {
                    Unstick();
                    OnCollision(go);
                    return false;
                }
                if(go.getHasCollider()){
                    if(CheckCollision(go, translation)){
                        OnCollision(go);
                        return false;
                    }
                }
            }
        }

        this.position.add(translation);
        return true;
    }

    public void setPosition(Vector2 position) {
        isModified = true;
        this.position = position;
    }

    public String getSprite() {
        return sprite;
    }

    public void setSprite(String sprite) {
        isModified = true;
        this.sprite = sprite;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        isModified = true;
        this.ID = ID;
    }

    public Vector2 getScale() {
        return scale;
    }

    public void setScale(Vector2 scale) {
        isModified = true;
        this.scale = scale;
    }

    /**
     * Gets all the tiles within a given range of the game object
     * @param range in tiles
     * @return a list of tiles in range of the game object
     */
    public ArrayList<GameObject> GetTilesInRange(int range){
        int[] coords = Util.worldPosToWorldIndex(position);
        ArrayList<GameObject> gameObjects = new ArrayList<>();

        for(int i = Math.max(coords[0]-range, 0); i <= Math.min(coords[0] + range, gameService.getWorldSize()); i++){
            for(int j = Math.max(coords[1]-range, 0); j <= Math.min(coords[1] + range, gameService.getWorldSize()); j++){
                gameObjects.add(gameService.getGameState().getWorld().getTiles()[i][j]);
            }
        }
        return gameObjects;
    }

    /**
     * Gets the collider of the current game object
     * @return Vector2 representing half width and height of collider
     */
    public Vector2[] getCollider(){
        return new Vector2[]{
                Vector2.Sub(position, new Vector2(20, 20)),
                Vector2.Add(position, new Vector2(20, 20))
        };
    }

    public void OnCollision(GameObject collision){

    }

    /**
     * Checks collision with a game object
     * @param other the potential collision
     * @param dir the current movement direction of the game object
     * @return true for a collision
     */
    private boolean CheckCollision(GameObject other, Vector2 dir){
        if(!other.getHasCollider()) return false;

        Vector2[] otherCollider = other.getCollider();
        Vector2[] collider = getCollider();

        return Util.isOverlapping(otherCollider[0], otherCollider[1],
                                  Vector2.Add(collider[0], dir), Vector2.Add(collider[1],dir));
    }

    /**
     * Unsticks the game object in the case that it gets stuck in a collision
     */
    private void Unstick() {
        int limit = 1000;
        boolean isUnstuck = false;

        for(int iter = 1; iter < limit; iter++){
            for (int i = -iter; i < iter; i++){
                for(int j = -iter; j < iter; j++){
                    isUnstuck = stuckTranslate(i, j);
                }
                if(isUnstuck) break;
            }
            if(isUnstuck) break;
        }
    }

    public boolean stuckTranslate(int x, int y){
        Vector2 translation = new Vector2(x, y);
        if(hasCollider) {
            /*
             * TILE COLLISIONS
             */
            for (GameObject go : GetTilesInRange(1)) {
                if (CheckCollision(go, translation)) {
                    OnCollision(go);
                    return false;
                }
            }

            /*
             * OTHER COLLISIONS
             */
            Collection<GameObject> gameObjects = gameService.getGameObjects().values();
            gameObjects.remove(this);
            for(GameObject go : gameObjects){
                if(go.getHasCollider()){
                    if(CheckCollision(go, translation)){
                        OnCollision(go);
                        return false;
                    }
                }
            }
        }

        this.position.add(translation);
        return true;
    }

    @Override
    public String toString() {
        return String.format("GameObject [id=%s, position=%s, scale=%s, sprite=%s]", ID, position, scale, sprite);
    }

    public boolean isModified() {
        return isModified;
    }

    public void setModified(boolean modified) {
        isModified = modified;
    }

    public void setHasCollider(boolean hasCollider) {
        this.hasCollider = hasCollider;
    }

    public boolean getHasCollider() {
        return hasCollider;
    }

    public void setService(GameService gameService) {
        this.gameService = gameService;
    }
}
