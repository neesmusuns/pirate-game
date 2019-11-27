package is.hi.hbv501.pirategame.pirategame.game.objects;

import is.hi.hbv501.pirategame.pirategame.game.GameObject;
import is.hi.hbv501.pirategame.pirategame.services.GameService;

import java.util.List;

public class Treasure extends GameObject {
    public Treasure(GameService gameService, int worldIndex) {
        super(gameService, worldIndex);
        this.setSprite("treasure");
        this.setZIndex(1);
    }

    public void Update(){
        super.Update();

        List<GameObject> gameObjects = super.gameService.getGameObjectsInRange(
                this, 30, getWorldIndex()
        );

        gameObjects.forEach(gameObject -> {
            if(gameObject instanceof Pirate){
                setPosition(gameObject.getPosition());
                ((Pirate) gameObject).setHoldingTreasure(true);
            }
        });

    }
}
