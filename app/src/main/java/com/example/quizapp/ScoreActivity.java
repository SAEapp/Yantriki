package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity {
    private TextView score;
    private Button donebtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        score = findViewById(R.id.score);
        donebtn = findViewById(R.id.sa_done);

        Bundle bundle = getIntent().getExtras();
        int scores = bundle.getInt("Score");
        int totalq =  bundle.getInt("TotalQ");
        score.setText(String.valueOf(scores) + "/" + String.valueOf(totalq) );

        donebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScoreActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}