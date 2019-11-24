package is.hi.hbv501.pirategame.pirategame.game.objects;

import is.hi.hbv501.pirategame.pirategame.game.datastructures.Vector2;

public class User {
    /**
     * Username of user
     */
    private String username;

    /**
     * Password of user
     */
    private String password;

    /**
     * The current SessionID of the user, set upon client connection
     */
    private String SessionID;

    /**
     * A list of key presses. Can be parsed as input
     */
    private String keyPresses;

    /**
     * ID reference to the user's player object
     */
    private long PlayerObjectID;

    private int money;
    private int headwear;
    private int shirt;
    private int pants;
    private int boat;
    private int drinks = 1;
    private int treasure;
    private int worldIndex = 0;
    private boolean hasChangedWorld;
    private int previousWorldIndex = 0;

    private Vector2 deltaMovement = new Vector2();

    public User(String username, String password){
        this.username = username;
        this.password = password;
    }

    public User(){

    }

    public String getSessionID() {
        return SessionID;
    }

    public void setSessionID(String sessionID) {
        SessionID = sessionID;
    }

    public String getKeyPresses() {
        return keyPresses;
    }

    public void setKeyPresses(String keys){
        this.keyPresses = keys;
    }

    public void clearKeyPresses() {
        this.keyPresses = "";
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getPlayerObjectID() {
        return PlayerObjectID;
    }

    public void setPlayerObjectID(long playerObjectID) {
        PlayerObjectID = playerObjectID;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getHeadwear() {
        return headwear;
    }

    public void setHeadwear(int headwear) {
        this.headwear = headwear;
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

    public int getBoat() {
        return boat;
    }

    public void setBoat(int boat) {
        this.boat = boat;
    }

    public int getDrinks() {
        return drinks;
    }

    public void setDrinks(int drinks) {
        this.drinks = drinks;
    }

    public int getTreasure() {
        return treasure;
    }

    public void setTreasure(int treasure) {
        this.treasure = treasure;
    }

    public int getWorldIndex() {
        return worldIndex;
    }

    public void setWorldIndex(int worldIndex) {
        previousWorldIndex = worldIndex;
        this.worldIndex = worldIndex;
        hasChangedWorld = true;
    }

    public Vector2 getDeltaMovement() {
        return deltaMovement;
    }

    public void setDeltaMovement(Vector2 deltaMovement) {
        this.deltaMovement = deltaMovement;
    }

    public void setHasChangedWorld(boolean hasChangedWorld) {
        this.hasChangedWorld = hasChangedWorld;
    }

    public boolean hasChangedWorld() {
        return hasChangedWorld;
    }

    public int getPreviousWorldIndex() {
        return previousWorldIndex;
    }

    public void setPreviousWorldIndex(int previousWorldIndex) {
        this.previousWorldIndex = previousWorldIndex;
    }
}
