package com.example.pirategame;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Sprite {
    private Bitmap bmp;

    public float scaleX;
    public float scaleY;

    public Sprite(Bitmap bmp) {
        this.bmp=bmp;
    }

    public void onDraw(Canvas canvas, float x, float y) {
        canvas.drawBitmap(bmp, x, y, null);
    }
}