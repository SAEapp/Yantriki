package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import static com.example.quizapp.MainActivity.levelsList;

//import static com.example.quizapp.MainActivity.levelsList;


public class LevelsActivity extends AppCompatActivity {
    private GridView level_grid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Levels");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //for back arrow

        level_grid = findViewById(R.id.levelGrid);
        //Adapter for Grid View
//        List<String> levelList = new ArrayList<>();
//        levelList.add("Basic");
//        levelList.add("Intermediate");
//        levelList.add("Hard");

        LevellGridAdapter adapter = new LevellGridAdapter(levelsList);
        level_grid.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home)
        {
            LevelsActivity.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}