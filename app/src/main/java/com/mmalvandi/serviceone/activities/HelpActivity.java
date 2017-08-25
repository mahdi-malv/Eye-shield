package com.mmalvandi.serviceone.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mmalvandi.serviceone.R;

public class HelpActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        setTitle(R.string.how_to_use);
    }
}
