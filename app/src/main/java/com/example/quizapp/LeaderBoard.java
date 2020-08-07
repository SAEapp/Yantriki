package com.example.quizapp;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class LeaderBoard extends AppCompatActivity {

    private FirebaseFirestore mFirestore;
    private RecyclerView LeaderBoard;
    private List<LBUsers> lbUsersList;
    private LBrecyclerAdapter lBrecyclerAdapter;
    private Dialog loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);
        lbUsersList= new ArrayList<>();
        lBrecyclerAdapter= new LBrecyclerAdapter(lbUsersList);

        loading = new Dialog(LeaderBoard.this);
        loading.setContentView(R.layout.loading_progressbar);
        loading.setCancelable(false);
        loading.getWindow().setBackgroundDrawableResource(R.drawable.progress_background);
        loading.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loading.show();



        LeaderBoard= (RecyclerView) findViewById(R.id.recyclerView);
        LeaderBoard.setAdapter(lBrecyclerAdapter);
        LeaderBoard.setHasFixedSize(true);
        LeaderBoard.setLayoutManager(new LinearLayoutManager(this));



        mFirestore= FirebaseFirestore.getInstance();
        Query leader= mFirestore.collection("users").orderBy("total_score", Query.Direction.DESCENDING);
        leader.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(e!=null){
                    Log.i("FireStore", "onEvent: Error");
                }
                for(QueryDocumentSnapshot doc:queryDocumentSnapshots){

                    LBUsers lbUsers=doc.toObject(LBUsers.class);
                    lbUsersList.add(lbUsers);
                    lBrecyclerAdapter.notifyDataSetChanged();


                }
                loading.cancel();
            }
        });



    }
}