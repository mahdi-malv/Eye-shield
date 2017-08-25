package com.mmalvandi.serviceone.activities;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;
import com.mmalvandi.serviceone.R;
import com.mmalvandi.serviceone.maindraweritems.Items;
import com.mmalvandi.serviceone.maindraweritems.ListAdapter;
import com.mmalvandi.serviceone.maindraweritems.RecyclerItemClickListener;
import com.mmalvandi.serviceone.services.EyeService;

//TODO: add persian support

@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity {

    public static final String RECEIVER_TAG = "Main.Broadcast";
    RelativeLayout layout;
    CircleMenu circleMenu;
    SharedPreferences settings;
    ActionBarDrawerToggle toggle;

    private enum Mode {
        NONE,
        DAY,
        NIGHT,
        MY_MODE
    }

    ;

    private Mode mode = Mode.NONE;
    public static final String MAIN_COLOR = "#01579b";
    public static final String DAY_COLOR = "#ffc107";
    public static final String NIGHT_COLOR = "#565454";
    public static final String MODE_COLOR = "#2e7d32";
    public static final String OFF_COLOR = "#f44336";
    public static final int bgOn = R.drawable.bg1_blur;
    public static final int bgOff = R.drawable.bg2_blur;
    BitmapDrawable bgON, bgOFF;
    DrawerLayout drawerLayout;
    RecyclerView list;
    RecyclerView.Adapter adapter;
    BroadcastReceiver screenUpdate = new ScreenUpdateBroadcast();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addToolbar();
        getInit();

        bgON = toBitmpDrawable(bgOn);
        bgOFF = toBitmpDrawable(bgOff);

        retrieveMode();
        newInfoChanger();
        addOrChangeCircleMenu(MAIN_COLOR, DAY_COLOR, NIGHT_COLOR, MODE_COLOR);
        setCircleMenuClicks();
        addListItemClickEvents();
        registerReceiver(screenUpdate, new IntentFilter(EyeService.INTENT_DESTROY));
        registerReceiver(screenUpdate, new IntentFilter(EyeService.INTENT_START));
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.START))
            drawerLayout.closeDrawer(Gravity.START);
        else
            finish();
    }

    @Override
    protected void onStop() {
        SharedPreferences preferences = getSharedPreferences("save_mode", MODE_PRIVATE);
        preferences.edit().putInt("mode", mode.ordinal()).apply();
        super.onStop();
        try {
            unregisterReceiver(screenUpdate);
        } catch (Exception ignored) {
        }
    }

    public boolean eyeServiceIsRunning() {
        Class<?> serviceClass = EyeService.class;
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    } // Check if service runnin'

    public void onClickExit(View view) {
        finish();
    }

    private void onClickOptionAbout() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(R.string.about_message);
        alertDialogBuilder.setTitle(R.string.about);
        alertDialogBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                //do Nothing
            }
        });
        alertDialogBuilder.setNegativeButton(R.string.rate, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MainActivity.this, R.string.no_rate, Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void onClickExitAndStop() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(R.string.do_u_wanna_quit);
        alertDialogBuilder.setTitle(R.string.exit_and_stop);
        alertDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                stopService(new Intent(MainActivity.this, EyeService.class));
                finish();
            }
        });
        alertDialogBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();

        if (eyeServiceIsRunning()) alertDialog.show();
        else finish();
    }

    public void onClickSettings() {
        startActivity(new Intent(MainActivity.this, SettingActivity.class));
    }

    public void showToast(String mode) {
        Toast toast = Toast.makeText(MainActivity.this, "EyeShield Mode \"" + mode + "\" activated!", Toast.LENGTH_SHORT);
        View toastView = toast.getView();
        TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
        toastMessage.setTextSize(19);
        toastMessage.setTextColor(Color.BLACK);
        toastMessage.setGravity(Gravity.CENTER);
        toastMessage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_small, 0, 0, 0);
        toastMessage.setCompoundDrawablePadding(16);
        toastView.setBackgroundColor(Color.WHITE);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();
    }

    private void addOrChangeCircleMenu(String main, String day, String night, String mode) {
        circleMenu.setMainMenu(Color.parseColor(main), R.drawable.ic_power_1, R.drawable.ic_power_2)
                .addSubMenu(Color.parseColor(mode), R.drawable.ic_mode)
                .addSubMenu(Color.parseColor(day), R.drawable.ic_day)
                .addSubMenu(Color.parseColor(OFF_COLOR), R.drawable.ic_off)
                .addSubMenu(Color.parseColor(night), R.drawable.ic_night);
    }

    private void setCircleMenuClicks() {
        circleMenu.setOnMenuSelectedListener(new OnMenuSelectedListener() {
            @Override
            public void onMenuSelected(int index) {
                switch (index) {
                    case 0:
                        activeMyMode();
                        break;
                    case 1:
                        activeDayMode();
                        break;
                    case 3:
                        activeNightMode();
                        break;
                    case 2:
                        turnAllOff();
                        break;
                }
            }
        });
    }

    private void activeDayMode() {
        //Happens when night mode is pressed
        mode = Mode.DAY;
        layout.setBackground(bgON);

        newInfoChanger();

        stopServiceIfRunning();
        GetSavedValues get = new GetSavedValues(this);
        startServiceWithValues(20, 20, get.isNotification(), get.isSound());
        showToast("Day");
    }

    private void activeNightMode() {
        mode = Mode.NIGHT;
        layout.setBackground(bgON);
        newInfoChanger();

        stopServiceIfRunning();
        GetSavedValues get = new GetSavedValues(this);
        startServiceWithValues(10, 20, get.isNotification(), get.isSound());
        showToast("Night");
    }

    private void activeMyMode() {
        mode = Mode.MY_MODE;
        layout.setBackground(bgON);
        newInfoChanger();

        stopServiceIfRunning();
        GetSavedValues get = new GetSavedValues(this);
        startServiceWithValues(get.getTime(), get.getRestTime(), get.isNotification(), get.isSound());
        showToast("My Mode");
    }

    private void turnAllOff() {
        mode = Mode.NONE;
        layout.setBackground(bgOFF);
        newInfoChanger();
        stopServiceIfRunning();

    }

    private void newInfoChanger() {
        TextView powerInfo = (TextView) findViewById(R.id.modeShow);
        String text = "Mode : ";
        switch (mode) {
            case NONE:
                text += "Deactive";
                break;
            case DAY:
                text += "Day Mode Active";
                break;
            case NIGHT:
                text += "Night Mode Active";
                break;
            case MY_MODE:
                text += "My Mode Active";
        }
        powerInfo.setText(text);
    }

    private void retrieveMode() {
        SharedPreferences preferences = getSharedPreferences("save_mode", MODE_PRIVATE);
        if (!eyeServiceIsRunning()) {
            mode = Mode.NONE;
            preferences.edit().putInt("mode", 0).apply();
            layout.setBackground(bgOFF);
        } else {
            int a = preferences.getInt("mode", 0);
            switch (a) {
                case 1:
                    mode = Mode.DAY;
                    break;
                case 2:
                    mode = Mode.NIGHT;
                    break;
                case 3:
                    mode = Mode.MY_MODE;
                    break;
            }
            layout.setBackground(bgON);
        }
    }

    private void stopServiceIfRunning() {
        if (eyeServiceIsRunning()) {
            stopService(new Intent(MainActivity.this, EyeService.class));
        }
    }

    class ScreenUpdateBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            final String intentAction = intent.getAction();
            if (intentAction.equals(EyeService.INTENT_DESTROY)) {
                //Means it is destroyed and if Ui is running it will update screen
                TextView t = (TextView) findViewById(R.id.modeShow);
                t.setText("Mode : Deactive");
                layout.setBackground(bgOFF);
            } else if (intentAction.equals(EyeService.INTENT_START)) {
                newInfoChanger();
                layout.setBackground(bgON);
            }
        }
    }

    /*MAIN startService*/
    private void startServiceWithValues(int time, int restTime, boolean notificationTimer, boolean sound) {
        startService(new Intent(MainActivity.this, EyeService.class)
                .putExtra("sound", sound)
                .putExtra("notification", notificationTimer)
                .putExtra("rest_time", restTime)
                .putExtra("time", time)
        );
    }

    // Get saved values in order to update display everytime
    class GetSavedValues {

        private int time, restTime;

        private boolean notification, sound;

        public GetSavedValues(Context context) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            time = Integer.parseInt(preferences.getString("time", "20"));
            restTime = Integer.parseInt(preferences.getString("rest_time", "20"));
            notification = preferences.getBoolean("notification", true);
            sound = preferences.getBoolean("sound", false);
        }

        public int getTime() {
            return time;
        }

        public int getRestTime() {
            return restTime;
        }

        public boolean isNotification() {
            return notification;
        }

        public boolean isSound() {
            return sound;
        }

    }

    //TO CHANGE BG without LAG
    private BitmapDrawable toBitmpDrawable(int resDrawable) {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Bitmap bmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), resDrawable), size.x, size.y, true);
        return new BitmapDrawable(this.getResources(), bmp);
    }

    public void getInit() {
        layout = (RelativeLayout) findViewById(R.id.layout);
        circleMenu = (CircleMenu) findViewById(R.id.powerMenu);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        list = (RecyclerView) findViewById(R.id.left_drawer);
        //Prepare list
        list.setHasFixedSize(true);// has const size and im sure of it
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        list.setLayoutManager(mLayoutManager);
        list.setItemAnimator(new DefaultItemAnimator());
        //Drawer item
        Items[] items = {new Items(R.drawable.ic_build, getString(R.string.settings)),
                new Items(R.drawable.ic_about, getString(R.string.about)),
                new Items(R.drawable.ic_help, getString(R.string.how_to_use)),
                new Items(R.drawable.ic_power_2, getString(R.string.screen_filter)),
                new Items(R.drawable.ic_close, getString(R.string.stop_and_exit))
        };
        adapter = new ListAdapter(this, items);
        list.setAdapter(adapter);

        //Add toggle
        toggle = new ActionBarDrawerToggle(this, drawerLayout, addToolbar(), R.string.open_drawer, R.string.close_drawer) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                syncState();
                supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                syncState();
                supportInvalidateOptionsMenu();
            }
        };
        drawerLayout.addDrawerListener(toggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        toggle.syncState();
    }

    //Handle drawer itemclicks
    private void addListItemClickEvents() {

        list.addOnItemTouchListener(
                new RecyclerItemClickListener(this, list, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        drawerLayout.closeDrawer(Gravity.START);
                        //DO WHAT EVER
                        switch (position) {
                            case 0://Settings
                                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                                break;
                            case 1://About
                                startActivity(new Intent(MainActivity.this, AboutActivity.class));
                                break;
                            case 2:
                                startActivity(new Intent(MainActivity.this, HelpActivity.class));
                                break;
                            case 3:
                                startActivity(new Intent(MainActivity.this, FilterActivity.class));
                                break;
                            case 4:
                                onClickExitAndStop();
                                break;
                            default:
                                break;
                        }
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        //You can do nothing if you want
                    }
                })
        );
    }

    //It returns a toolbar because toggle botton uses it
    private Toolbar addToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/splash.ttf");
        TextView tv = (TextView) toolbar.findViewById(R.id.toolbarText);
        tv.setTypeface(face);
        return toolbar;
    }
}
