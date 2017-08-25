package com.mmalvandi.serviceone.services;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.WindowManager;
import android.widget.LinearLayout;
import com.mmalvandi.serviceone.SharedMemory;

/**
 * This is the class that is responsible for adding the filter on the
 * screen: It works as a service, so that the view persists across all
 * activities.
 *
 * @author Skillson
 */
public class FilterService extends Service {

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
        return super.onStartCommand(intent, flags, startId);
    }
}