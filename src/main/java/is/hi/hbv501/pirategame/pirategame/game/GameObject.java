package is.hi.hbv501.pirategame.pirategame.game;

import is.hi.hbv501.pirategame.pirategame.game.datastructures.Vector2;
import is.hi.hbv501.pirategame.pirategame.game.util.Util;
import is.hi.hbv501.pirategame.pirategame.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

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

    private boolean hasCollider;

    @Autowired
    GameService gameService;

    public GameObject(){
        this.ID = InstanceHandler.GetNextID();
        position = new Vector2();
        sprite = "";
    }

    /**
     * Update is called once every frame.
     */
    public void Update(){
        isModified = false;

        //for (GameObject go : GetTilesInRange(1)) {
        //    if (CheckCollision(go)) {
        //        OnCollision(go);
        //    }
        //}
    }

    public Vector2 getPosition() {
        return position;
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

    private ArrayList<GameObject> GetTilesInRange(int range){
        int[] coords = Util.worldPosToWorldIndex(position);
        ArrayList<GameObject> gameObjects = new ArrayList<>();

        for(int i = Math.max(coords[0]-range, 0); i < Math.min(coords[0] + range, gameService.getWorldSize()); i++){
            for(int j = Math.max(coords[1]-range, 0); j < Math.min(coords[1] + range, gameService.getWorldSize()); j++){
                gameObjects.add(gameService.getGameState().getWorld().getTiles()[i][j]);
            }
        }
        return gameObjects;
    }

    public void GetCollider(){

    }

    public void OnCollision(GameObject collision){

    }

    private boolean CheckCollision(GameObject other){

        return false;
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
}
