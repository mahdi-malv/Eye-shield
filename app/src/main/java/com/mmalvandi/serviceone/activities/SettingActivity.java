package com.mmalvandi.serviceone.activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.mmalvandi.serviceone.R;


public class SettingActivity extends PreferenceActivity {

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //The following function is highly deprecated
        //It is highly suggested to use fragment later though
        addPreferencesFromResource(R.xml.pref);
    }
}
