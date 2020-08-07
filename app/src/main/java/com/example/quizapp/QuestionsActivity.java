package com.example.quizapp;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import android.hardware.Sensor;
import android.hardware.SensorManager;

import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.example.quizapp.SetsActivity.level_id;
import static com.example.quizapp.SetsActivity.time_lim;

public class QuestionsActivity extends AppCompatActivity implements View.OnClickListener {
    private SoundPool soundPool;
    private int done,insight,wrongans,correctans;
    private TextView questiondis,qnumdis;
    private TextView counter;
    private int qnum;
    private int set_num;
    private Dialog loading;
    private Button opt1,opt2,opt3,opt4;
    private List<Questions> questionsList;
    CountDownTimer countDownTimer;
    MediaPlayer mediaPlayer;
    int score =0;

    private FirebaseFirestore db ;

///////////////////////////////something for the future//////////////////////////////////////
//    @Override
//    protected void onResume() {
//        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
//                SensorManager.SENSOR_DELAY_NORMAL);
//        super.onResume();
//        cameraView.start();                                                                          //to start camera
//    }
//
//    @Override
//    protected void onPause() {
//        mSensorManager.unregisterListener(mSensorListener);
//        super.onPause();
//        cameraView.stop();  }                                                                        //to stop camera (when the activity gets paused)

/////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(6)
                    .setAudioAttributes(audioAttributes)
                    .build();
        }else
        {
            soundPool = new SoundPool(6, AudioManager.STREAM_MUSIC,0);

        }
        done = soundPool.load(this,R.raw.done,1);
        insight = soundPool.load(this,R.raw.insight,1);
        wrongans = soundPool.load(this,R.raw.wrongans,1);
        correctans = soundPool.load(this,R.raw.correctans,1);
        questiondis = findViewById(R.id.question);
        counter = findViewById(R.id.countdown);
        qnumdis = findViewById(R.id.quest_count);

        opt1 = findViewById(R.id.opt1);
        opt2 = findViewById(R.id.opt2);
        opt3 = findViewById(R.id.opt3);
        opt4 = findViewById(R.id.opt4);

        loading = new Dialog(QuestionsActivity.this);
        loading.setContentView(R.layout.loading_progressbar);
        loading.setCancelable(false);
        loading.getWindow().setBackgroundDrawableResource(R.drawable.progress_background);
        loading.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);


        set_num = getIntent().getIntExtra("set_id",1);

        opt1.setOnClickListener(this);
        opt2.setOnClickListener(this);
        opt3.setOnClickListener(this);
        opt4.setOnClickListener(this);

        db = FirebaseFirestore.getInstance();
        getQuestionsList();
    }



    private void getQuestionsList() {
        loading.show();
        questionsList = new ArrayList<>();
        db.collection("Quizes").document("Level-"+ String.valueOf(level_id)).collection("set-" + String.valueOf(set_num)).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
//                    QuerySnapshot quests = task.getResult();
                    for(QueryDocumentSnapshot doc : task.getResult()){
                        questionsList.add(new Questions(doc.getString("quest")
                                ,doc.getString("a")
                                ,doc.getString("b")
                                ,doc.getString("c")
                                ,doc.getString("d")
                                ,Integer.parseInt(doc.getString("ans"))));
                    }
                    setQuestion();
                } else {
                    Toast.makeText(QuestionsActivity.this, (CharSequence) task.getException(),Toast.LENGTH_SHORT).show();
                }
                loading.cancel();
            }
        });
//        questionsList.add(new Questions("Question 1","A","B","C","D",4));
//        questionsList.add(new Questions("Question 2","A","B","C","D",1));
//        questionsList.add(new Questions("Question 3","A","B","C","D",3));
//        questionsList.add(new Questions("Question 4","A","B","C","D",2));
//
//        setQuestion();
    }

    private void setQuestion() {
        counter.setText(String.valueOf(time_lim));
        questiondis.setText(questionsList.get(qnum).getQuestion());
        opt1.setText(questionsList.get(qnum).getOptionA());
        opt2.setText(questionsList.get(qnum).getOptionB());
        opt3.setText(questionsList.get(qnum).getOptionC());
        opt4.setText(questionsList.get(qnum).getOptionD());

        qnumdis.setText(String.valueOf(qnum) + "/" + String.valueOf(questionsList.size()));
        startCounter();
        qnum = 0;
    }

    private void startCounter() {
        countDownTimer = new CountDownTimer(time_lim*1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                counter.setText(String.valueOf(millisUntilFinished/1000));

            }

            @Override
            public void onFinish() {
                Vibrator v = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                //Vibrate for 500 milliseconds
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                {
                    v.vibrate(VibrationEffect.createOneShot(500,VibrationEffect.DEFAULT_AMPLITUDE));

                }else{
                    //deprecated inAPI 26
                    v.vibrate(500);
                }

                changeQuestion();
            }
        };
        countDownTimer.start();
    }

    private void changeQuestion() {
        if(qnum<questionsList.size()-1) {
            qnum++;
            playAnim(questiondis,0,0);
            playAnim(opt1,0,1);
            playAnim(opt2,0,2);
            playAnim(opt3,0,3);
            playAnim(opt4,0,4);

            qnumdis.setText(String.valueOf(qnum+1) + "/" + String.valueOf(questionsList.size()));
            counter.setText(String.valueOf(10));
            startCounter();


        } else {
            //show score
            countDownTimer.cancel();
            Intent intent = new Intent(QuestionsActivity.this,ScoreActivity.class);
            intent.putExtra("Score",score);
            intent.putExtra("TotalQ",questionsList.size());
            startActivity(intent);
            finish();
        }
    }

    private void playAnim(final View v, final int val, final int viewnum) {
        v.animate().alpha(val).scaleX(val).scaleY(val).setDuration(500)
                .setStartDelay(100).setInterpolator(new DecelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if(val == 0) {
                            switch(viewnum){
                                case 0 :
                                    ((TextView)v).setText(questionsList.get(qnum).getQuestion());
                                    break;
                                case 1 :
                                    ((Button)v).setText(questionsList.get(qnum).getOptionA());
                                    break;
                                case 2 :
                                    ((Button)v).setText(questionsList.get(qnum).getOptionB());
                                    break;
                                case 3 :
                                    ((Button)v).setText(questionsList.get(qnum).getOptionC());
                                    break;
                                case 4 :
                                    ((Button)v).setText(questionsList.get(qnum).getOptionD());
                                    break;
                            }
                            if(viewnum != 0)
                                ((Button)v).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#BD4ED5")));

                            playAnim(v,1,viewnum);
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
    }

    @Override
    public void onClick(View v) {
        int selectedOpt = 0;
        switch(v.getId()) {
            case R.id.opt1 :
                selectedOpt = 1;
                break;
            case R.id.opt2 :
                selectedOpt = 2;
                break;
            case R.id.opt3 :
                selectedOpt = 3;
                break;
            case R.id.opt4 :
                selectedOpt = 4;
                break;
            default:
        }
        countDownTimer.cancel();
        checkAnswer(selectedOpt,v) ;
    }

    private void checkAnswer(int selectedOpt, View v) {
        if(selectedOpt == questionsList.get(qnum).getAnswer()) {
            //right answer
            soundPool.play(correctans,1,1,0,0,1);
            ((Button)v).setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            score++;

        } else {
            //wrong answer
            soundPool.play(wrongans,1,1,0,0,1);
            ((Button)v).setBackgroundTintList(ColorStateList.valueOf(Color.RED));


            switch (questionsList.get(qnum).getAnswer()) {
                case 1:
                    opt1.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
                case 2:
                    opt2.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
                case 3:
                    opt3.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
                case 4:
                    opt4.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;

            }
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                soundPool.play(done,1,1,0,0,1);
                changeQuestion();
            }
        }, 2000);
    }

//    /**To handle orientation changes
//     * */
//    @Override
//    protected void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putString("Display Output",display.getText().toString());   //save textView output in our outstate bundle
//    }
//
//    @Override
//    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        display.setText(savedInstanceState.getString("Display Output"));   //receiving and displaying the value from our saved instance state bundle
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundPool.release();
        soundPool=null;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(QuestionsActivity.this,MainActivity.class);
        startActivity(intent);
        finish();



    }

}
