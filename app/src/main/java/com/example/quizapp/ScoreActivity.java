package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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

import static com.example.quizapp.newSetActivity.levelid;
import static com.example.quizapp.QuestionsActivity.setId;

public class ScoreActivity extends AppCompatActivity {
    private TextView new_score,bestScore;
    private Button donebtn;
    private FirebaseFirestore db;
    private FirebaseAuth fAuth;
    private FirebaseUser user;
    private int fScore;
    private int totalq;
    private int high_score;
    private Dialog loading;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        new_score = findViewById(R.id.new_score);
        bestScore = findViewById(R.id.best_score);
        donebtn = findViewById(R.id.sa_done);

        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        user = fAuth.getCurrentUser();

        Bundle bundle = getIntent().getExtras();
        fScore = bundle.getInt("Score");
        totalq = bundle.getInt("TotalQ");
        new_score.setText("Score : "+ String.valueOf(fScore) + "/" + String.valueOf(totalq));

        loading = new Dialog(ScoreActivity.this);
        loading.setContentView(R.layout.loading_progressbar);
        loading.setCancelable(false);
        loading.getWindow().setBackgroundDrawableResource(R.drawable.progress_background);
        loading.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loading.show();
        new Thread() {
            public void run() {

                updatePrams();

            }
        }.start();
        loading.cancel();




        donebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ScoreActivity.this,MainActivity2.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                finish();
            }
        });
    }

    private void updatePrams() {
        final Map<String, Object> quizParams = new HashMap<>();
        quizParams.put("best_score", "0");
        quizParams.put("first_try", true);

        final DocumentReference documentReference1 = db.collection("users").document(userID).collection("quize_scores")
                .document(String.valueOf(levelid) + String.valueOf(setId));
        documentReference1.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot.exists()) {
                    if (fScore > Integer.parseInt(documentSnapshot.getString("best_score"))) {
                        high_score = fScore;
                        Map<String, Object> updatedParams = new HashMap<>();
                        updatedParams.put("best_score", String.valueOf(fScore));
                        updatedParams.put("first_try", false);
                        documentReference1.update(updatedParams).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                            }
                        });

                    } else {
                        high_score = Integer.parseInt(documentSnapshot.getString("best_score"));
                    }
                    bestScore.setText("Best : "+String.valueOf(high_score) + "/" + String.valueOf(totalq));
                } else {
                    db.collection("users").document(userID).collection("quize_scores")
                            .document(String.valueOf(levelid) + String.valueOf(setId)).set(quizParams);
                    Log.d("tag", "Created Document");
                    new Thread() {
                        public void run() {
                            // sleep(3000);
                            db.collection("users").document(userID)
                                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    int val = Integer.parseInt(String.valueOf(documentSnapshot.get("total_score"))) ;

                                    db.collection("users").document(userID).update("total_score",String.valueOf(val+fScore));
                                    db.collection("users").document(userID).update("full_score",val+fScore);
                                    Toast.makeText(ScoreActivity.this, "FULL SCORE UPDATED!", Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                    }.start();
                }
            }
        });
    }
}