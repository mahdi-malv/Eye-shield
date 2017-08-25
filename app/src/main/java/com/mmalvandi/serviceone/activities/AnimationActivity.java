package com.mmalvandi.serviceone.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.airbnb.lottie.LottieAnimationView;
import com.mmalvandi.serviceone.R;
import com.mmalvandi.serviceone.font.Tools;

public class AnimationActivity extends AppCompatActivity implements View.OnClickListener {

    //TODO add animation for flipper
    ViewFlipper startFlipper;
    LottieAnimationView[] animationViews = new LottieAnimationView[4];
    LottieAnimationView next1, next2, next3;
    TextView gotToMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getInit();
        next1.setOnClickListener(this);
        next2.setOnClickListener(this);
        next3.setOnClickListener(this);
        gotToMain.setOnClickListener(this);
        Tools.setFont(this, R.id.textView1, "tenderness");
        Tools.setFont(this, R.id.textView2, "tenderness");
        Tools.setFont(this, R.id.textView3, "tenderness");
        Tools.setFont(this, R.id.textView4, "tenderness");

    }

    private void getInit() {
        startFlipper = (ViewFlipper) findViewById(R.id.startFlipper);
        animationViews[0] = (LottieAnimationView) findViewById(R.id.gradientAnim);
        animationViews[0].setAnimation("gradient.json");
        animationViews[1] = (LottieAnimationView) findViewById(R.id.sickVRAnim);
        animationViews[1].setAnimation("vr_sickness.json");
        animationViews[2] = (LottieAnimationView) findViewById(R.id.timeAnim);
        animationViews[2].setAnimation("clock.json");
        animationViews[3] = (LottieAnimationView) findViewById(R.id.eyeMovingAnim);
        animationViews[3].setAnimation("last_flipper.json");
        for (LottieAnimationView l : animationViews) l.loop(true);
        startFlipper.setDisplayedChild(0);
        animationViews[0].playAnimation();

        next1 = (LottieAnimationView) findViewById(R.id.next1);
        next2 = (LottieAnimationView) findViewById(R.id.next2);
        next3 = (LottieAnimationView) findViewById(R.id.next3);
        gotToMain = (TextView) findViewById(R.id.gotIt);
    }

    @Override
    public void onBackPressed() {
        switch (startFlipper.getDisplayedChild()) {
            case 0: {
                finish();
                break;
            }
            case 1: {
                changeFlipperAndAnimation(1, false);// Back to first
                break;
            }
            case 2: {
                changeFlipperAndAnimation(2, false);// Back to second
                break;
            }
            case 3: {
                changeFlipperAndAnimation(3, false);// Back to third
                break;
            }
        }
    }


    private void changeFlipperAndAnimation(int displayedChild, boolean isGoingForward) {
        int a = isGoingForward ? 1 : -1;
        animationViews[displayedChild].cancelAnimation();
        if (isGoingForward) {
            startFlipper.setInAnimation(this, R.anim.anim_in_next);
            startFlipper.setOutAnimation(this, R.anim.anim_out_next);
        } else {
            startFlipper.setInAnimation(this, R.anim.anim_in_prev);
            startFlipper.setOutAnimation(this, R.anim.anim_out_prev);
        }
        startFlipper.setDisplayedChild(displayedChild + a);
        animationViews[displayedChild + a].playAnimation();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next1: {
                changeFlipperAndAnimation(0, true);
                break;
            }
            case R.id.next2: {
                changeFlipperAndAnimation(1, true);
                break;
            }
            case R.id.next3: {
                changeFlipperAndAnimation(2, true);
                break;
            }
            case R.id.gotIt: {
                finish();
                startActivity(new Intent(AnimationActivity.this, MainActivity.class));
                break;
            }
            default: break;
        }
    }
}
