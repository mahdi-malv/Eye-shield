package com.mmalvandi.serviceone;

import android.content.Context;
import android.content.SharedPreferences;

// This class hold the Seekbar values
public class SharedMemory {

    private SharedPreferences prefs;

    public SharedMemory(Context ctx){
        prefs=ctx.getSharedPreferences("SCREEN_SETTINGS", Context.MODE_PRIVATE);
    }

    public void setAlpha(int value){
        prefs.edit().putInt("alpha", value).apply();
    }

    public void setRed(int value){
        prefs.edit().putInt("red", value).apply();
    }

    public void setGreen(int value){
        prefs.edit().putInt("green", value).apply();
    }

    public void setBlue(int value){
        prefs.edit().putInt("blue", value).apply();
    }

    public int getBlue(){
        return prefs.getInt("blue", 0x00);
    }

    public int getGreen(){
        return prefs.getInt("green", 0x00);
    }

    public int getRed(){
        return prefs.getInt("red", 0x00);
    }

    public int getAlpha(){
        return prefs.getInt("alpha", 0x33);
    }

    public static int getColor(int alpha, int red, int green, int blue){
        String hex = String.format("%02x%02x%02x%02x", alpha, red, green, blue);
        return (int)Long.parseLong(hex,16);
    }

    public int getColor(){
        String hex = String.format("%02x%02x%02x%02x", getAlpha(), getRed(), getGreen(), getBlue());
        return (int)Long.parseLong(hex,16);
    }
}
