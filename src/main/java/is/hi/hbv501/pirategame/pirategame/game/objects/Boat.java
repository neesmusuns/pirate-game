package is.hi.hbv501.pirategame.pirategame.game.objects;

import is.hi.hbv501.pirategame.pirategame.game.GameObject;
import is.hi.hbv501.pirategame.pirategame.game.datastructures.Vector2;
import is.hi.hbv501.pirategame.pirategame.services.GameService;

public class Boat extends GameObject {
    int speed = 3;

    public Boat(GameService gameService){
        super(gameService);
        super.setSprite("boat");
        setHasCollider(true);
        addIgnoreLayer("water");
    }

    public Vector2[] getCollider(){
        return new Vector2[]{
                Vector2.Sub(getPosition(), new Vector2(15, 15)),
                Vector2.Add(getPosition(), new Vector2(15, 15))
        };
    }
}
