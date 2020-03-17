package com.example.pirategame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import com.example.pirategame.persistentcookiejar.PersistentCookieJar;
import com.example.pirategame.persistentcookiejar.cache.SetCookieCache;
import com.example.pirategame.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import okhttp3.CookieJar;
import okhttp3.OkHttpClient;

public class GameView extends SurfaceView {
    private GameView gameView;
    private GameLoopThread gameLoopThread;

    EntityManager entityManager;

    boolean hasSentLogIn = false;

    OkHttpClient client;


    public GameView(Context context, EntityManager entityManager, boolean isBackground){
        super(context);
    }

    public GameView(Context context, EntityManager entityManager) {
        super(context);
        setFocusable(true);
        InputMethodManager mgr = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT);
        gameView = this;
        gameView.setZOrderOnTop(true);
        this.entityManager = entityManager;
        gameLoopThread = new GameLoopThread(gameView);

        CookieJar cookieJar =
                new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context));

        client = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .build();

        SurfaceHolder holder = getHolder();

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

    void render(Canvas ctx) {
        ctx.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        if(!hasSentLogIn) {
            hasSentLogIn = true;
            RequestSender.sendLogInRequest(entityManager, client, ctx);
        }
        if(entityManager.isLoggedIn) {
            RequestSender.sendUpdateRequest(entityManager, client, ctx);
        }

        entityManager.render(ctx, false, this);
    }
}