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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nullable;

import static com.example.quizapp.LevellGridAdapter.levelid;
import static com.example.quizapp.SetsAdapter.setId;

public class ScoreActivity extends AppCompatActivity {
    private TextView score;
    private Button donebtn;
    private FirebaseFirestore db;
    private FirebaseAuth fAuth;
    private FirebaseUser user;
    private int fScore;
    private int total_score;
    private int totalq;
    private String high_score;
    private boolean is_first_try;
//    private Integer Databasescore;
    private String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        score = findViewById(R.id.new_score);
        donebtn = findViewById(R.id.sa_done);

        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        user = fAuth.getCurrentUser();

        Bundle bundle = getIntent().getExtras();
        fScore = bundle.getInt("Score");
        totalq = bundle.getInt("TotalQ");
        score.setText(String.valueOf(fScore) + "/" + String.valueOf(totalq));
        final Map<String, Object> quizParams = new HashMap<>();
        quizParams.put("best_score", "0");
        quizParams.put("first_try", "1");

        final DocumentReference documentReference1 = db.collection("users").document(userID).collection("quize_scores")
                .document(String.valueOf(levelid) + String.valueOf(setId));
        documentReference1.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot.exists()) {
                    if (fScore > Integer.parseInt(documentSnapshot.getString("best_score"))) {
                        Map<String, Object> updatedParams = new HashMap<>();
                        updatedParams.put("best_score", String.valueOf(fScore));
                        updatedParams.put("first_try", "0");
                        documentReference1.update(updatedParams).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(ScoreActivity.this, "Params Updated", Toast.LENGTH_SHORT).show();
                            }
                        });
                        is_first_try = false;
                        high_score = documentSnapshot.getString("best_score");
                    }
                } else {
                    db.collection("users").document(userID).collection("quize_scores")
                            .document(String.valueOf(levelid) + String.valueOf(setId)).set(quizParams);
                    is_first_try = true;
                    Log.d("tag", "Created Document");
                }
            }
        });
        if(is_first_try) {
        final DocumentReference documentReference = db.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot.exists()) {
                    total_score = Integer.parseInt(documentSnapshot.getString("total_score"));
                    total_score += fScore;
                    Map<String, Object> updated = new HashMap<>();
                    updated.put("fName", documentSnapshot.getString("fName"));
                    updated.put("email", documentSnapshot.getString("email"));
                    updated.put("phone", documentSnapshot.getString("phone"));
                    updated.put("total_score", String.valueOf(total_score));
                    documentReference.update(updated).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(ScoreActivity.this, "Score Updated", Toast.LENGTH_SHORT).show();
                        }
                    });


                } else {
                    Log.d("tag", "onEvent: Document does not exists");
                }
            }
        });
    }







//        donebtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                db.collection("LeaderBoard").document(userID).get()
//                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                            @Override
//                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                if(task.isSuccessful()){
//                                    DocumentSnapshot document= task.getResult();
//                                    if(document.exists()){
//                                        Databasescore= (Integer) document.get("scorenum");
//                                        Log.i("Retrieving score", "onComplete:"+datascore);
//                                    }
//                                }
//                            }
//                        });
//
//                Databasescore=Databasescore+scores;
//                HashMap<String, Object> userScore= new HashMap<>();
//                userScore.put("score", Databasescore.toString());
//                userScore.put("scorenum", Databasescore);
//                db.collection("LeaderBoard").document(userID).set(userScore);
//
//                Intent intent = new Intent(ScoreActivity.this,MainActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });

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