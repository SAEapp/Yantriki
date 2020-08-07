package com.example.quizapp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private Button startbtn;
//    private Button logoutbtn,profilebtn;
    private FirebaseFirestore firestore;
    public static List<String> levelsList = new ArrayList<>();
    private Dialog loading;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home , container , false);

        startbtn = view.findViewById(R.id.start1);
//        logoutbtn = view.findViewById(R.id.button1);
//        profilebtn = view.findViewById(R.id.profile_button1);

//        Button leaderboard= view.findViewById(R.id.leaderboard1);
//        leaderboard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getActivity(), LeaderBoard.class));
//            }
//        });

        loading = new Dialog(getActivity());
        loading.setContentView(R.layout.loading_progressbar);
        loading.setCancelable(false);
        loading.getWindow().setBackgroundDrawableResource(R.drawable.progress_background);
        loading.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);


        firestore = FirebaseFirestore.getInstance();

//        logoutbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                logout(v);
////                Intent intent = new Intent(getActivity(),MainActivity2.class);
////                startActivity(intent);
//            }
//        });

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

//        profilebtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(getActivity(),Profile.class);
//                startActivity(intent);
//
//            }
//        });
        return view;
    }

//    public void logout(View view){
//        FirebaseAuth.getInstance().signOut();
//        startActivity(new Intent(getActivity(),Login.class));
//        getActivity().finish();
//    }
    private void loadData() {
        levelsList.clear();
        firestore.collection("Quizes").document("Levels")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful())
                {
                    DocumentSnapshot doc = task.getResult();

                    if(doc.exists())
                    {
                        long count = (long)doc.get("count");

                        for(int i=1; i <= count; i++)
                        {
                            String levelName = doc.getString("level-" + String.valueOf(i));
                            // String catID = doc.getString("CAT" + String.valueOf(i) + "_ID");

                            levelsList.add(levelName);
                        }
                        Intent intent = new Intent(getActivity(),levelCard.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(getActivity(),"No Levels Document Exists!",Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                    }

                }
                else
                {

                    Toast.makeText(getActivity(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
                loading.cancel();
            }
        });
    }



}
