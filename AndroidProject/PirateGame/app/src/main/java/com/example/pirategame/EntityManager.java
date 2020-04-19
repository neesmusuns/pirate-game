package com.example.pirategame;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Pair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class EntityManager {

    private Map<Integer, GameObject> gameObjects = new LinkedHashMap<>();
    private Map<Integer, GameObject> backgroundObjects = new LinkedHashMap<>();

    private Queue<Pair<Integer, GameObject>> backgroundObjectQueue = new LinkedList<>();

    private Pair<Float, Float> posShift = new Pair<>(0.0f, 0.0f);
    int[] removedIDs;
    private int playerID;
    private Pair<Float, Float> playerPos = new Pair<>(0.0f, 0.0f);
    private Stats stats = new Stats();
    private GameObject background;
    boolean changedWorld = true;
    private boolean isInShop;
    private boolean hasGeneratedShopUI;

    public boolean isLoggedIn;
    private boolean gameObjectsMarkedDirty;

    public EntityManager(){
        Start();
    }


    private void Start(){
    }

    public void updateGameState(JSONObject gameState, Canvas ctx) throws JSONException {
        {
            //TODO: Implement shop UI
            if(gameState.has("shopItems")){
                this.isInShop = true;
                if(!this.hasGeneratedShopUI){
                    //renderShopUI(gameState.shopItems);
                    this.hasGeneratedShopUI = true;
                }
                //$("#money").html("GOLD: " + this._stats.money);
            } else{
                //if(this.hasGeneratedShopUI)
                    //clearShopUI();
            }



            if(gameState.has("playerID")){
                this.playerID = gameState.getInt("playerID");
            }

            if(gameState.has("stats")) {
                JSONObject stats = (JSONObject) gameState.get("stats");
                this.stats.setHealth(stats.getInt("health"));
                this.stats.setDrink(stats.getInt("drink"));
                this.stats.setBreath(stats.getInt("breath"));
                this.stats.setHasTreasure(stats.getBoolean("hasTreasure"));
                this.stats.setMoney(stats.getInt("money"));
                this.stats.setHasMap(stats.getBoolean("hasMap"));
                this.stats.setMarkerRot(stats.getDouble("health"));
            }

            JSONArray gameObjects = gameState.getJSONArray("gameObjects");

            //Iterate through all received game objects
            for(int i = 0; i < gameObjects.length(); i++){
                JSONObject go = (JSONObject) gameObjects.get(i);
                if(go.getInt("id") == this.playerID)
                    this.playerPos= new Pair<>((float)go.getDouble("x"),
                                               (float)go.getDouble("y"));

                int foundID = go.getInt("id");
                boolean hasFoundObject = false;

                //Check if game object exists
                if(this.gameObjects.containsKey(foundID) || this.backgroundObjects.containsKey(foundID)){
                    hasFoundObject = true;
                    GameObject obj = this.gameObjects.get(foundID);
                    if(obj == null) continue;
                    obj.targetX = (float) go.getDouble("x");
                    obj.targetY = (float) go.getDouble("y");
                    obj.scaleX = (float) go.getDouble("scaleX");
                    obj.scaleY = (float) go.getDouble("scaleY");
                    obj.zIndex = go.getInt("zIndex");
                    obj.isRendered = go.getBoolean("isRendered");
                    if (this.playerID == foundID)
                        obj.tooltip = go.getString("tooltip");
                }

                //If no instance exists, create a new instance
                if(!hasFoundObject) {
                    GameObject obj = new GameObject(
                            go.getInt("id"),
                            (float) go.getDouble("x"),
                            (float) go.getDouble("y"),
                            (float) go.getDouble("scaleX"),
                            (float) go.getDouble("scaleY"),
                            go.getInt("zIndex"),
                            go.getString("sprite"),
                            "",
                            go.getBoolean("isStatic"),
                            go.getBoolean("isRendered"),
                            this
                    );
                    if(obj.zIndex == 0) //If background object
                        this.backgroundObjectQueue.add(new Pair<>(obj.id, obj));
                    else
                        this.gameObjects.put(obj.id, obj);
                    gameObjectsMarkedDirty = true;
                }

            }

            JSONArray tempRemovedGameObjects = gameState.getJSONArray("tempRemovedGameObjectIDs");
            for(int i = 0; i < tempRemovedGameObjects.length(); i++) {
                int ID = ((JSONObject) tempRemovedGameObjects.get(i)).getInt("id");
                int index = 0;
                for(int k = 0; k < this.gameObjects.size(); k++){
                    if(this.gameObjects.get(k).id == ID) {
                        index = k;
                        break;
                    }
                }
                this.gameObjects.remove(index);
            }

            JSONArray removedGameObjects = gameState.getJSONArray("removedGameObjectIDs");
            for(int i = 0; i < removedGameObjects.length(); i++) {
                int ID = (removedGameObjects.getInt(i));
                int index = 0;
                for(int k = 0; k < this.gameObjects.size(); k++){
                    if(this.gameObjects.get(k).id == ID) {
                        index = k;
                        break;
                    }
                }
                this.gameObjects.remove(index);

            }

            if(this.background != null) {
                this.background.targetX = -this.posShift.first;
                this.background.targetY = -this.posShift.second;
            }

            if(gameState.has("changedWorld")){
                this.changedWorld = gameState.getBoolean("changedWorld");
            }

            JSONObject posShift = (JSONObject) gameState.get("posShift");

            float x = (float) posShift.getDouble("x");
            float y = (float) posShift.getDouble("y");
            if(!this.changedWorld) {
                float xLerp = Util.lerp(0, (x-800) - this.posShift.first, 0.03f);
                float yLerp = Util.lerp(0, (y-300) - this.posShift.second, 0.03f);

                //System.out.println("X: " + xLerp + "Y: " + yLerp);

                this.posShift = new Pair<>(this.posShift.first + xLerp, this.posShift.second + yLerp);
            } else{
                float xLerp = Util.lerp(0, x - this.posShift.first, 0.03f);
                float yLerp = Util.lerp(0, y - this.posShift.second, 0.03f);

                this.posShift = new Pair<>(this.posShift.first + xLerp, this.posShift.second + yLerp);
            }
        }
    }

    public void render(Canvas ctx, boolean isBackground, GameView gameView){
        if(!isBackground) {
            if(GameActivity.gButton == null){
                GameActivity.gButton = new GButton(100, 100,
                        BitmapFactory.decodeResource(gameView.getResources(),
                                Util.StringToBitmap("map")));
            }

            GameActivity.gButton.setPosition(playerPos.first + 500, playerPos.second + 500);
            GameActivity.gButton.x = playerPos.first + 500;
            GameActivity.gButton.y = playerPos.second + 500;
            GameActivity.gButton.draw(ctx);

            ctx.translate(this.posShift.first, this.posShift.second);

            if (gameObjects.size() > 0) {
                //Sort gameObjects via zIndex
                if (gameObjectsMarkedDirty) {
                    gameObjects = sortByValue(gameObjects);
                    gameObjectsMarkedDirty = false;
                }

                gameObjects.forEach((id, obj) -> {
                    obj.render(ctx, gameView);
                });
            }
        } else {
            ctx.translate(posShift.first, posShift.second);

            if(background != null && !background.hasBeenRendered){
                background.render(ctx, gameView);
                background.hasBeenRendered = true;
            } else{
                background = new GameObject(-1, 0, 0,
                        2, 2, -1,
                        "background", "",
                        true, true,
                        this);
            }

            while(!backgroundObjectQueue.isEmpty()){
                Pair<Integer, GameObject> p = backgroundObjectQueue.peek();
                if(p != null) {
                    backgroundObjects.put(p.first, p.second);
                    backgroundObjectQueue.remove();
                }
            }

            backgroundObjects.forEach((id, obj) -> {
                //if (!obj.hasBeenRendered) {
                    obj.render(ctx, gameView);
                    obj.hasBeenRendered = true;
                //}
            });

        }
    }

    private static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

    public Pair<Float, Float> getPosShift() {
        return posShift;
    }
}
