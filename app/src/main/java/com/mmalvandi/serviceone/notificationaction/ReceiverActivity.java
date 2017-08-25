package com.mmalvandi.serviceone.notificationaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.mmalvandi.serviceone.services.EyeService;
import com.mmalvandi.serviceone.R;

/**
 * There is a action in {@link EyeService} when it is running.
 * Action 'Stop' is to show a dialog. But broadcast can not.
 * So i used pendingIntent line 77 to handle.
 */
public class ReceiverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setWindowFeatures();
        setContentView(R.layout.activity_receiver);
        showDialog();
    }

    public void showDialog() {
        //Activity must be finished anyway.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.do_u_wanna_quit);
        builder.setTitle(R.string.exit_and_stop);
        builder.setIcon(R.drawable.icon_small);
        builder.setPositiveButton(R.string.yes,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        stopService(new Intent(ReceiverActivity.this, EyeService.class));
                        finish();
                    }
                });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });// If outside touched. Activity will be finished
        builder.create().show();
    }

    private void setWindowFeatures() {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
