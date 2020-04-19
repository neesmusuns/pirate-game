package is.hi.hbv501.pirategame.pirategame.game.objects;

import is.hi.hbv501.pirategame.pirategame.game.GameObject;
import is.hi.hbv501.pirategame.pirategame.game.datastructures.Vector2;
import is.hi.hbv501.pirategame.pirategame.services.GameService;

public class Crab extends GameObject {
    private Vector2 movedPos = new Vector2(0, 0);
    private boolean movingRight = true;
    private double speed = 0.3f;

    public Crab(GameService gameService, int worldIndex) {
        super(gameService, worldIndex);

        super.setSprite("crab");
    }

    public Crab(GameService gameService) {
        super(gameService);
        super.setSprite("crab");
        super.setZIndex(1);

        speed += 0.05f - 0.1f*Math.random();
    }

    public void Update(){
        super.Update();

        Vector2 moveDir = new Vector2(movingRight ? speed : -speed, 0);

        if(Vector2.Add(movedPos, moveDir).getX() > 60)
            movingRight = false;

        if(Vector2.Add(movedPos, moveDir).getX() < 0)
            movingRight = true;

        movedPos.add(moveDir);
        translate(moveDir.getX(), moveDir.getY());
    }
}
