package com.example.quizapp;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
    private RecyclerView LeaderBoard1,LeaderBoard2,LeaderBoard3;
    private List<LBUsers> lbUsersList1;
    private List<LBUsers2> lbUsersList2;
    private List<LBUsers3> lbUsersList3;
    private LBrecyclerAdapter lBrecyclerAdapter1;
    private LBrecyclerAdapter2 lBrecyclerAdapter2;
    private LBrecyclerAdapter3 lBrecyclerAdapter3;
    private Dialog loading;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        lbUsersList1 = new ArrayList<>();
        lbUsersList2 = new ArrayList<>();
        lbUsersList3 = new ArrayList<>();
        lBrecyclerAdapter1 = new LBrecyclerAdapter(lbUsersList1);
        lBrecyclerAdapter2 = new LBrecyclerAdapter2(lbUsersList2);
        lBrecyclerAdapter3 = new LBrecyclerAdapter3(lbUsersList3);

        loading = new Dialog(getActivity());
        loading.setContentView(R.layout.loading_progressbar);
        loading.setCancelable(false);
        loading.getWindow().setBackgroundDrawableResource(R.drawable.progress_background);
        loading.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loading.show();


        LeaderBoard1 = view.findViewById(R.id.recyclerView1);
        LeaderBoard1.setAdapter(lBrecyclerAdapter1);
        LeaderBoard1.setHasFixedSize(true);
        LeaderBoard1.setLayoutManager(new LinearLayoutManager(view.getContext()));

        LeaderBoard2 = view.findViewById(R.id.recyclerView2);
        LeaderBoard2.setAdapter(lBrecyclerAdapter2);
        LeaderBoard2.setHasFixedSize(true);
        LeaderBoard2.setLayoutManager(new LinearLayoutManager(view.getContext()));

        LeaderBoard3 = view.findViewById(R.id.recyclerView3);
        LeaderBoard3.setAdapter(lBrecyclerAdapter3);
        LeaderBoard3.setHasFixedSize(true);
        LeaderBoard3.setLayoutManager(new LinearLayoutManager(view.getContext()));


        mFirestore = FirebaseFirestore.getInstance();
        Query leader1 = mFirestore.collection("users").orderBy("level1score", Query.Direction.DESCENDING);
        leader1.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.i("FireStore", "onEvent: Error");
                }
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    LBUsers lbUsers1 = doc.toObject(LBUsers.class);
                    if (lbUsers1.getLevel1score() != 0) {
                        lbUsersList1.add(lbUsers1);
                        lBrecyclerAdapter1.notifyDataSetChanged();
                    }

                }



//                loading.cancel();
            }
        });

        Query leader2 = mFirestore.collection("users").orderBy("level2score", Query.Direction.DESCENDING);
        leader2.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.i("FireStore", "onEvent: Error");
                }
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    LBUsers2 lbUsers2 = doc.toObject(LBUsers2.class);
                    if (lbUsers2.getLevel2score() != 0) {
                        lbUsersList2.add(lbUsers2);
                        lBrecyclerAdapter2.notifyDataSetChanged();
                    }

                }



//                loading.cancel();
            }
        });
        Query leader3 = mFirestore.collection("users").orderBy("level3score", Query.Direction.DESCENDING);
        leader3.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.i("FireStore", "onEvent: Error");
                }
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    LBUsers3 lbUsers3 = doc.toObject(LBUsers3.class);
                    if (lbUsers3.getLevel3score() != 0) {
                        lbUsersList3.add(lbUsers3);
                        lBrecyclerAdapter3.notifyDataSetChanged();
                    }

                }



                loading.cancel();
            }
        });
        return view;
    }


}
