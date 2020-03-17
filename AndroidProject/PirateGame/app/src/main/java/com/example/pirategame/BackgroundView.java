package com.example.pirategame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.pirategame.persistentcookiejar.PersistentCookieJar;
import com.example.pirategame.persistentcookiejar.cache.SetCookieCache;
import com.example.pirategame.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import okhttp3.CookieJar;
import okhttp3.OkHttpClient;

public class BackgroundView extends GameView {


    private SurfaceHolder holder;
    private GameView gameView;
    private GameLoopThread gameLoopThread;

    EntityManager entityManager;


    public BackgroundView(Context context, EntityManager entityManager, boolean isBackground) {
        super(context, entityManager, isBackground);
        gameView = this;
        gameView.setZOrderOnTop(true);
        this.entityManager = entityManager;
        gameLoopThread = new GameLoopThread(gameView);

        holder = getHolder();

        holder.setFormat(PixelFormat.TRANSPARENT);

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
    protected void render(Canvas ctx) {
        entityManager.render(ctx, true, this);
    }
}