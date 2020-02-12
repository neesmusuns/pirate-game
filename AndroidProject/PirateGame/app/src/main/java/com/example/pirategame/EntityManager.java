package com.example.pirategame;

import android.graphics.Canvas;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Collections;

public class EntityManager {

    GameView gameView;
    Canvas ctx;
    ArrayList<GameObject> gameObjects = new ArrayList<>();
    Pair<Double, Double> posShift;
    int[] removedIDs;
    int playerID;
    Pair<Double, Double> playerPos;
    Stats stats;
    GameObject background;
    boolean changedWorld = true;
    boolean isInShop;
    boolean hasGeneratedShopUI;

    public EntityManager(GameView gameView){
        this.gameView = gameView;
        Start();
    }

    private void Start(){

    }

    public void update(){
        if(background != null)
            background.targetX += 2f;
    }

    public void render(Canvas ctx){
        if(background != null){
            background.render(ctx);
        } else{
            System.out.println("Added background");
            background = new GameObject(-1, 0, 0,
                                    2, 2, -1,
                                    "background", "",
                                    true, true,
                                    gameView, this);
        }

        if (gameObjects.size() > 0) {
            //Sort gameObjects via zIndex
            Collections.sort(gameObjects);

            gameObjects.forEach(obj -> obj.render(ctx));
        }
    }
}
