package is.hi.hbv501.pirategame.pirategame.controllers;

import is.hi.hbv501.pirategame.pirategame.game.GameManager;
import is.hi.hbv501.pirategame.pirategame.game.datastructures.GameState;
import is.hi.hbv501.pirategame.pirategame.game.objects.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;

@Controller
public class GameController {
    @RequestMapping("/")
    public String Game(){
        return "index";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public @ResponseBody
    String CheckAdapter(HttpServletRequest request, HttpSession session) throws JSONException {
        String sessionID = session.getId();
        if(!GameManager.gm.getUsers().containsKey(sessionID)){
           GameManager.gm.addUser(sessionID, new User(sessionID));
        }

        GameManager.gm.addKeysToUser(request.getParameter("Keys"), sessionID);

        GameState currentState = GameManager.gm.getGameState();
        JSONObject gameState = new JSONObject();
        JSONArray gameObjectsArray = new JSONArray();
        currentState.getGameObjects().forEach(obj -> {
            try {
                JSONObject gameObject = new JSONObject();
                gameObject.put("id", obj.getID());
                gameObject.put("sprite", obj.getSprite());
                gameObject.put("x", obj.getPosition().getX());
                gameObject.put("y", obj.getPosition().getY());
                gameObject.put("scaleX", obj.getScale().getX());
                gameObject.put("scaleY", obj.getScale().getY());

                gameObjectsArray.put(gameObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        gameState.put("gameObjects", gameObjectsArray);

        return gameState.toString();
    }
}
