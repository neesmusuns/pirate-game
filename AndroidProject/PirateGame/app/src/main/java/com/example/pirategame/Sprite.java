package com.example.pirategame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Sprite {
    private Bitmap bmp;

    public float scaleX;
    public float scaleY;

    Paint paint = new Paint();

    public Sprite(Bitmap bmp) {
        this.bmp=bmp;
        paint.setAntiAlias(false);
        paint.setDither(true);
        paint.setFilterBitmap(false);
    }

    void onDraw(Canvas canvas, float x, float y) {
        canvas.save();
        canvas.scale(this.scaleX, this.scaleY);
        canvas.translate(x, y);
        canvas.rotate(0);
        int w = bmp.getWidth();
        int h = bmp.getHeight();
        canvas.drawBitmap(bmp, -w/2, -h/2, paint);
        canvas.restore();
    }
}