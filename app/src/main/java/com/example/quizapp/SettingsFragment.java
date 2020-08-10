package com.example.quizapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import static com.example.quizapp.MainActivity2.pushNotificationState;
import static com.example.quizapp.MainActivity2.soundState;
import static com.example.quizapp.MainActivity2.vibrationState;

public class SettingsFragment extends Fragment {

    private FirebaseUser user;
    private String userId, phone, fullName, email;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private Button log_outbtn, editProfile, deleteAccount, about;
    public static SwitchCompat soundEffects, vibrations, pushNotif;
    private Dialog waiting;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String SWITCH1 = "switch1";  //sound
    public static final String SWITCH2 = "switch2";   //vibration
    public static final String SWITCH3 = "switch3";   //push notif


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        log_outbtn = view.findViewById(R.id.logoutButton1);
        editProfile = view.findViewById(R.id.edit_profile);
        about = view.findViewById(R.id.about_btn);
        deleteAccount = view.findViewById(R.id.delete_account);
        soundEffects = view.findViewById(R.id.sounds_switch);
        vibrations = view.findViewById(R.id.vibration_switch);
        pushNotif = view.findViewById(R.id.push_notif_switch);

        waiting = new Dialog(getActivity());
        waiting.setContentView(R.layout.waiting_progressbar);
        waiting.setCancelable(true);
        waiting.getWindow().setBackgroundDrawableResource(R.drawable.progress_background);
        waiting.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        loadData();


        soundEffects.setChecked(soundState);
        vibrations.setChecked(vibrationState);
        pushNotif.setChecked(pushNotificationState);


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userId = fAuth.getCurrentUser().getUid();
        user = fAuth.getCurrentUser();

        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (documentSnapshot.exists()) {
                    phone = documentSnapshot.getString("phone");
                    fullName = documentSnapshot.getString("fName");
                    email = documentSnapshot.getString("email");

                } else {
                    Log.d("tag", "onEvent: Document do not exists");
                }
            }
        });


        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialoge = new AlertDialog.Builder(getContext());
                dialoge.setTitle("Are you sure?");
                dialoge.setMessage("This will result in completely removing your account and user data from our database.");

                dialoge.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        waiting.show();
                        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                waiting.cancel();
                                if (task.isSuccessful()) {
                                    Toast.makeText(getActivity(), "Account deleted", Toast.LENGTH_LONG).show();

                                    Intent i = new Intent(getActivity(), Login.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(i);
                                } else {
                                    Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });

                dialoge.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = dialoge.create();
                alertDialog.show();
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), AboutAppActivity.class);
                startActivity(i);
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), EditProfile.class);
                i.putExtra("fullName", fullName);
                i.putExtra("email", email);
                i.putExtra("phone", phone);
                startActivity(i);

            }
        });

        log_outbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder dialoge = new AlertDialog.Builder(getContext());
                dialoge.setTitle("Are you sure?");
                dialoge.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout(v);
                    }
                });

                dialoge.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = dialoge.create();
                alertDialog.show();
            }
        }) ;
        return view;
    }

    public void saveData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(SWITCH1, soundEffects.isChecked());
        editor.putBoolean(SWITCH2, vibrations.isChecked());
        editor.putBoolean(SWITCH3, pushNotif.isChecked());

        editor.apply();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        soundState = sharedPreferences.getBoolean(SWITCH1, true);
        vibrationState = sharedPreferences.getBoolean(SWITCH2, true);
        pushNotificationState = sharedPreferences.getBoolean(SWITCH3, true);
    }

    @Override
    public void onDestroy() {
        saveData();
        super.onDestroy();
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();      //logout
        startActivity(new Intent(getContext(), Login.class));
        getActivity().finish();
    }

}
