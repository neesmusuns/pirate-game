package is.hi.hbv501.pirategame.pirategame.game.objects;

import is.hi.hbv501.pirategame.pirategame.game.GameObject;
import is.hi.hbv501.pirategame.pirategame.game.datastructures.Vector2;

public class Pirate extends GameObject {

    private int health; // pirate's health

    private int drunkedness; // pirate's drunkedness level

    private int hat; // hat/parrot accessories

    private int shirt; // upper clothing

    private int pants; // lower clothing

    private boolean isInBoat; // whether or not in boat

    private boolean isDiving; // whether or not in the diving mode

    private boolean isInShop; // whether or not inside the shop

    private int moveSpeed; // speed of movement

    private int damage; // damage inflicted in fight

    private int worldIndex; // the current world, main or different diving worlds


    public void move(Vector2 dir) {

    }

    public void attack() {

    }

    public void interactWithBoat() {

    }

    public void enterShop() {

    }

    public void exitShop() {

    }

    public void dive() {

    }

    public void exitDive() {

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

}
