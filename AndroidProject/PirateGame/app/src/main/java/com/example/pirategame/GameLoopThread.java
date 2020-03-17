package com.example.pirategame;

import android.annotation.SuppressLint;
import android.graphics.Canvas;

public class GameLoopThread extends Thread {
    private GameView view;
    private boolean running = false;

    private long previousTime;
    private int fps = 60;

    public GameLoopThread(GameView view) {
        this.view = view;
    }

    public void setRunning(boolean run) {
        running = run;
    }

    @SuppressLint("WrongCall")
    @Override
    public void run() {
        while (running) {
            long currentTimeMillis = System.currentTimeMillis();
            long elapsedTimeMs = currentTimeMillis - previousTime;
            long sleepTimeMs = (long) (1000f/ fps - elapsedTimeMs);

            Canvas c = null;
            try {
                c = view.getHolder().lockCanvas();
                if (c == null) {
                    Thread.sleep(1);

                    continue;

                }else if (sleepTimeMs > 0){

                    Thread.sleep(sleepTimeMs);

                }

                synchronized (view.getHolder()) {
                    view.render(c);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (c != null) {
                    view.getHolder().unlockCanvasAndPost(c);
                    previousTime = System.currentTimeMillis();
                }
            }
        }
    }
}
