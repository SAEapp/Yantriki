package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private Button startbtn,logoutbtn,profilebtn,temp;
    private FirebaseFirestore firestore;
    public static List<String> levelsList = new ArrayList<>();
    private Dialog loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startbtn = findViewById(R.id.start);
        logoutbtn = findViewById(R.id.button);
        profilebtn = findViewById(R.id.profile_button);
        temp = findViewById(R.id.temp);

        temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SettingsActivity.class));
            }
        });

        Button leaderboard= findViewById(R.id.leaderboard);
        leaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LeaderBoard.class));
            }
        });

        loading = new Dialog(MainActivity.this);
        loading.setContentView(R.layout.loading_progressbar);
        loading.setCancelable(false);
        Objects.requireNonNull(loading.getWindow()).setBackgroundDrawableResource(R.drawable.progress_background);
        loading.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        
        firestore = FirebaseFirestore.getInstance();

        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout(v);
                /*Intent intent = new Intent(MainActivity.this,MainActivity2.class);
                startActivity(intent);*/
            }
        });

        startbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.show();
                new Thread() {
                    public void run() {
                        // sleep(3000);

                        loadData();


                    }
                }.start();

            }
        });

        profilebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this,Profile.class);
                startActivity(intent);

            }
        });
    }

    private void loadData() {
        levelsList.clear();
        firestore.collection("Quizes").document("Levels")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful())
                {
                    DocumentSnapshot doc = task.getResult();

                    assert doc != null;
                    if(doc.exists())
                    {
                        long count = (long)doc.get("count");

                        for(int i=1; i <= count; i++)
                        {
                            String levelName = doc.getString("level-" + i);
                           // String catID = doc.getString("CAT" + String.valueOf(i) + "_ID");

                            levelsList.add(levelName);
                        }
                        Intent intent = new Intent(MainActivity.this,levelCard.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this,"No Levels Document Exists!",Toast.LENGTH_SHORT).show();
                        finish();
                    }

                }
                else
                {

                    Toast.makeText(MainActivity.this, Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_SHORT).show();
                }
                loading.cancel();
            }
        });
    }

    public void logout(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }
}