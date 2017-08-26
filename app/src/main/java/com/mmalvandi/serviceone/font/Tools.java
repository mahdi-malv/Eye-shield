package com.mmalvandi.serviceone.font;
/* Creator: Mahdi on 8/25/2017. */

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.view.Display;
import android.widget.TextView;

public class Tools {

    /**
     *
     * @param context to get Assets
     * @param textViewId  to get the View to change it's font
     * @param fontName The name of font you want to use --> Must be in Assets/fonts with .ttf extension
     */
    public static void setFont(Activity context, int textViewId, String fontName) {
        Typeface face = Typeface.createFromAsset(context.getAssets(),
                "fonts/" + fontName + ".ttf");
        TextView tv = (TextView) context.findViewById(textViewId);
        tv.setTypeface(face);
    }

    public static void stopNotification(Context context) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancelAll();
    }

    public static BitmapDrawable toBitmpDrawable(Activity activity, int resDrawable) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Bitmap bmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                activity.getResources(), resDrawable), size.x, size.y, true);
        return new BitmapDrawable(activity.getResources(), bmp);
    }
}
