package com.example.quizapp;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText mFullName,mEmail,mPassword,mPhone;
    Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    String userID;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFullName=findViewById(R.id.fullName);
        mEmail=findViewById(R.id.email);
        mPassword=findViewById(R.id.password);
        mPhone=findViewById(R.id.phone);
        mRegisterBtn=findViewById(R.id.registerBtn);
        mLoginBtn=findViewById(R.id.createText);
        fAuth= FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        progressBar=findViewById(R.id.progressBar);
        imageView=findViewById(R.id.imageView);

        //Animation of the elements

        AlphaAnimation alphaAnimation= new AlphaAnimation(0.5f,1f);
        alphaAnimation.setDuration(1500);
        alphaAnimation.setStartOffset(0);
        alphaAnimation.setFillAfter(true);
        imageView.startAnimation(alphaAnimation);
        mFullName.startAnimation(alphaAnimation);
        mEmail.startAnimation(alphaAnimation);
        mPassword.startAnimation(alphaAnimation);
        mPhone.startAnimation(alphaAnimation);
        mRegisterBtn.startAnimation(alphaAnimation);
        mLoginBtn.startAnimation(alphaAnimation);

        imageView.setTranslationY(-600f);
        ObjectAnimator animation = ObjectAnimator.ofFloat(imageView, "translationY", 0f);
        animation.setDuration(2000);
        animation.start();

        if (fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity2.class));
            finish();
        }

        //creating user and adding data in firestore

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email=mEmail.getText().toString().trim();
                String password=mPassword.getText().toString().trim();
                final String fullName = mFullName.getText().toString();
                final String phone    = mPhone.getText().toString();

                if (TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required!");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    mPassword.setError("Password is Req");
                }
                if (password.length() < 6){
                    mPassword.setError("Password must be >= 6 Characters");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                //register the user in firebase
                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            fAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){

                                        Toast.makeText(Register.this, "User Created.", Toast.LENGTH_SHORT).show();
                                        userID = fAuth.getCurrentUser().getUid();
                                        DocumentReference documentReference = fStore.collection("users").document(userID);
                                        Map<String,Object> user = new HashMap<>();
                                        user.put("fName",fullName);
                                        user.put("email",email);
                                        user.put("phone",phone);
                                        user.put("total_score","0");
                                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG, "onSuccess: user Profile is created for "+ userID);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d(TAG, "onFailure: " + e.toString());
                                            }
                                        });
                                        leaderboard(userID,fullName);
                                        if(fAuth.getCurrentUser().isEmailVerified()){
                                        startActivity(new Intent(getApplicationContext(), MainActivity2.class));
                                        overridePendingTransition(android.R.anim.fade_in, R.anim.zoom);}
                                        else {

                                            Toast.makeText(Register.this, "Please verify your email", Toast.LENGTH_SHORT).show();

                                        }

                                    }
                                }
                            });

                            /*Toast.makeText(Register.this, "User Created.", Toast.LENGTH_SHORT).show();
                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("users").document(userID);
                            Map<String,Object> user = new HashMap<>();
                            user.put("fName",fullName);
                            user.put("email",email);
                            user.put("phone",phone);
                            user.put("total_score","0");
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: user Profile is created for "+ userID);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: " + e.toString());
                                }
                            });
                            leaderboard(userID,fullName);
                            startActivity(new Intent(getApplicationContext(), MainActivity2.class));
                            overridePendingTransition(android.R.anim.fade_in, R.anim.zoom);*/
                        }else{
                            Toast.makeText(Register.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });
    }

    //adding data for leaderboard

    public void leaderboard(String UserID, String fname){

        DocumentReference Reference = fStore.collection("ScoreBoard").document(userID);
        HashMap<String, Object> LBuser= new HashMap<>();
        LBuser.put("name", fname);
        int userscore=0;
        LBuser.put("scorenum", userscore);
        LBuser.put("score", Integer.toString(userscore));
        Reference.set(LBuser).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i("LeaderBoard Data", "onSuccess: DataUpdated");
            }

        });

    }
}