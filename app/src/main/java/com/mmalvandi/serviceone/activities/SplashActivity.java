package com.mmalvandi.serviceone.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.mmalvandi.serviceone.R;
import com.mmalvandi.serviceone.font.Tools;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Tools.setFont(this, R.id.app_name, "splash");
        startWithDelay(3000L);
    }

    private boolean isFirstRun(String sharedPreferencesName) {
        boolean isFirstRun = getSharedPreferences(sharedPreferencesName, MODE_PRIVATE).getBoolean("isFirstRun", true);
        if (isFirstRun) {
            getSharedPreferences(sharedPreferencesName, MODE_PRIVATE).edit().putBoolean("isFirstRun", false).apply();
            return true;
        }
        return false;
    }

    private void startWithDelay(long delay) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isFirstRun("myPref")) {
                    //TODO : Change and fix
                    Intent mainIntent = new Intent(SplashActivity.this, AnimationActivity.class);
                    finish();
                    startActivity(mainIntent);
                } else {
                    Intent mainIntent = new Intent(SplashActivity.this, AnimationActivity.class);
                    finish();
                    startActivity(mainIntent);
                }
            }
        }, delay);
    }
}
