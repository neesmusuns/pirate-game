package is.hi.hbv501.pirategame.pirategame.game.util;

import is.hi.hbv501.pirategame.pirategame.game.datastructures.Vector2;
import is.hi.hbv501.pirategame.pirategame.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

public class Util {
    public static Map<Integer, String> Keys = new HashMap<Integer, String>(){{
       put(87, "W");
       put(65, "A");
       put(83, "S");
       put(68, "D");
       put(69, "E");
       put(70, "F");
    }};

    public static int[] worldPosToWorldIndex(Vector2 worldPos){
        return (new int[]{(int) (Math.round(worldPos.getX())/40), (int) (Math.round(worldPos.getY())/40)});
    }
}
