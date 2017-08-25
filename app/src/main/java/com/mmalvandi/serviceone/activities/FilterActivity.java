package com.mmalvandi.serviceone.activities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.SeekBar;
import com.mmalvandi.serviceone.R;
import com.mmalvandi.serviceone.SharedMemory;
import com.mmalvandi.serviceone.services.FilterService;

public class FilterActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    SharedMemory shared;

    SeekBar alphaSeek;
    SeekBar redSeek;
    SeekBar greenSeek;
    SeekBar blueSeek;

    int alpha, red, green, blue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        setTitle("Set Filter");
        initialize();
    }

    private void initialize() {
        shared = new SharedMemory(this);
        getInit();
        alphaSeek.setOnSeekBarChangeListener(this);
        redSeek.setOnSeekBarChangeListener(this);
        greenSeek.setOnSeekBarChangeListener(this);
        blueSeek.setOnSeekBarChangeListener(this);

        alpha = shared.getAlpha();
        red = shared.getRed();
        green = shared.getGreen();
        blue = shared.getBlue();

        alphaSeek.setProgress(alpha);
        redSeek.setProgress(red);
        greenSeek.setProgress(green);
        blueSeek.setProgress(blue);

        updateColor();
    }

    private void stopServiceIfActive() {
        if (FilterService.STATE == FilterService.ACTIVE) {
            Intent i = new Intent(FilterActivity.this, FilterService.class);
            stopService(i);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {
        if (seekBar == alphaSeek) {
            alpha = seekBar.getProgress();
        }
        if (seekBar == redSeek) {
            red = seekBar.getProgress();
        }
        if (seekBar == greenSeek) {
            green = seekBar.getProgress();
        }
        if (seekBar == blueSeek) {
            blue = seekBar.getProgress();
        }
        updateColor();
    }

    private void updateColor() {
        int color = SharedMemory.getColor(alpha, red, green, blue);
        ColorDrawable cd = new ColorDrawable(color);
        getWindow().setBackgroundDrawable(cd);
    }

    @Override
    public void onStartTrackingTouch(SeekBar sb) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar sb) {
    }

    public void cancelClick(View v) {
        stopServiceIfActive();
        NotificationManager m = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        m.cancel(0x355);
    }

    public void applyClick(View v) {
        shared.setAlpha(alpha);
        shared.setRed(red);
        shared.setGreen(green);
        shared.setBlue(blue);

        Intent i = new Intent(FilterActivity.this, FilterService.class);
        startService(i);

        makeNotification();
    }

    private void makeNotification() {
        NotificationCompat.Builder nb = new NotificationCompat.Builder(this);
        nb.setContentTitle("Shady Screen Filter");
        nb.setSmallIcon(android.R.drawable.btn_star_big_off);
        nb.setContentText("Active");
        nb.setAutoCancel(true);
        Intent resultIntent = new Intent(this, FilterActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(FilterActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        nb.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0x355, nb.build());
    }

    private void getInit() {
        alphaSeek = (SeekBar) findViewById(R.id.alphaControl);
        redSeek = (SeekBar) findViewById(R.id.redControl);
        greenSeek = (SeekBar) findViewById(R.id.greenControl);
        blueSeek = (SeekBar) findViewById(R.id.blueControl);
    }
}