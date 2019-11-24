package is.hi.hbv501.pirategame.pirategame.game.objects;

import is.hi.hbv501.pirategame.pirategame.services.GameService;

public class Shop extends Tile {

    public Shop(GameService gameService) {
        super(gameService);
        setSprite("shop");
    }


    public Shop(GameService gameService, int worldIndex) {
        super(gameService);
        super.setWorldIndex(worldIndex);
    }
}
