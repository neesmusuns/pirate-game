package is.hi.hbv501.pirategame.pirategame.game.objects;

import is.hi.hbv501.pirategame.pirategame.game.GameObject;
import is.hi.hbv501.pirategame.pirategame.services.GameService;

public class Boat extends GameObject {
    int speed = 3;

    public Boat(GameService gameService){
        super(gameService);
        super.setSprite("boat");
    }
}
