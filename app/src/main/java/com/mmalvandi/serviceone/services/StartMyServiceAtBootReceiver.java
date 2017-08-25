package com.mmalvandi.serviceone.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Calendar;

public class StartMyServiceAtBootReceiver extends BroadcastReceiver {
    SharedPreferences settings;
    int hour = Calendar.HOUR_OF_DAY;
    boolean isNight = hour < 6 || hour > 19;

    @Override
    public void onReceive(Context context, Intent intent) {
        settings = PreferenceManager.getDefaultSharedPreferences(context);
        int time;
        boolean notification = settings.getBoolean("notification", true);
        boolean sound = settings.getBoolean("sound", false);
        time = isNight ? 10 : 20;
        if (settings.getBoolean("startup", true)) {
            if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
                Intent serviceIntent = new Intent(context, EyeService.class)
                        .putExtra("time", time)
                        .putExtra("notification", notification)
                        .putExtra("sound", sound);
                context.startService(serviceIntent);
            }
        }

    }
}
