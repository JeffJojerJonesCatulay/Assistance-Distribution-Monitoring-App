package com.example.assistancedistributionmonitoringapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 5000;

    // Animation
    Animation topAnim, bottomAnim;

    // Images
    ImageView logo;

    // TextView
    TextView appName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        // Animation
        topAnim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.bottom_animation);

        // ImageView
        logo = findViewById(R.id.logo);

        //TextView
        appName =  findViewById(R.id.appName);

        // On Create Functions
        splashScreenAnimation();
    }

    public void splashScreenAnimation(){
        // Animation Effect
        logo.setAnimation(topAnim);
        appName.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, logIn.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_SCREEN);
    }
}