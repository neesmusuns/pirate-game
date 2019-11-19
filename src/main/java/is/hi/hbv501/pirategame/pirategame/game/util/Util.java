package is.hi.hbv501.pirategame.pirategame.game.util;

import is.hi.hbv501.pirategame.pirategame.game.datastructures.Vector2;
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
        int[] coords = new int[]{((int)Math.round(worldPos.getX())/40), ((int)Math.round(worldPos.getY())/40)};
        return (coords);
    }

    public static boolean isOverlapping(Vector2 l1, Vector2 r1, Vector2 l2, Vector2 r2) {
        // If one rectangle is on left side of other
        if (l1.getX() > r2.getX() || l2.getX() > r1.getX()) {
            return false;
        }

        // If one rectangle is above other
        if (l1.getY() > r2.getY() || l2.getY() > r1.getY()) {
            return false;
        }

        return true;
    }
}
