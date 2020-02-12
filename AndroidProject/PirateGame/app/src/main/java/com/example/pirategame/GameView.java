package com.example.pirategame;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView {
    private Bitmap bmp;
    private SurfaceHolder holder;
    private GameView gameView;
    private GameLoopThread gameLoopThread;
    private Sprite sprite;

    private EntityManager entityManager;

    public GameView(Context context) {
        super(context);
        gameView = this;
        entityManager = new EntityManager(gameView);
        gameLoopThread = new GameLoopThread(gameView);
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                boolean retry = true;
                gameLoopThread.setRunning(false);
                while (retry) {
                    try {
                        gameLoopThread.join();
                        retry = false;
                    } catch (InterruptedException e) {
                    }
                }
            }


            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (gameLoopThread.getState() == Thread.State.TERMINATED)
                {
                    gameLoopThread = new GameLoopThread(gameView);
                }

                gameLoopThread.setRunning(true);
                gameLoopThread.start();
            }



            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        entityManager.update();
        entityManager.render(canvas);
    }
}