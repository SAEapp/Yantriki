package com.example.quizapp;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AboutAppActivity extends AppCompatActivity {

    private CardView cardAbout;
    private ImageView saelogo, appicon, twitter, instagram, linkedin, facebook;
    private TextView AboutHead, AboutBody1, AboutBody2, DevHead;
    private LinearLayout DevBody, imageLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
        cardAbout=findViewById(R.id.cardAbout);
        saelogo=findViewById(R.id.saelogo);
        appicon=findViewById(R.id.appicon);

        twitter=findViewById(R.id.twitterImage);
        instagram=findViewById(R.id.instaImage);
        facebook=findViewById(R.id.faceImage);
        linkedin=findViewById(R.id.linkImage);

        DevBody=findViewById(R.id.bodyDevelopers);
        AboutHead=findViewById(R.id.headAbout);
        AboutBody1=findViewById(R.id.bodyAbout1);
        AboutBody2=findViewById(R.id.aboutBody2);
        DevHead=findViewById(R.id.headDevelopers);

        imageLink=findViewById(R.id.imageLink);




        final AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
        alphaAnimation.setDuration(1500);
        alphaAnimation.setStartOffset(0);
        alphaAnimation.setFillAfter(true);
        appicon.setAnimation(alphaAnimation);

        final AlphaAnimation alphaAnimation1= new AlphaAnimation(0f, 1f);
        alphaAnimation1.setDuration(1000);
        alphaAnimation1.setStartOffset(0);
        alphaAnimation1.setFillAfter(true);




        Handler handler= new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                cardAbout.setVisibility(View.VISIBLE);
                saelogo.setVisibility(View.INVISIBLE);

                final AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
                alphaAnimation.setDuration(1500);
                alphaAnimation.setStartOffset(0);
                alphaAnimation.setFillAfter(true);
                cardAbout.setAnimation(alphaAnimation);


                saelogo.setTranslationY(600f);
                ObjectAnimator animation = ObjectAnimator.ofFloat(saelogo, "translationY", 0f);
                animation.setDuration(2000);
                animation.start();

                cardAbout.setTranslationY(600f);
                ObjectAnimator Aboutanim = ObjectAnimator.ofFloat(cardAbout, "translationY", 0f);
                Aboutanim.setDuration(2000);
                Aboutanim.start();

                Handler contentHand= new Handler();
                contentHand.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //AboutBody2.setVisibility(View.VISIBLE);
                        //AboutBody1.setVisibility(View.VISIBLE);
                        //DevBody.setVisibility(View.VISIBLE);
                        AboutBody1.setAnimation(alphaAnimation1);
                        AboutBody2.setAnimation(alphaAnimation1);
                        DevBody.setAnimation(alphaAnimation1);
                    }
                }, 3000);

                Handler mhandler= new Handler();
                mhandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AlphaAnimation mAnimation = new AlphaAnimation(0f, 1f);
                        mAnimation.setDuration(1000);
                        mAnimation.setStartOffset(0);
                        mAnimation.setFillAfter(true);
                        saelogo.setAnimation(mAnimation);
                    }
                }, 500);

                Handler bhandler= new Handler();
                bhandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        AboutHead.setTranslationX(-50f);
                        ObjectAnimator AHanim = ObjectAnimator.ofFloat(AboutHead, "translationX", 0f);
                        AHanim.setDuration(1000);
                        AHanim.start();
                        AboutHead.startAnimation(alphaAnimation);

                        DevHead.setTranslationX(-50f);
                        ObjectAnimator DHanim= ObjectAnimator.ofFloat(DevHead, "translationX", 0f);
                        DHanim.setDuration(1000);
                        DHanim.start();
                        DevHead.startAnimation(alphaAnimation);




                    }
                }, 2500);

            }
        }, 1500);

    instagram.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent= new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);

            intent.setData(Uri.parse("https://www.instagram.com/sae_club_mnnit"));

            startActivity(intent);
        }
    });

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);

                intent.setData(Uri.parse("https://www.facebook.com/saeclubmnnit"));

                startActivity(intent);
            }
        });

        linkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);

                intent.setData(Uri.parse("https://in.linkedin.com/company/sae-india-collegiate-club-of-mnnit"));

                startActivity(intent);
            }
        });

        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);

                intent.setData(Uri.parse("https://twitter.com/team_Solarec"));

                startActivity(intent);
            }
        });


    }



    @Override
    public void onBackPressed() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        super.onBackPressed();
    }
}