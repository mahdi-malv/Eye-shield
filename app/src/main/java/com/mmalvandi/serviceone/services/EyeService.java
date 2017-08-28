package com.mmalvandi.serviceone.services;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.mmalvandi.serviceone.R;
import com.mmalvandi.serviceone.activities.MainActivity;
import com.mmalvandi.serviceone.activities.TimerActivity;
import com.mmalvandi.serviceone.notificationaction.ReceiverActivity;

public class EyeService extends Service {

    public static final String TAG = "EYE_RECEIVER";
    public static final String INTENT_DESTROY = "ir.skillson.eyeshield.DESTROY";
    public static final String INTENT_START = "ir.skillson.eyeshield.START";
    public static final int notifyID = 1;

    int second = 0;
    boolean running = false;
    final Handler handler = new Handler();
    Runnable r;
    private boolean isSoundAllowed = false;
    private boolean isNotificationAllowed = true;
    private int customRestTime, customTime;
    BroadcastReceiver offReceiver = new EyeOffReceiver();
    BroadcastReceiver onReceiver = new EyeOnReceiver();

    public EyeService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isSoundAllowed = intent.getBooleanExtra("sound", false);
        isNotificationAllowed = intent.getBooleanExtra("notification", true);
        customTime = intent.getIntExtra("time", 20);
        customRestTime = intent.getIntExtra("rest_time", 20);
        // initialize the timer vars
        running = true;
        second = intent.getIntExtra("second", 0);
        // start the method
        runTimer();
        registerReceiver(offReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
        registerReceiver(onReceiver, new IntentFilter(Intent.ACTION_SCREEN_ON));
        sendBroadcast(new Intent(INTENT_START));
        return START_STICKY;
    }

    private void runTimer() {


        final NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder;
        builder = builder();

        final Notification notification;
        notification = builder.build();
        startForeground(notifyID, notification);

        r = new Runnable() {
            @SuppressLint("DefaultLocale")
            @Override
            public void run() {
                if (customTime < 1 || customRestTime < 10) {
                    customTime = 1;
                    customRestTime = 10;
                    Toast.makeText(EyeService.this, R.string.invalid_value, Toast.LENGTH_SHORT).show();
                }
                int hours = second / 3600;
                int minutes = (second % 3600) / 60;
                int secs = second % 60;
                String time;
                if (running) {
                    second++;
                }
                if (second == customTime * 60 + 1) {
                    if (isSoundAllowed) {
                        MediaPlayer mediaPlayer = MediaPlayer.create(EyeService.this, R.raw.beep);
                        mediaPlayer.start();
                    }
                    running = false;
                    Intent i = new Intent(EyeService.this, TimerActivity.class);
                    i.putExtra("rest_time", customRestTime);
                    i.putExtra("sound", isSoundAllowed);
                    i.putExtra("time", customTime);
                    i.putExtra("notification", isNotificationAllowed);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    second = 0;
                }
                if (second >= 0) {
                    time = String.format("%02d : %02d : %02d", hours, minutes, secs);
                    builder.setContentText(time + getString(R.string.past_time) + "(" + customTime + getString(R.string.mini_minute) + ")");
                }
                if (isNotificationAllowed) {
                    mNotificationManager.notify(
                            notifyID,
                            builder.build());
                }
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(r);
    }

    class EyeOffReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            SharedPreferences settings = getSharedPreferences("save_service_values", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("sound", isSoundAllowed);
            editor.putBoolean("notification", isNotificationAllowed);
            editor.putInt("time", customTime);
            editor.putInt("rest_time", customRestTime);
            editor.apply();
            // save values in files to restore it later
            handler.removeCallbacks(r);
            second = 0;
            running = false;
        }

    }// Happens when screen OFF

    class EyeOnReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            SharedPreferences settings = getSharedPreferences("save_service_values", 0);
            isSoundAllowed = settings.getBoolean("sound", false);
            isNotificationAllowed = settings.getBoolean("notification", true);
            customTime = settings.getInt("time", 20);
            customRestTime = settings.getInt("rest_time", 20);
            // take back what's left
            handler.removeCallbacks(r);
            running = true;
            second = 0;
            runTimer();
        }

    } // Happens when screen ON

    @Override
    public void onDestroy() {
        Log.e(TAG, "destroy is called from service Obj");
        Intent i = new Intent(INTENT_DESTROY);
        sendBroadcast(i);
        finishEverything();
        super.onDestroy();
    }

    // Restart when task is removed
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.e(TAG, "task is called from service Obj");
        finishEverything();
        restartServiceIfRemoved();
        super.onTaskRemoved(rootIntent);
    }

    private void finishEverything() {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancelAll();
        unregisterReceiver(offReceiver);
        unregisterReceiver(onReceiver);
        handler.removeCallbacks(r);
    }

    private boolean restartServiceIfRemoved() {
        Intent restartServiceTask = new Intent(getApplicationContext(), this.getClass());
        restartServiceTask.putExtra("rest_time", customRestTime);
        restartServiceTask.putExtra("time", customTime);
        restartServiceTask.putExtra("sound", isSoundAllowed);
        restartServiceTask.putExtra("notification", isNotificationAllowed);
        restartServiceTask.putExtra("second", second + 3);
        restartServiceTask.setPackage(getPackageName());
        PendingIntent restartPendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceTask, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager myAlarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        myAlarmService.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 1000,
                restartPendingIntent);
        return false;
    }

    private NotificationCompat.Builder builder() {

        Intent intent = new Intent(getApplicationContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

        return new NotificationCompat.Builder(this)
                .setContentTitle("Eye Shield Running")
                .setOngoing(true)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.eye_icon)
                .addAction(R.drawable.ic_action_stop, "Stop", PendingIntent.getActivity(this, 1, new Intent(this, ReceiverActivity.class), 0))
                .setContentText("");
    }
}