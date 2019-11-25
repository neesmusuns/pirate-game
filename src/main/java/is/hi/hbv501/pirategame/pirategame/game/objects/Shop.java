package is.hi.hbv501.pirategame.pirategame.game.objects;

import is.hi.hbv501.pirategame.pirategame.services.GameService;

public class Shop extends Tile {

    private Shop(GameService gameService) {
        super(gameService);
        setSprite("shop");
    }


    public Shop(GameService gameService, int worldIndex) {
        this(gameService);
        super.setWorldIndex(worldIndex);
    }
}
