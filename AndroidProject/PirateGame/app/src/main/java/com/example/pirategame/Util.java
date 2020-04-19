package com.example.pirategame;

import android.graphics.Bitmap;

import java.util.HashMap;

public class Util {

    public static boolean[] input = new boolean[4];

    public static HashMap<String, Bitmap> bitmaps = new HashMap<>();

    public static String collectInput(){

        String inputString = "";

        for(int i = 0; i < input.length; i++){
            switch (i){
                case 0:
                    inputString += input[0] ? "87 " : "";
                    break;
                case 1:
                    inputString += input[1] ? "83 " : "";
                    break;
                case 2:
                    inputString += input[2] ? "65 " : "";
                    break;
                case 3:
                    inputString += input[3] ? "68 " : "";
                    break;
            }
        }

        return inputString;
    }

    public static float lerp(float start, float end, float amt){
        return (1-amt)*start+amt*end;
    }

    public static int StringToBitmap(String sprite) {
        switch (sprite){
            case "pirate":
                return R.drawable.pirate0;
            case "map":
                return R.drawable.map;
            case "sea1":
                return R.drawable.sea1;
            case "sea2":
                return R.drawable.sea2;
            case "beach1":
                return R.drawable.beach1;
            case "black_shirt":
                return  R.drawable.black_shirt;
            case "boat":
                return R.drawable.boat0;
            case "parrot1":
                return R.drawable.parrot1;
            case "sword1":
                return R.drawable.sword1;
            case "shop":
                return R.drawable.shop;
            case "healthfull":
                return R.drawable.healthfull;
            case "healthempty":
                return R.drawable.healthempty;
            case "bottlefull":
                return R.drawable.bottlefull;
            case "bottleempty":
                return R.drawable.bottleempty;
            case "pier":
                return R.drawable.pier;
            case "underwater1":
                return R.drawable.underwater1;
            case "underwater2":
                return R.drawable.underwater2;
            case "background":
                return R.drawable.background;
            case "treasure":
                return R.drawable.treasure;
            case "bubble":
                return R.drawable.bubble;
            case "marker":
                return R.drawable.marker;
            case "crab":
                return R.drawable.crab;

            default:
                return R.drawable.pirate0;
        }
    }
}
