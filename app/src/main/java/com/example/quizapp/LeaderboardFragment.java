package com.example.quizapp;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardFragment extends Fragment {

    private FirebaseFirestore mFirestore;
    private RecyclerView LeaderBoard;
    private List<LBUsers> lbUsersList;
    private LBrecyclerAdapter lBrecyclerAdapter;
    private Dialog loading;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leaderboard , container , false);
        lbUsersList= new ArrayList<>();
        lBrecyclerAdapter= new LBrecyclerAdapter(lbUsersList);

        loading = new Dialog(getActivity());
        loading.setContentView(R.layout.loading_progressbar);
        loading.setCancelable(false);
        loading.getWindow().setBackgroundDrawableResource(R.drawable.progress_background);
        loading.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loading.show();



        LeaderBoard= (RecyclerView) view.findViewById(R.id.recyclerView);
        LeaderBoard.setAdapter(lBrecyclerAdapter);
        LeaderBoard.setHasFixedSize(true);
        LeaderBoard.setLayoutManager(new LinearLayoutManager(view.getContext()));



        mFirestore= FirebaseFirestore.getInstance();
        Query leader= mFirestore.collection("users").orderBy("full_score", Query.Direction.DESCENDING);
        leader.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if(e!=null){
                    Log.i("FireStore", "onEvent: Error");
                }
                for(QueryDocumentSnapshot doc:queryDocumentSnapshots){
                    LBUsers lbUsers=doc.toObject(LBUsers.class);
                    if(lbUsers.getFull_score()!=0){
                        lbUsersList.add(lbUsers);
                        lBrecyclerAdapter.notifyDataSetChanged();
                    }

                }

                loading.cancel();
            }
        });
        return view;
    }




}
