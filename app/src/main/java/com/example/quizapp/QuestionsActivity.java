package com.example.quizapp;

import android.animation.Animator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.example.quizapp.MainActivity2.soundState;
import static com.example.quizapp.MainActivity2.vibrationState;
import static com.example.quizapp.newSetActivity.time_lim;

public class QuestionsActivity extends AppCompatActivity implements View.OnClickListener {
    private SoundPool soundPool;
    private int done, insight, wrongans, correctans;
    private TextView questiondis, qnumdis;
    private TextView counter;
    private int qnum;
    private int set_num, level_id;
    private Dialog loading;
    private Button opt1, opt2, opt3, opt4;
    private List<Questions> questionsList;
    CountDownTimer countDownTimer;
    MediaPlayer mediaPlayer;
    int score = 0;
    public static int setId;
    private FirebaseFirestore db;


    @Override
    protected void onResume() {
        soundPool.autoResume();
        super.onResume();

    }

    @Override
    protected void onPause() {
        soundPool.autoPause();
        super.onPause();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(6)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            soundPool = new SoundPool(6, AudioManager.STREAM_MUSIC, 0);

        }
        done = soundPool.load(this, R.raw.done, 1);
        insight = soundPool.load(this, R.raw.insight, 1);
        wrongans = soundPool.load(this, R.raw.wrongans, 1);
        correctans = soundPool.load(this, R.raw.correctans, 1);
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
        loading.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        set_num = getIntent().getIntExtra("set_id", 1);
        setId = set_num;
        level_id = getIntent().getIntExtra("level", 1);

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
        db.collection("Quizes").document("Level-" + level_id).collection("set-" + set_num).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
//                    QuerySnapshot quests = task.getResult();
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        questionsList.add(new Questions(doc.getString("quest")
                                , doc.getString("a")
                                , doc.getString("b")
                                , doc.getString("c")
                                , doc.getString("d")
                                , Integer.parseInt(doc.getString("ans"))));
                    }
                    setQuestion();
                } else {
                    Toast.makeText(QuestionsActivity.this, (CharSequence) task.getException(), Toast.LENGTH_SHORT).show();
                }
                loading.cancel();
            }
        });
    }

    private void setQuestion() {
        counter.setText(String.valueOf(time_lim));
        questiondis.setText(questionsList.get(qnum).getQuestion());
        opt1.setText(questionsList.get(qnum).getOptionA());
        opt2.setText(questionsList.get(qnum).getOptionB());
        opt3.setText(questionsList.get(qnum).getOptionC());
        opt4.setText(questionsList.get(qnum).getOptionD());

        qnumdis.setText(qnum + "/" + questionsList.size());
        startCounter();
        qnum = 0;
    }

    private void startCounter() {
        countDownTimer = new CountDownTimer(time_lim * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                counter.setText(String.valueOf(millisUntilFinished / 1000));

            }

            @Override
            public void onFinish() {
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                //Vibrate for 500 milliseconds
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (vibrationState)
                        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));

                } else {
                    //deprecated inAPI 26
                    if (vibrationState)
                        v.vibrate(500);
                }

                changeQuestion();
            }
        };
        countDownTimer.start();
    }

    private void changeQuestion() {
        if (qnum < questionsList.size() - 1) {
            qnum++;
            playAnim(questiondis, 0, 0);
            playAnim(opt1, 0, 1);
            playAnim(opt2, 0, 2);
            playAnim(opt3, 0, 3);
            playAnim(opt4, 0, 4);

            qnumdis.setText((qnum + 1) + "/" + questionsList.size());
            counter.setText(String.valueOf(10));
            startCounter();


        } else {
            //show score
            countDownTimer.cancel();
            Intent intent = new Intent(QuestionsActivity.this, ScoreActivity.class);
            intent.putExtra("Score", score);
            intent.putExtra("TotalQ", questionsList.size());
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
                        if (val == 0) {
                            switch (viewnum) {
                                case 0:
                                    ((TextView) v).setText(questionsList.get(qnum).getQuestion());
                                    break;
                                case 1:
                                    ((Button) v).setText(questionsList.get(qnum).getOptionA());
                                    break;
                                case 2:
                                    ((Button) v).setText(questionsList.get(qnum).getOptionB());
                                    break;
                                case 3:
                                    ((Button) v).setText(questionsList.get(qnum).getOptionC());
                                    break;
                                case 4:
                                    ((Button) v).setText(questionsList.get(qnum).getOptionD());
                                    break;
                            }
                            if (viewnum != 0)
                                v.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#46009F")));

                            playAnim(v, 1, viewnum);
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
        switch (v.getId()) {
            case R.id.opt1:
                selectedOpt = 1;
                break;
            case R.id.opt2:
                selectedOpt = 2;
                break;
            case R.id.opt3:
                selectedOpt = 3;
                break;
            case R.id.opt4:
                selectedOpt = 4;
                break;
            default:
        }
        countDownTimer.cancel();
        checkAnswer(selectedOpt, v);
    }

    private void checkAnswer(int selectedOpt, View v) {
        if (selectedOpt == questionsList.get(qnum).getAnswer()) {
            //right answer
            playRightSound(soundState);
            v.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            score++;

        } else {
            //wrong answer
            playWrongSound(soundState);
            v.setBackgroundTintList(ColorStateList.valueOf(Color.RED));


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
                if (soundState) {
                    soundPool.play(done, 1, 1, 0, 0, 1);
                }
                changeQuestion();
            }
        }, 2000);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundPool.release();
        soundPool = null;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialoge = new AlertDialog.Builder(QuestionsActivity.this);
        dialoge.setTitle("Are you sure?");
        dialoge.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                countDownTimer.cancel();
                Intent intent = new Intent(QuestionsActivity.this, MainActivity2.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });

        dialoge.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = dialoge.create();
        alertDialog.show();


    }

    public void playRightSound(boolean state) {
        if (state)
            soundPool.play(correctans, 1, 1, 0, 0, 1);
    }

    public void playWrongSound(boolean state) {
        if (state)
            soundPool.play(wrongans, 1, 1, 0, 0, 1);
    }


}
