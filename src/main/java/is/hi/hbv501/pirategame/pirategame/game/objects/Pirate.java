package is.hi.hbv501.pirategame.pirategame.game.objects;

import is.hi.hbv501.pirategame.pirategame.game.GameObject;
import is.hi.hbv501.pirategame.pirategame.game.datastructures.Vector2;
import is.hi.hbv501.pirategame.pirategame.services.GameService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Pirate extends GameObject {

    private double health = 2; // pirate's health

    private int drunkedness; // pirate's drunkedness level

    private int drinks = 1;

    private int hat; // hat/parrot accessories

    private int shirt; // upper clothing

    private int pants; // lower clothing

    private GameObject boat;

    private Shop shop;

    private boolean isInBoat; // whether or not in boat

    private boolean isDiving; // whether or not in the diving mode

    private boolean isInShop; // whether or not inside the shop

    private int moveSpeed = 2; // speed of movement

    private int damage; // damage inflicted in fight

    private boolean isHoldingTreasure;

    private boolean hasTreasure;

    private boolean hasMap;

    TreasureMarker treasureMarker;

    private Vector2 overWorldPosition;

    private double breath = 10;

    private int maxBreath = 10;



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

    public void enterShop(Shop s)
    {
        isInShop = true;
        shop = s;
    }

    public void exitShop() {
        isInShop = false;
        shop = null;
    }

    public void dive() {
        boolean diveSuccessful = true;

        if(diveSuccessful){
            if(isInBoat){
                isInBoat = false;
            }
            overWorldPosition = getPosition();
            isDiving = true;
        }
    }

    public void exitDive() {
        isDiving = false;
        if(boat != null)
            isInBoat = true;
        setBreath(10);
        setWorldIndex(0);
        setPosition(overWorldPosition);
    }

    /**
     * Consumes one drink and restores one heart
     */
    public void drink() {
        if(drinks > 0){
            drinks--;
            health = Math.ceil(health+0.01);
        }
    }

    public Vector2[] getCollider(){
        return new Vector2[]{
                Vector2.Sub(getPosition(), new Vector2(13, 13)),
                Vector2.Add(getPosition(), new Vector2(13, 13))
        };
    }

    public double getTreasureMarkerRot(){
        double angle = 0;

        Vector2 dir = Vector2.Sub(treasureMarker.getPosition(), getPosition());

        angle = Math.atan((-dir.getY())/dir.getX())*180/Math.PI;

        if(angle < 0){
            angle = 360 + angle;
        }

        return angle;
    }


    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
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

    public Vector2 getOverWorldPosition() {
        return overWorldPosition;
    }

    public void setOverWorldPosition(Vector2 overWorldPosition) {
        this.overWorldPosition = overWorldPosition;
    }

    public boolean hasTreasure() {
        return hasTreasure;
    }

    public void setHasTreasure(boolean hasTreasure) {
        this.hasTreasure = hasTreasure;
    }

    public double getBreath() {
        return breath;
    }

    public void setBreath(double breath) {
        this.breath = breath;
    }

    public int getMaxBreath() {
        return maxBreath;
    }

    public boolean isHoldingTreasure() {
        return isHoldingTreasure;
    }

    public void setHoldingTreasure(boolean holdingTreasure) {
        isHoldingTreasure = holdingTreasure;
    }

    public int getDrinks() {
        return drinks;
    }

    public Shop getShop() {
        return shop;
    }

    public boolean hasMap() {
        return hasMap;
    }

    public void setHasMap(boolean hasMap) {
        this.hasMap = hasMap;
    }

    public TreasureMarker getTreasureMarker() {
        return treasureMarker;
    }

    public void setTreasureMarker(TreasureMarker treasureMarker) {
        this.treasureMarker = treasureMarker;
    }
}
