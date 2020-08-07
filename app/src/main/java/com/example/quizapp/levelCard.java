package com.example.quizapp;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class levelCard extends AppCompatActivity {

    ViewPager viewPager;
    Adapter adapter;
    Integer[] colors= null;
    List<CardModel> cardModels;
    ArgbEvaluator argbEvaluator;
    private FirebaseAuth auth;
    private Object cardAdapter;

    public void onStart(){

        super.onStart();

        FirebaseUser currentuser=auth.getCurrentUser();
        if(currentuser==null){

            startActivity(new Intent(levelCard.this, Login.class));

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardlevel);

        final Button start= (Button) findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(levelCard.this, newSetActivity.class);
                intent.putExtra("Level", "Beginner");
                intent.putExtra("Level_ID", 1);
                startActivity(intent);
            }
        });



        auth= FirebaseAuth.getInstance();


        cardModels= new ArrayList<>();
        cardModels.add(new CardModel(R.mipmap.beginner, "Beginner", "For people new to AutoMobile"));
        cardModels.add(new CardModel(R.mipmap.intermediate, "Intermediate", "Your Knowledge will be tested"));
        cardModels.add(new CardModel(R.mipmap.advanced, "Advanced", "Your knowledge will be strained"));


        cardAdapter= new cardAdapter(cardModels, this);


        viewPager= findViewById(R.id.viewPager);
        viewPager.setAdapter((PagerAdapter) cardAdapter);
        viewPager.setPadding(130,500,130,0);

        Integer[] colors_temp= {getResources().getColor(R.color.color1),
                getResources().getColor(R.color.color2),
                getResources().getColor(R.color.color3)};
        colors=colors_temp;

        final Drawable[] drawables={getDrawable(R.mipmap.levelone), getDrawable(R.mipmap.leveltwo), getDrawable(R.mipmap.levelthree)};

        argbEvaluator= new ArgbEvaluator();

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position<(((PagerAdapter) cardAdapter).getCount()-1)&& position<(drawables.length-1)){

                    //viewPager.setBackgroundColor((Integer) argbEvaluator.evaluate(positionOffset,colors[position], colors[position+1]));

                    //viewPager.setBackground(drawables[position]);

                }
                else {
                    //viewPager.setBackground(drawables[drawables.length-1]);
                }
            }

            @Override
            public void onPageSelected(final int position) {
                start.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i= new Intent(levelCard.this, newSetActivity.class);
                        if(position==0) {
                            i.putExtra("Level", "Beginner");
                            i.putExtra("Level_ID", position+1);
                        }
                        if(position==1){
                            i.putExtra("Level", "Intermediate");
                            i.putExtra("Level_ID", position+1);
                        }
                        if(position==2){
                            i.putExtra("Level", "Advanced");
                            i.putExtra("Level_ID", position+1);
                        }
                        startActivity(i);
                    }
                });
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }



}