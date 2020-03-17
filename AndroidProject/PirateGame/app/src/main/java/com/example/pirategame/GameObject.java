package com.example.pirategame;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class GameObject implements Comparable {
    EntityManager entityManager;

    int id  = 0;
    float targetX = 0;
    float targetY = 0;
    float x = 0;
    float y = 0;
    Sprite sprite;
    private final String spriteString;
    float scaleX = 1;
    float scaleY = 1;
    int zIndex = 0;
    String tooltip = "";

    boolean isStatic = false;
    boolean isRendered = true;
    public boolean hasBeenRendered;

    public GameObject(int id, float targetX, float targetY, float scaleX, float scaleY, int zIndex,
                      String spriteString, String tooltip, boolean isStatic, boolean isRendered,
                      EntityManager entityManager){
        this.id = id;
        this.targetX = targetX;
        this.targetY = targetY;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.zIndex = zIndex;
        this.tooltip = tooltip;
        this.isStatic = isStatic;
        this.isRendered = isRendered;
        this.entityManager = entityManager;
        this.spriteString = spriteString;
    }

    public void render(Canvas ctx, GameView gameView){
        if(this.sprite == null){
            if(Util.bitmaps.containsKey(spriteString))
                this.sprite = new Sprite(Util.bitmaps.get(spriteString));
            else{
                Bitmap bmp = BitmapFactory.decodeResource(gameView.getResources(),
                        Util.StringToBitmap(spriteString));
                this.sprite = new Sprite(bmp);

                Util.bitmaps.put(spriteString, bmp);
            }
        }
        float origScaleX = scaleX;

        this.sprite.scaleX = this.scaleX;

        float origScaleY = this.sprite.scaleY;

        this.sprite.scaleY = this.scaleY;

        if(this.isStatic || entityManager.changedWorld) {
            this.x = this.targetX;
            this.y = this.targetY;
        } else{
            this.x = Util.lerp(this.x, this.targetX, 0.1f);
            this.y = Util.lerp(this.y, this.targetY, 0.1f);
        }

        if(this.isRendered) {
            this.sprite.onDraw(
                    ctx, this.x, this.y
            );
        }

        if(!this.tooltip.equals("")) {
            //ctx.font = "12px Arial";
            //ctx.textAlign = "center";
            //ctx.fillText(this.tooltip, this.x, this.y - 20);

            Paint p = new Paint();
            ctx.drawText(tooltip, x, y, p);
        }

        this.sprite.scaleX = origScaleX;
        this.sprite.scaleY = origScaleY;
    }

    @Override
    public int compareTo(Object o) {
        GameObject go = (GameObject) o;

        return Integer.compare(zIndex, go.zIndex);
    }
}
