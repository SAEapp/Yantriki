package com.example.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        imageView=findViewById(R.id.appicon);
        AlphaAnimation alphaAnimation= new AlphaAnimation(0f,1f);
        alphaAnimation.setDuration(2000);
        alphaAnimation.setStartOffset(0);
        alphaAnimation.setFillAfter(true);
        imageView.startAnimation(alphaAnimation);
        Handler handler= new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreen.this, Register.class));
                overridePendingTransition(android.R.anim.fade_in, R.anim.splashscreen);
                finish();
            }
        }, 1500);

    }
}