package com.mmalvandi.serviceone.activities;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import com.mmalvandi.serviceone.R;
import com.mmalvandi.serviceone.services.EyeService;

public class TimerActivity extends AppCompatActivity {

    TextView timerText;
    Button skip;
    Handler timeHandler;
    Runnable timeRunnable;
    float timeCounter = 0;
    boolean isSkipPressed = false, doubleBackToExitPressedOnce = false;
    SharedPreferences settings;
    String secondsRemained;
    BroadcastReceiver screenOffReceiver = new ScreenOffReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setWindowFeatures();
        changePosition();
        getInit();
        settings = PreferenceManager.getDefaultSharedPreferences(TimerActivity.this);

        timer();

        registerReceiver(screenOffReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        return MotionEvent.ACTION_OUTSIDE != event.getAction() && super.onTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) return false;
        else return false;
    }

    @Override
    protected void onDestroy() {
        try {
            unregisterReceiver(screenOffReceiver);
        } catch (Exception ignored) {
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {/* do noting */
        onSkipPressed(null);
    }

    public void onSkipPressed(View view) {
        if (doubleBackToExitPressedOnce) {
            isSkipPressed = true;
            //First gets the values retrieved from service's intent. If failed uses default
            //TODO: Get values true someHow
            stopService(new Intent(TimerActivity.this, EyeService.class));
            startService(new Intent(TimerActivity.this, EyeService.class)
                    .putExtra("sound", getIntent().getBooleanExtra("sound", false))
                    .putExtra("notification", getIntent().getBooleanExtra("notification", true))
                    .putExtra("time", getIntent().getIntExtra("time", 20))
                    .putExtra("rest_time", getIntent().getIntExtra("rest_time", 20))
            );
            finish();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        skip.setText(R.string.skip_q);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
                skip.setText(R.string.skip);
            }
        }, 2000);

    }

    //This method does the time counting in service
    private void timer() {

        final int timerInitializer = getIntent().getIntExtra("rest_time", 20);
        final boolean isSoundActive = getIntent().getBooleanExtra("sound", false);
        final int time = getIntent().getIntExtra("time", 20);
        final boolean isNotificationAllowed = getIntent().getBooleanExtra("notification", true);
        timeCounter = timerInitializer + 2;
        timeHandler = new Handler();
        timeRunnable = new Runnable() {
            @SuppressLint("DefaultLocale")
            @Override
            public void run() {
                if (!isSkipPressed) {
                    timeCounter--;
//                    secondsRemained = String.format("%2d", (int) timeCounter) + getString(R.string.seconds);
                    if (timeCounter == 1)
                        secondsRemained = String.format("%2d", (int) timeCounter) + getString(R.string.second);
                    else if (timeCounter > 1)
                        secondsRemained = String.format("%2d", (int) timeCounter) + getString(R.string.seconds);
                    else secondsRemained = getString(R.string.done);
                    timerText.setText(secondsRemained);
                } else timeHandler.removeCallbacks(timeRunnable);
                if ((int) timeCounter == 0) {
                    timeHandler.removeCallbacks(timeRunnable);

                    stopService(new Intent(TimerActivity.this, EyeService.class));
                    startService(new Intent(TimerActivity.this, EyeService.class)
                            .putExtra("time", time)
                            .putExtra("rest_time", timerInitializer)
                            .putExtra("notification", isNotificationAllowed)
                            .putExtra("sound", isSoundActive)
                    );
                    finish();
                    if (isSoundActive) {
                        MediaPlayer mediaPlayer = MediaPlayer.create(TimerActivity.this, R.raw.beep_1);
                        mediaPlayer.start();
                    }
                }
                timeHandler.postDelayed(timeRunnable, 1000);
            }
        };
        timeRunnable.run();
    }

    private void getInit() {
        skip = (Button) findViewById(R.id.text_skip);
        timerText = (TextView) findViewById(R.id.text_timer);
    }

    private void changePosition() {
        try {
            WindowManager.LayoutParams wmlp = TimerActivity.this.getWindow().getAttributes();
            wmlp.gravity = Gravity.TOP | Gravity.FILL_HORIZONTAL;
            wmlp.windowAnimations = android.R.style.Animation_Translucent;
        } catch (NullPointerException ignored) {
        }
    }

    //Wish to understand why i used finish() here? If screen goes off it will finish and must be
    class ScreenOffReceiver extends BroadcastReceiver {
            @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    }

    private void setWindowFeatures() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

}
