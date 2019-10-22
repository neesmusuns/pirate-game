package is.hi.hbv501.pirategame.pirategame.game.util;

import is.hi.hbv501.pirategame.pirategame.game.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Input {
    public static boolean GetKey(String key, String input){
        if(input == null)
            return false;
        if(input.equals(""))
            return false;
        
        String[] keyStrings = input.split(" ");
        Map<String, List<Boolean>> keyPresses = new HashMap<>();

        for (String keyString : keyStrings) {

            String[] keyValues = keyString.split("_");

            keyPresses.put(Util.Keys.get(Integer.parseInt(keyValues[0])), new ArrayList<Boolean>(){{
                add(true);
            }});
        }

        key = key.toUpperCase();

        return keyPresses.containsKey(key);


    }


}
