package com.example.pirategame;

public class Util {
    public static float lerp(float start, float end, float amt){
        return (1-amt)*start+amt*end;
    }
}
