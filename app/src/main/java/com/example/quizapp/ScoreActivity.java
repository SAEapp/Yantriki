package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.contentcapture.DataRemovalRequest;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ScoreActivity extends AppCompatActivity {
    private TextView score;
    private Button donebtn;
    private FirebaseFirestore db;
    private FirebaseAuth fAuth;
    private Integer Databasescore;
    private String userID,datascore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        db=FirebaseFirestore.getInstance();

        score = findViewById(R.id.score);
        donebtn = findViewById(R.id.sa_done);

        fAuth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();

        Bundle bundle = getIntent().getExtras();
        final int scores = bundle.getInt("Score");
        int totalq =  bundle.getInt("TotalQ");
        score.setText(String.valueOf(scores) + "/" + String.valueOf(totalq) );
        Map<String,Object> user=new HashMap<>();
         userID = fAuth.getCurrentUser().getUid();


        donebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                db.collection("LeaderBoard").document(userID).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){
                                    DocumentSnapshot document= task.getResult();
                                    if(document.exists()){
                                        Databasescore= (Integer) document.get("scorenum");
                                        Log.i("Retrieving score", "onComplete:"+datascore);
                                    }
                                }
                            }
                        });

                Databasescore=Databasescore+scores;
                HashMap<String, Object> userScore= new HashMap<>();
                userScore.put("score", Databasescore.toString());
                userScore.put("scorenum", Databasescore);
                db.collection("LeaderBoard").document(userID).set(userScore);

                Intent intent = new Intent(ScoreActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}