package is.hi.hbv501.pirategame.pirategame.controllers;

import is.hi.hbv501.pirategame.pirategame.game.GameObject;
import is.hi.hbv501.pirategame.pirategame.game.datastructures.World;
import is.hi.hbv501.pirategame.pirategame.game.objects.Tile;
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
    public GameController(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }

    @RequestMapping("/")
    public String Game() {
        return "index";
    }

    private final UserService userService;

    private final GameService gameService;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public @ResponseBody
    String CheckAdapter(HttpServletRequest request, HttpSession session) throws JSONException {
        boolean isLoggedIn = Boolean.parseBoolean(request.getParameter("IsLoggedIn"));
        boolean isQuitting = Boolean.parseBoolean(request.getParameter("IsQuitting"));
        GameState currentState = gameService.getGameState();

        String sessionID = session.getId();

        /*
         * QUITTING
         */
        if (isQuitting && isLoggedIn) {
            gameService.requestRemoveUser(sessionID);

        /*
         * LOGGING IN
         */
        } else if (!isLoggedIn) {
            String username = request.getParameter("Username");
            String password = request.getParameter("Password");

            //If user exists in database
            if (userService.findUserByCredentials(username) != null) {
                User user = userService.findUserByCredentials(username);

                //Check password consistency & fetch / deny user
                if (user.getPassword().equals(password)) {
                    user.setSessionID(sessionID);
                    gameService.enqueueUser(sessionID, user);
                } else {
                    JSONObject response = new JSONObject();
                    response.put("IsLoggedIn", "false");
                    return response.toString();
                }
            }
            //If user doesn't exist, create a new one
            else {
                User user = new User(username, password);
                user.setSessionID(sessionID);
                gameService.enqueueUser(sessionID, user);
            }

            //Package up the World and send to user
            JSONObject response = new JSONObject();
            JSONArray gameObjectsArray = new JSONArray();
            World world = currentState.getWorld();
            Tile[][] tiles = world.getTiles();
            for(int i = 0; i < world.getWidth(); i++) {
                for (int j = 0; j < world.getHeight(); j++) {
                    GameObject obj = tiles[i][j];
                    putGameObject(gameObjectsArray, obj);
                }
            }

            JSONObject posShift = new JSONObject();
            posShift.put("x", 0);
            posShift.put("y", 0);

            response.put("IsLoggedIn", "true");
            response.put("gameObjects", gameObjectsArray);
            response.put("removedGameObjectIDs", new JSONArray());
            response.put("posShift", posShift);
            return response.toString();
        }
        /*
         * PLAYING
         */
        else if(gameService.getUsers().containsKey(sessionID)){
            //Collect user request's input and assign it to user object
            gameService.addKeysToUser(request.getParameter("Keys"), sessionID);

            //Send current game state to user
            JSONObject gameState = new JSONObject();
            JSONArray gameObjectsArray = new JSONArray();
            currentState.getGameObjects().values().forEach(obj -> putGameObject(gameObjectsArray, obj));
            JSONArray removedGameObjectIDs = new JSONArray();

            currentState.getRemovedGameObjectIDs().forEach(removedGameObjectIDs::put);

            JSONObject posShift = new JSONObject();
            posShift.put("x", gameService.getUsers().get(sessionID).getDeltaMovement().getX());
            posShift.put("y", gameService.getUsers().get(sessionID).getDeltaMovement().getY());

            gameState.put("gameObjects", gameObjectsArray);
            gameState.put("removedGameObjectIDs", removedGameObjectIDs);
            gameState.put("posShift", posShift);


            return gameState.toString();
        }

        //Returned when request is incorrectly formatted or contains invalid data
        return "Bad Request";
    }

    private void putGameObject(JSONArray gameObjectsArray, GameObject obj) {
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
    }
}
