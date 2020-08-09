package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import static com.example.quizapp.SettingsFragment.SHARED_PREFS;
import static com.example.quizapp.SettingsFragment.SWITCH1;
import static com.example.quizapp.SettingsFragment.SWITCH2;
import static com.example.quizapp.SettingsFragment.SWITCH3;
import static com.example.quizapp.SettingsFragment.pushNotif;
import static com.example.quizapp.SettingsFragment.soundEffects;
import static com.example.quizapp.SettingsFragment.vibrations;

public class MainActivity2 extends AppCompatActivity {


    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    DrawerLayout drawer;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;

    boolean doubleBackToExitPressedOnce = false;

    public static boolean soundState;  //sound
    public static boolean vibrationState;   //vibration
    public static boolean pushNotificationState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        loadData();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Dashboard");
        navigationView = findViewById(R.id.nested);


        drawer = findViewById(R.id.drawer);
        toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.open,R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();

        //add default fragment

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragmentContainer,new HomeFragment());
        fragmentTransaction.commit();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                drawer.closeDrawer(GravityCompat.START);
                if(item.getItemId() == R.id.home){
                    fragmentManager = getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentContainer,new HomeFragment());
                    fragmentTransaction.commit();
                    // loadFragment(new MainFragment());
                }
                if(item.getItemId() == R.id.account){
                    fragmentManager = getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentContainer,new ProfileFragment());
                    fragmentTransaction.commit();
                    //loadFragment(new SecondFragment());
                }
                if(item.getItemId() == R.id.leaderboard){
                    fragmentManager = getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentContainer,new LeaderboardFragment());
                    fragmentTransaction.commit();
                    //loadFragment(new MainFragment());
                }
                if(item.getItemId() == R.id.settings){
                    fragmentManager = getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentContainer,new SettingsFragment());
                    fragmentTransaction.commit();
                    //loadFragment(new MainFragment());
                }
                return true;

            }

        });
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer,new HomeFragment());
        fragmentTransaction.commit();

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,Context.MODE_PRIVATE);
        soundState = sharedPreferences.getBoolean(SWITCH1,true);
        vibrationState = sharedPreferences.getBoolean(SWITCH2,true);
        pushNotificationState = sharedPreferences.getBoolean(SWITCH3,true);
    }


}