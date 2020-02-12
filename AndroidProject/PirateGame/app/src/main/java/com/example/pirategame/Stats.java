package com.example.pirategame;

public class Stats {
    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getDrink() {
        return drink;
    }

    public void setDrink(int drink) {
        this.drink = drink;
    }

    public int getBreath() {
        return breath;
    }

    public void setBreath(int breath) {
        this.breath = breath;
    }

    public boolean isHasTreasure() {
        return hasTreasure;
    }

    public void setHasTreasure(boolean hasTreasure) {
        this.hasTreasure = hasTreasure;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public boolean isHasMap() {
        return hasMap;
    }

    public void setHasMap(boolean hasMap) {
        this.hasMap = hasMap;
    }

    public double getMarkerRot() {
        return markerRot;
    }

    public void setMarkerRot(double markerRot) {
        this.markerRot = markerRot;
    }

    private int health = 3;
    private int drink = 3;
    private int breath = 10;
    private boolean hasTreasure = false;
    private int money = 0;
    private boolean hasMap = false;
    private double markerRot = 0;

}
