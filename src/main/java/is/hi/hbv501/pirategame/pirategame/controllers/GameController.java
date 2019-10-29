package is.hi.hbv501.pirategame.pirategame.controllers;

import is.hi.hbv501.pirategame.pirategame.services.GameService;
import is.hi.hbv501.pirategame.pirategame.game.datastructures.GameState;
import is.hi.hbv501.pirategame.pirategame.game.objects.User;
import is.hi.hbv501.pirategame.pirategame.services.UserService;
import org.hibernate.service.spi.InjectService;
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
    public String Game() {
        return "index";
    }

    @Autowired
    private UserService userService;

    @Autowired GameService gameService;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public @ResponseBody
    String CheckAdapter(HttpServletRequest request, HttpSession session) throws JSONException {
        boolean isLoggedIn = Boolean.parseBoolean(request.getParameter("IsLoggedIn"));
        boolean isQuitting = Boolean.parseBoolean(request.getParameter("IsQuitting"));

        String sessionID = session.getId();

        if (isQuitting && isLoggedIn) {
            gameService.removeUser(sessionID);
        } else if (!isLoggedIn) {
            String username = request.getParameter("Username");
            String password = request.getParameter("Password");

            if (userService.findUserByCredentials(username) != null) {
                User user = userService.findUserByCredentials(username);
                if (user.getPassword().equals(password)) {
                    user.setSessionID(sessionID);
                    gameService.addUser(sessionID, user);
                } else
                    return "false";
            } else {
                User user = new User(username, password);
                user.setSessionID(sessionID);
                gameService.addUser(sessionID, user);
            }

            return "true";
        } else if(gameService.getUsers().containsKey(sessionID)){
            gameService.addKeysToUser(request.getParameter("Keys"), sessionID);

            GameState currentState = gameService.getGameState();
            JSONObject gameState = new JSONObject();
            JSONArray gameObjectsArray = new JSONArray();
            currentState.getGameObjects().values().forEach(obj -> {
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
            JSONArray removedGameObjectIDs = new JSONArray();

            currentState.getRemovedGameObjectIDs().forEach(removedGameObjectIDs::put);

            gameState.put("gameObjects", gameObjectsArray);
            gameState.put("removedGameObjectIDs", removedGameObjectIDs);

            return gameState.toString();
        }

        return "Bad Request";
    }
}
