package com.mmalvandi.serviceone.activities;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mmalvandi.serviceone.R;

public class AboutActivity extends AppCompatActivity {
    TextView rateUsText, contactUsText, developerText, appNameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setTitle(getString(R.string.about_us));
        if (isFirstRun("myPref")) showHintDialog();// is it first Time Running the App?
        getInit();

        contactUsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AboutActivity.this, EmailActivity.class));
            }
        });
        rateApp();
    }

    private void getInit() {
        rateUsText = (TextView) findViewById(R.id.text_rate_us);
        contactUsText = (TextView) findViewById(R.id.text_contact_us);
        developerText = (TextView) findViewById(R.id.text_developer);
        appNameText = (TextView) findViewById(R.id.app_name);
        appNameText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(AboutActivity.this, R.string.kothar_zero, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        developerText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(AboutActivity.this, R.string.kothar_three, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        contactUsText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(AboutActivity.this, R.string.kothar_two, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        rateUsText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(AboutActivity.this, R.string.kothar_one, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }// provided an easter egg in app (Quran Text by holding some buttons)

    private boolean isFirstRun(String sharedPreferencesName) {
        boolean isFirstRun = getSharedPreferences(sharedPreferencesName, MODE_PRIVATE).getBoolean("isFirstRun2", true);
        if (isFirstRun) {
            getSharedPreferences(sharedPreferencesName, MODE_PRIVATE).edit().putBoolean("isFirstRun2", false).apply();
            return true;
        }
        return false;
    }

    protected void showHintDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(R.string.about_activity);
        alertDialogBuilder.setTitle(R.string.about_eye_shield);
        alertDialogBuilder.setIcon(R.drawable.icon_small);
        alertDialogBuilder.setPositiveButton(R.string.okay,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {/* Do Nothing */}
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void rateApp() {
        rateUsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_EDIT);
                intent.setData(Uri.parse("bazaar://details?id=" + "com.mmalvandi.eyeshield"));
                intent.setPackage("com.farsitel.bazaar");
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AboutActivity.this);
                    alertDialogBuilder.setMessage("Couldn't find Bazaar.\nLooks like you don't have this app!");
                    alertDialogBuilder.setTitle("Error Occurred!");
                    alertDialogBuilder.setIcon(R.drawable.icon_small);
                    alertDialogBuilder.setPositiveButton(R.string.okay,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                        /* Do Nothing */
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }
        });
    }
}