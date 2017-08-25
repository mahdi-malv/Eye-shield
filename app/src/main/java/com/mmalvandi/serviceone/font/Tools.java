package com.mmalvandi.serviceone.font;
/* Creator: Mahdi on 8/25/2017. */

import android.app.Activity;
import android.graphics.Typeface;
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


}
