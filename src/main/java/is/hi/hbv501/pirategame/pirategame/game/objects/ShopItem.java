package is.hi.hbv501.pirategame.pirategame.game.objects;

public class ShopItem {
    private String name;
    private int price;
    private String sprite;

    public ShopItem(String n, int p, String s) {
        name = n;
        price = p;
        sprite = s;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getSprite() {
        return sprite;
    }
}
