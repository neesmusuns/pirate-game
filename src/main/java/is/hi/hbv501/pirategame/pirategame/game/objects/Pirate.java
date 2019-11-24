package is.hi.hbv501.pirategame.pirategame.game.objects;

import is.hi.hbv501.pirategame.pirategame.game.GameObject;
import is.hi.hbv501.pirategame.pirategame.game.datastructures.Vector2;
import is.hi.hbv501.pirategame.pirategame.services.GameService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Pirate extends GameObject {

    private int health = 2; // pirate's health

    private int drunkedness; // pirate's drunkedness level

    private int hat; // hat/parrot accessories

    private int shirt; // upper clothing

    private int pants; // lower clothing

    private GameObject boat;

    private boolean isInBoat; // whether or not in boat

    private boolean isDiving; // whether or not in the diving mode

    private boolean isInShop; // whether or not inside the shop

    private int moveSpeed = 2; // speed of movement

    private int damage; // damage inflicted in fight

    private int worldIndex; // the current world, main or different diving worlds

    public Pirate(GameService gameService) {
        super(gameService);
        super.setSprite("pirate");
        setZIndex(2);
    }

    public void Start(){
        super.Start();
        setHasCollider(true);
        addIgnoreLayer("land");
    }

    public void Update(){
        setHasCollider(!isInBoat);
    }

    public void move(Vector2 dir) {

    }

    public void moveRelativeToBoat(){
        if(isInBoat)
            setPosition(new Vector2(boat.getPosition().getX(), boat.getPosition().getY() - 20));

    }

    public void attack() {

    }

    public void interactWithBoat() {

    }

    public void enterShop(Shop s) {
        System.out.println("Bought a boat");
    }

    public void exitShop() {

    }

    public void dive() {

    }

    public void exitDive() {

    }

    public Vector2[] getCollider(){
        return new Vector2[]{
                Vector2.Sub(getPosition(), new Vector2(13, 13)),
                Vector2.Add(getPosition(), new Vector2(13, 13))
        };
    }


    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getDrunkedness() {
        return drunkedness;
    }

    public void setDrunkedness(int drunkedness) {
        this.drunkedness = drunkedness;
    }

    public int getHat() {
        return hat;
    }

    public void setHat(int hat) {
        this.hat = hat;
    }

    public int getShirt() {
        return shirt;
    }

    public void setShirt(int shirt) {
        this.shirt = shirt;
    }

    public int getPants() {
        return pants;
    }

    public void setPants(int pants) {
        this.pants = pants;
    }

    public boolean isInBoat() {
        return isInBoat;
    }

    public void setInBoat(boolean inBoat) {
        isInBoat = inBoat;
    }

    public boolean isDiving() {
        return isDiving;
    }

    public void setDiving(boolean diving) {
        isDiving = diving;
    }

    public boolean isInShop() {
        return isInShop;
    }

    public void setInShop(boolean inShop) {
        isInShop = inShop;
    }

    public int getMoveSpeed() {
        return moveSpeed;
    }

    public void setMoveSpeed(int moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getWorldIndex() {
        return worldIndex;
    }

    public void setWorldIndex(int worldIndex) {
        this.worldIndex = worldIndex;
    }

    public void enterBoat(GameObject o) {
        setBoat(o);
        setInBoat(true);
    }

    public GameObject getBoat() {
        return boat;
    }

    public void setBoat(GameObject boat) {
        this.boat = boat;
    }

    public void exitBoat() {
        if(boat != null) {
            Map<Double, GameObject> tileDistances = new HashMap<>();

            for (GameObject tile : boat.GetTilesInRange(1)) {
                if (((Tile) tile).isLand())
                    tileDistances.put(Vector2.DistanceSquared(boat.getPosition(), tile.getPosition()),
                            tile);
            }

            if (!tileDistances.isEmpty()) {
                Double[] keys = tileDistances.keySet().toArray(new Double[tileDistances.size()]);
                Arrays.sort(keys);
                setPosition(tileDistances.get(keys[0]).getPosition());
                setInBoat(false);
            }
        }
    }
}
