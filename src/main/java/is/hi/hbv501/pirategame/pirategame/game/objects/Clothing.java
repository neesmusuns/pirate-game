package is.hi.hbv501.pirategame.pirategame.game.objects;

import is.hi.hbv501.pirategame.pirategame.game.GameObject;
import is.hi.hbv501.pirategame.pirategame.services.GameService;

public class Clothing extends GameObject{
    private GameObject target;


    public Clothing(GameService gameService, GameObject target, String sprite){
        this(gameService, 0);
        this.setSprite(sprite);
        this.target = target;
        this.setZIndex(3);
    }

    private Clothing(GameService gameService, int worldIndex) {
        super(gameService, worldIndex);
    }

    public void Update(){
        super.Update();

        setWorldIndex(target.getWorldIndex());
        setPosition(target.getPosition());
    }
}
