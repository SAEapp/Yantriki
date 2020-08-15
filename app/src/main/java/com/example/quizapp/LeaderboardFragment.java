package com.example.quizapp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;

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
    private Button beginner, Intermediate, Advanced;
    private Dialog loading;
    private AlphaAnimation alphaAnimation, outanim;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        lbUsersList1 = new ArrayList<>();
        lbUsersList2 = new ArrayList<>();
        lbUsersList3 = new ArrayList<>();
        lBrecyclerAdapter1 = new LBrecyclerAdapter(lbUsersList1);
        lBrecyclerAdapter2 = new LBrecyclerAdapter2(lbUsersList2);
        lBrecyclerAdapter3 = new LBrecyclerAdapter3(lbUsersList3);

        beginner=view.findViewById(R.id.Begibtn);
        Intermediate=view.findViewById(R.id.Intebtn);
        Advanced=view.findViewById(R.id.Advabtn);

        alphaAnimation = new AlphaAnimation(0f, 1f);
        alphaAnimation.setDuration(1000);
        alphaAnimation.setStartOffset(0);
        alphaAnimation.setFillAfter(false);

        outanim= new AlphaAnimation(1f, 0f);
        outanim.setDuration(1000);
        outanim.setFillAfter(false);
        outanim.setStartOffset(0);


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


        beginner.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if(LeaderBoard1.getVisibility()==View.VISIBLE){

                }
                else {
                    beginner.setBackground(getResources().getDrawable(R.drawable.button));
                    beginner.setTextColor(Color.parseColor("#FFFFFF"));

                    Intermediate.setBackground(getResources().getDrawable(R.drawable.whitecolor));
                    Intermediate.setTextColor(R.color.Black);

                    Advanced.setBackground(getResources().getDrawable(R.drawable.whitecolor));
                    Advanced.setTextColor(R.color.Black);


                    if(LeaderBoard2.getVisibility()==View.VISIBLE){
                        LeaderBoard2.setAnimation(outanim);
                        outanim.start();
                        LeaderBoard2.setVisibility(View.INVISIBLE);
                    }
                    if(LeaderBoard3.getVisibility()==View.VISIBLE){
                        LeaderBoard3.setAnimation(outanim);
                        outanim.start();
                        LeaderBoard3.setVisibility(View.INVISIBLE);
                    }
                    Handler handler= new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            LeaderBoard1.setAnimation(alphaAnimation);
                            alphaAnimation.start();
                            LeaderBoard1.setVisibility(View.VISIBLE);

                        }
                    },1000);

                }
            }
        });

        Intermediate.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {

                if(LeaderBoard2.getVisibility()==View.VISIBLE){

                }
                else {
                    Intermediate.setBackground(getResources().getDrawable(R.drawable.button));
                    Intermediate.setTextColor(Color.parseColor("#FFFFFF"));

                    beginner.setBackground(getResources().getDrawable(R.drawable.whitecolor));
                    beginner.setTextColor(R.color.Black);

                    Advanced.setBackground(getResources().getDrawable(R.drawable.whitecolor));
                    Advanced.setTextColor(R.color.Black);


                    if(LeaderBoard1.getVisibility()==View.VISIBLE){
                        LeaderBoard1.setAnimation(outanim);
                        outanim.start();
                        LeaderBoard1.setVisibility(View.INVISIBLE);
                    }
                    if(LeaderBoard3.getVisibility()==View.VISIBLE){
                        LeaderBoard3.setAnimation(outanim);
                        outanim.start();
                        LeaderBoard3.setVisibility(View.INVISIBLE);
                    }
                    Handler handler= new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            LeaderBoard2.setAnimation(alphaAnimation);
                            alphaAnimation.start();
                            LeaderBoard2.setVisibility(View.VISIBLE);

                        }
                    },1000);

                }
            }
        });

        Advanced.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if(LeaderBoard3.getVisibility()==View.VISIBLE){

                }
                else {
                    Advanced.setBackground(getResources().getDrawable(R.drawable.button));
                    Advanced.setTextColor(Color.parseColor("#FFFFFF"));

                    beginner.setBackground(getResources().getDrawable(R.drawable.whitecolor));
                    beginner.setTextColor(R.color.Black);

                    Intermediate.setBackground(getResources().getDrawable(R.drawable.whitecolor));
                    Intermediate.setTextColor(R.color.Black);


                    if(LeaderBoard1.getVisibility()==View.VISIBLE){
                        LeaderBoard1.setAnimation(outanim);
                        outanim.start();
                        LeaderBoard1.setVisibility(View.INVISIBLE);
                    }
                    if(LeaderBoard2.getVisibility()==View.VISIBLE){
                        LeaderBoard2.setAnimation(outanim);
                        outanim.start();
                        LeaderBoard2.setVisibility(View.INVISIBLE);
                    }
                    Handler handler= new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            LeaderBoard3.setAnimation(alphaAnimation);
                            alphaAnimation.start();
                            LeaderBoard3.setVisibility(View.VISIBLE);


                        }
                    },1000);

                }
            }
        });



        return view;
    }


}
