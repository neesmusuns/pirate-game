package is.hi.hbv501.pirategame.pirategame.controllers;

import is.hi.hbv501.pirategame.pirategame.game.GameObject;
import is.hi.hbv501.pirategame.pirategame.game.datastructures.GameState;
import is.hi.hbv501.pirategame.pirategame.game.datastructures.World;
import is.hi.hbv501.pirategame.pirategame.game.objects.Pirate;
import is.hi.hbv501.pirategame.pirategame.game.objects.ShopItem;
import is.hi.hbv501.pirategame.pirategame.game.objects.Tile;
import is.hi.hbv501.pirategame.pirategame.game.objects.User;
import is.hi.hbv501.pirategame.pirategame.game.statics.ItemPrices;
import is.hi.hbv501.pirategame.pirategame.services.GameService;
import is.hi.hbv501.pirategame.pirategame.services.UserService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
        boolean hasExitedShop = Boolean.parseBoolean(request.getParameter("HasExited"));
        boolean isBuying = Boolean.parseBoolean(request.getParameter("IsBuying"));
        boolean isSelling = Boolean.parseBoolean(request.getParameter("IsSelling"));

        GameState currentState = gameService.getGameState();

        String sessionID = session.getId();

        /*
         * QUITTING
         */
        if (isQuitting && isLoggedIn) {
            gameService.requestRemoveUser(sessionID);
        }else if(isQuitting) {
            //Do nothing
        }
        /*
         * SELLING
         */
        else if(isSelling){
            User user = gameService.getUsers().get(sessionID);
            Pirate p = ((Pirate) gameService.getGameObjects().get(user.getPlayerObjectID()));
            if(p.hasTreasure()){
                user.setMoney(user.getMoney() + 20);
                p.setHasTreasure(false);
            }
        /*
         * BUYING
         */
        } else if(isBuying){
            String boughtItem = request.getParameter("Item");
            User user = gameService.getUsers().get(sessionID);
            Pirate p = ((Pirate) gameService.getGameObjects().get(user.getPlayerObjectID()));

            if(user.getMoney() >= ItemPrices.prices.get(boughtItem)){
                user.setMoney(user.getMoney() - ItemPrices.prices.get(boughtItem));
            }

            if(boughtItem.contains("boat")){
                gameService.addBoat();
            } else if(boughtItem.contains("map")){
                gameService.generateTreasureMarker(user);
            } else if(boughtItem.contains("bottle")){
                p.setDrinks(p.getDrinks() + 1);
            } else if(boughtItem.contains("parrot")){
                p.setHat(1);
                gameService.addClothing(p, "headwear");
            }
        }
        /*
         * EXITING SHOP
         */
        else if(hasExitedShop) {
            User user = gameService.getUsers().get(sessionID);
            Pirate p = ((Pirate) gameService.getGameObjects().get(user.getPlayerObjectID()));
            p.exitShop();
        }

        /*
         * LOGGING IN
         */
        else if (!isLoggedIn) {
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
            JSONArray tempRemovedGameObjectIDs = new JSONArray();
            World world = currentState.getWorld(0);
            putWorld(gameObjectsArray, world);

            if(gameService.getUsers().containsKey(sessionID)) {
                JSONObject stats = getUserStats(sessionID);
                response.put("stats", stats);
                response.put("playerID", gameService.getUsers().get(sessionID).getPlayerObjectID());
            }

            JSONObject posShift = new JSONObject();
            posShift.put("x", 0);
            posShift.put("y", 0);

            response.put("IsLoggedIn", "true");
            response.put("gameObjects", gameObjectsArray);
            response.put("tempRemovedGameObjectIDs", tempRemovedGameObjectIDs);
            response.put("removedGameObjectIDs", new JSONArray());
            response.put("posShift", posShift);
            return response.toString();
        }
        /*
         * PLAYING
         */
        else if(gameService.getUsers().containsKey(sessionID)){
            User user = gameService.getUsers().get(sessionID);

            //Collect user request's input and assign it to user object
            gameService.addKeysToUser(request.getParameter("Keys"), sessionID);

            //Send current game state to user
            JSONObject gameState = new JSONObject();
            JSONArray gameObjectsArray = new JSONArray();
            JSONArray removedGameObjectIDs = new JSONArray();
            JSONArray tempRemovedGameObjectIDs = new JSONArray();
            currentState.getGameObjects().values().forEach(obj -> {
                if(obj.getWorldIndex() == user.getWorldIndex())
                    putGameObject(gameObjectsArray, obj);
                else if (user.hasChangedWorld())
                    tempRemovedGameObjectIDs.put(obj.getID());
            });


            currentState.getRemovedGameObjectIDs().forEach(removedGameObjectIDs::put);

            boolean changedWorld = false;
            if(user.hasChangedWorld()) {
                System.out.println("Switched world");
                for(Tile[] tiles : currentState.getWorld(user.getPreviousWorldIndex()).getTiles()){
                    for (Tile tile : tiles) {
                        tempRemovedGameObjectIDs.put(tile.getID());
                    }
                }
                World world = currentState.getWorld(user.getWorldIndex());
                putWorld(gameObjectsArray, world);
                changedWorld = true;
                user.setHasChangedWorld(false);
            }

            JSONObject stats = getUserStats(sessionID);

            JSONObject posShift = new JSONObject();
            posShift.put("x", gameService.getUsers().get(sessionID).getDeltaMovement().getX());
            posShift.put("y", gameService.getUsers().get(sessionID).getDeltaMovement().getY());

            Pirate p = ((Pirate) gameService.getGameObjects().get(user.getPlayerObjectID()));
            if(p.isInShop()){
                JSONArray shopItemsArray = new JSONArray();
                p.getShop().getShopItems().forEach(item -> putShopItem(shopItemsArray, item));
                gameState.put("shopItems", shopItemsArray);
            }


            gameState.put("changedWorld", changedWorld);
            gameState.put("playerID", gameService.getUsers().get(sessionID).getPlayerObjectID());
            gameState.put("stats", stats);
            gameState.put("tempRemovedGameObjectIDs", tempRemovedGameObjectIDs);
            gameState.put("gameObjects", gameObjectsArray);
            gameState.put("removedGameObjectIDs", removedGameObjectIDs);
            gameState.put("posShift", posShift);



            return gameState.toString();
        }

        //Returned when request is incorrectly formatted or contains invalid data
        return "Bad Request";
    }

    private void putWorld(JSONArray gameObjectsArray, World world) {
        int count = 0;
        Tile[][] tiles = world.getTiles();
        for (int i = 0; i < world.getWidth(); i++) {
            for (int j = 0; j < world.getHeight(); j++) {
                count++;
                GameObject obj = tiles[i][j];
                putGameObject(gameObjectsArray, obj);
            }
        }
        System.out.println("Sent " + count + " tiles");
    }

    private JSONObject getUserStats(String sessionID) throws JSONException {
        JSONObject stats = new JSONObject();
        User user = gameService.getUsers().get(sessionID);
        Pirate player = (Pirate) gameService.getGameObjects().get(user.getPlayerObjectID());

        stats.put("health", player.getHealth());
        stats.put("drink", player.getDrinks());
        stats.put("breath", player.getBreath());
        stats.put("hasTreasure", player.hasTreasure());
        stats.put("money", user.getMoney());
        stats.put("hasMap", player.hasMap());
        stats.put("markerRot", player.getTreasureMarkerRot());

        return stats;
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
            gameObject.put("isStatic", obj.isStatic());
            gameObject.put("zIndex", obj.getZIndex());
            gameObject.put("tooltip", obj.getTooltip());
            gameObject.put("isRendered", obj.isRendered());

            gameObjectsArray.put(gameObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void putShopItem(JSONArray shopItemsArray, ShopItem item) {
        try {
            JSONObject shopItem = new JSONObject();
            shopItem.put("name", item.getName());
            shopItem.put("price", item.getPrice());
            shopItem.put("sprite", item.getSprite());

            shopItemsArray.put(shopItem);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
