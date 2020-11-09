package com.mobileemail_register.SPLASH_SCREEN_ACTIVITY;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;
import com.mobileemail_register.LOGIN_ACTIVITY.LoginPage_Activity;
import com.mobileemail_register.R;



public class SplashScreen_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        AppCenter.start(getApplication(), "15ee0d52-7eb0-4aa2-9ec5-40b1491add0e",
                Analytics.class, Crashes.class);

        ImageView image_splash_screen = findViewById(R.id.image_splash_screen);
        TextView textViewVersion = findViewById(R.id.textViewVersion);

        final Animation anim = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fade_in);
        image_splash_screen.startAnimation(anim);
        textViewVersion.startAnimation(anim);


        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {


                Intent intent = new Intent(SplashScreen_Activity.this, LoginPage_Activity.class);
                startActivity(intent);
                finishAffinity();


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }



}