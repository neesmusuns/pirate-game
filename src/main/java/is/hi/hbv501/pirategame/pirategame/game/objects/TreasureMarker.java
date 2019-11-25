package is.hi.hbv501.pirategame.pirategame.game.objects;

import is.hi.hbv501.pirategame.pirategame.game.GameObject;
import is.hi.hbv501.pirategame.pirategame.game.datastructures.World;
import is.hi.hbv501.pirategame.pirategame.services.GameService;

/**
 * Class defining a treasure marker. <br>
 * A treasure marker marks a treasure in the game world, points to a dive world, and contains
 * treasure values.
 */
public class TreasureMarker extends GameObject {

    /**
     * A string type defining the treasure type. 'Gold' and 'Item' are the two options
     */
    String treasureType = "gold";

    private World diveWorld;

    public TreasureMarker(GameService gameService) {
        super(gameService);
        setRendered(false);
        diveWorld = new World(gameService);
    }


}
