package is.hi.hbv501.pirategame.pirategame.controllers;

import is.hi.hbv501.pirategame.pirategame.services.GameService;
import is.hi.hbv501.pirategame.pirategame.game.datastructures.GameState;
import is.hi.hbv501.pirategame.pirategame.game.objects.User;
import is.hi.hbv501.pirategame.pirategame.services.UserService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class GameController {
    @RequestMapping("/")
    public String Game(){
        return "index";
    }

    @Autowired
    private
    UserService userService;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public @ResponseBody
    String CheckAdapter(HttpServletRequest request, HttpSession session) throws JSONException {
        boolean isLoggedIn = Boolean.parseBoolean(request.getParameter("IsLoggedIn"));

        String sessionID = session.getId();

        if (!isLoggedIn) {
            String username = request.getParameter("Username");
            String password = request.getParameter("Password");

            if (userService.findUserByCredentials(username) != null) {
                User user = userService.findUserByCredentials(username);
                user.setSessionID(sessionID);
                GameService.gm.addUser(sessionID, new User(username, password));
            } else{
                User user = new User(username, password);
                user.setSessionID(sessionID);
                GameService.gm.addUser(sessionID, new User(username, password));
            }

            return "true";
        } else {
            GameService.gm.addKeysToUser(request.getParameter("Keys"), sessionID);

            GameState currentState = GameService.gm.getGameState();
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
}
