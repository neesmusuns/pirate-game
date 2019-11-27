package is.hi.hbv501.pirategame.pirategame.game.objects;

import is.hi.hbv501.pirategame.pirategame.services.GameService;

import java.util.ArrayList;
import java.util.List;

public class Shop extends Tile {

    private List<ShopItem> shopItems = new ArrayList<ShopItem>();


    public void addShopItem(ShopItem item) {
        shopItems.add(item);
    }


    private Shop(GameService gameService) {
        super(gameService);
        setSprite("shop");
    }


    public Shop(GameService gameService, int worldIndex) {
        this(gameService);
        super.setWorldIndex(worldIndex);
    }
}
