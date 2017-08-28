package com.mmalvandi.serviceone.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.view.WindowManager;
import android.widget.LinearLayout;
import com.mmalvandi.serviceone.SharedMemory;
import com.mmalvandi.serviceone.activities.FilterActivity;

/**
 * This is the class that is responsible for adding the filter on the
 * screen: It works as a service, so that the view persists across all
 * activities.
 *
 * @author Skillson
 */
public class FilterService extends Service {

    public static final int NOTIFICATION_ID = 2;
    LinearLayout linearLayout;
    SharedMemory sharedMemory;

    public static int STATE;

    public static final int INACTIVE=0;
    public static final int ACTIVE=0;

    static{
        STATE=INACTIVE;
    }

    @Override
    public IBinder onBind(Intent i) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sharedMemory =new SharedMemory(this);
        linearLayout = new LinearLayout(this);
        linearLayout.setBackgroundColor(sharedMemory.getColor());
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManager.addView(linearLayout, params);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(linearLayout !=null){
            WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
            windowManager.removeView(linearLayout);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        linearLayout.setBackgroundColor(sharedMemory.getColor());
        makeNotification();
        return START_STICKY;
    }

    private void makeNotification() {
        NotificationCompat.Builder nb = new NotificationCompat.Builder(this);
        nb.setContentTitle("EyeShield Filter");
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
        mNotificationManager.notify(NOTIFICATION_ID, nb.build());
        final Notification notification;
        notification = nb.build();
        startForeground(NOTIFICATION_ID, notification);
    }
}