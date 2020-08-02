package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SetsActivity extends AppCompatActivity {
    private GridView setGrid;
    private FirebaseFirestore firestore ;
    private String levelName;
    public static int level_id;
    private Dialog loading;
    public static long time_lim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sets);

        Bundle bundle = getIntent().getExtras();
        levelName = bundle.getString("Level");
        level_id = bundle.getInt("Level_ID",1);

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(levelName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //for back arrow

        setGrid = findViewById(R.id.sets_gridview);

        loading = new Dialog(SetsActivity.this);
        loading.setContentView(R.layout.loading_progressbar);
        loading.setCancelable(false);
        loading.getWindow().setBackgroundDrawableResource(R.drawable.progress_background);
        loading.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loading.show();


        firestore = FirebaseFirestore.getInstance();
        loadSets();


    }

    private void loadSets() {
        firestore.collection("Quizes").document("Level-"+ String.valueOf(level_id))
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful())
                {
                    DocumentSnapshot doc = task.getResult();

                    if(doc.exists())
                    {
                        long sets = (long)doc.get("sets");
                        time_lim = (long)doc.get("time_limit");

                        SetsAdapter adapter = new SetsAdapter((int)sets);
                        setGrid.setAdapter(adapter);


                    }
                    else
                    {
                        Toast.makeText(SetsActivity.this,"No Level Document Exists!",Toast.LENGTH_SHORT).show();
                        finish();
                    }

                }
                else
                {

                    Toast.makeText(SetsActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }

                loading.cancel();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home)
        {
            SetsActivity.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}