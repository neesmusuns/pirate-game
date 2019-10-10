package is.hi.hbv501.pirategame.pirategame.game;

public class InstanceHandler {

    private static int IDCounter = 0;

    /**
     * Request the next available instance ID
     */
    public static int GetNextID(){
        return IDCounter++;
    }
}
