package com.example.quizapp;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    public static List<String> levelsList = new ArrayList<>();
    private Button startbtn;
    private FirebaseFirestore firestore;
    private Dialog loading;
    private ImageView back;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ((MainActivity2) getActivity()).getSupportActionBar().setTitle("Home");
        startbtn = view.findViewById(R.id.start1);


        back = view.findViewById(R.id.fragHomeBack);
        back.setTranslationY(-100f);
        ObjectAnimator animation = ObjectAnimator.ofFloat(back, "translationY", 0f);
        animation.setDuration(1000);
        animation.start();

        AlphaAnimation alphaAnimation = new AlphaAnimation(0.5f, 1f);
        alphaAnimation.setDuration(1000);
        alphaAnimation.setStartOffset(0);
        alphaAnimation.setFillAfter(true);
        back.setAnimation(alphaAnimation);
        startbtn.setAnimation(alphaAnimation);


        loading = new Dialog(getActivity());
        loading.setContentView(R.layout.loading_progressbar);
        loading.setCancelable(false);
        loading.getWindow().setBackgroundDrawableResource(R.drawable.progress_background);
        loading.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        firestore = FirebaseFirestore.getInstance();

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

        return view;
    }

    private void loadData() {
        levelsList.clear();
        firestore.collection("Quizes").document("Levels")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();

                    if (doc.exists()) {
                        long count = (long) doc.get("count");


                        for (int i = 1; i <= count; i++) {
                            String levelName = doc.getString("level-" + i);
                            // String catID = doc.getString("CAT" + String.valueOf(i) + "_ID");

                            levelsList.add(levelName);
                        }
                        Intent intent = new Intent(getActivity(), levelCard.class);
                        startActivity(intent);

                    } else {
                        Toast.makeText(getActivity(), "No Levels Document Exists!", Toast.LENGTH_SHORT).show();
                    }
                    getActivity().finish();

                } else {

                    Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
                loading.cancel();
            }
        });
    }


}
