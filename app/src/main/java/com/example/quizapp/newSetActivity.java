package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class newSetActivity extends AppCompatActivity implements NsetsAdapter.OnNoteListener{

    private String levelName;
    public static int levelid;
    private RecyclerView setRecycler;
    private FirebaseFirestore mFirestore;
    private Dialog loading;
    private List<setslist> sets;
    private NsetsAdapter Adapter;
    public static long time_lim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_set);

        Toast.makeText(getApplicationContext(), "newSet", Toast.LENGTH_LONG).show();

        Bundle bundle = getIntent().getExtras();
        levelName = bundle.getString("Level");
        levelid = bundle.getInt("Level_ID",1);

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(levelName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //for back arrow
        setRecycler= findViewById(R.id.setRecycler);

        loading = new Dialog(newSetActivity.this);
        loading.setContentView(R.layout.loading_progressbar);
        loading.setCancelable(false);
        loading.getWindow().setBackgroundDrawableResource(R.drawable.progress_background);
        loading.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loading.show();

        sets= new ArrayList<>();
        Adapter= new NsetsAdapter(sets, this);

        setRecycler.setAdapter(Adapter);
        setRecycler.setHasFixedSize(true);
        setRecycler.setLayoutManager(new LinearLayoutManager(this));



        mFirestore=FirebaseFirestore.getInstance();

        mFirestore.collection("Quizes").document("Level-"+ String.valueOf(levelid))
                .collection("sets").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for(QueryDocumentSnapshot doc: value){

                    Log.i("dataRecieveCHeck", "onEvent:"+value.size());

                    setslist set=doc.toObject(setslist.class);
                    sets.add(set);
                    Adapter.notifyDataSetChanged();


                }
                loading.cancel();
            }
        });

        mFirestore.collection("Quizes").document("Level-"+ String.valueOf(levelid))
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful())
                {
                    DocumentSnapshot doc = task.getResult();

                    if(doc.exists())
                    {

                        time_lim = (long)doc.get("time_limit");

                    }
                    else
                    {
                        Toast.makeText(newSetActivity.this,"No Level Document Exists!",Toast.LENGTH_SHORT).show();
                        finish();
                    }

                }
                else
                {

                    Toast.makeText(newSetActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }

                loading.cancel();
            }
        });





    }

    @Override
    public void onNoteClick(int position) {
        Intent intent = new Intent(newSetActivity.this,QuestionsActivity.class);
        Log.i("QuestionIntent", "onNoteClick:"+position);
        intent.putExtra("set_id",position+1);
        intent.putExtra("level",levelid);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(newSetActivity.this,levelCard.class);
        startActivity(intent);
        finish();
    }

}