package com.example.pirategame;

import android.annotation.SuppressLint;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class GameActivity extends Activity {

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;

    private FrameLayout gameLayout;
    private EntityManager entityManager;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        entityManager = new EntityManager();


        gameLayout = findViewById(R.id.linear_layout);

        BackgroundView backgroundView = new BackgroundView(this, entityManager,true);
        gameLayout.addView(backgroundView);

        GameView gameView = new GameView(this, entityManager);
        gameLayout.addView(gameView);

        Button mButtonUp = findViewById(R.id.button_up);
        mButtonUp.setOnTouchListener((v, event) -> {
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    System.out.println("Pressing");
                    Util.input[0] = true;
                    return true;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    System.out.println("Releasing");
                    Util.input[0] = false;
                    return true;
            }
            return false;
        });

        Button mButtonDown = findViewById(R.id.button_down);

        mButtonDown.setOnTouchListener((v, event) -> {
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Util.input[1] = true;
                    return true;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    Util.input[1] = false;
                    return true;
            }
            return false;
        });

        Button mButtonRight = findViewById(R.id.button_right);

        mButtonRight.setOnTouchListener((v, event) -> {
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Util.input[3] = true;
                    return true;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    Util.input[3] = false;
                    return true;
            }
            return false;
        });

        Button mButtonLeft = findViewById(R.id.button_left);

        mButtonLeft.setOnTouchListener((v, event) -> {
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Util.input[2] = true;
                    return true;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    Util.input[2] = false;
                    return true;
            }
            return false;
        });
    }

    public static GButton gButton;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX() - entityManager.getPosShift().first;
        float y = event.getY() - entityManager.getPosShift().second;

        System.out.print("TOUCH: ");
        System.out.println("(" + x + ", " + y + ")");

        System.out.print("MAP: ");
        System.out.println("(" + gButton.x + ", " + gButton.y + ")");
        if(Math.abs(gButton.x - x) < 100 && Math.abs(gButton.y - y) < 100) {
            System.out.println("Button pressed");
            return true;
        }

        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event){
        int keyCode = event.getKeyCode();
        if (event.getAction()==KeyEvent.ACTION_DOWN){
            switch (keyCode) {
                case KeyEvent.KEYCODE_W:
                    GameLoopThread.inputTimer[0] = System.currentTimeMillis();
                    Util.input[0] = true;
                    return true;
                case KeyEvent.KEYCODE_S:
                    GameLoopThread.inputTimer[1] = System.currentTimeMillis();
                    Util.input[1] = true;
                    return true;
                case KeyEvent.KEYCODE_A:
                    GameLoopThread.inputTimer[2] = System.currentTimeMillis();
                    Util.input[2] = true;
                    return true;
                case KeyEvent.KEYCODE_D:
                    GameLoopThread.inputTimer[3] = System.currentTimeMillis();
                    Util.input[3] = true;
                    return true;
                default:
                    return super.onKeyUp(keyCode, event);
            }
        }

        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        delayedHide();
    }

    @Override
    protected void onResume(){
        super.onResume();
        delayedHide();
    }

    private void hide() {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            gameLayout.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
        }
    };

    private final Handler mHideHandler = new Handler();
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide() {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, 100);
    }
}
