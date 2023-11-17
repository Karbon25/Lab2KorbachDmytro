package com.example.lab2korbachdmytro.GamePlace;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.lab2korbachdmytro.R;
import com.example.lab2korbachdmytro.Service.ObserverService;
import com.example.lab2korbachdmytro.ShowResult.GameResult;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GamePlace4x4 extends AppCompatActivity {
    private ObserverService observer;

    private int score;
    private int currentButtonActive;
    private OnBackPressedCallback callbackPressBack;
    private int timeGame;
    private String namePlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_place4x4);

        Intent intent = getIntent();
        if (intent != null) {
            namePlayer = intent.getStringExtra("namePlayer");
            timeGame = intent.getIntExtra("timeGame", 15000);
            int min_reaction_time = intent.getIntExtra("timeReactionMin", 1000);
            int max_reaction_time = intent.getIntExtra("timeReactionMax", 1500);

            score = 0;


            TextView timeView = findViewById(R.id.time_place_4x4);
            Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.animation_start_text);
            int[] listButtons = new int[]{
                    R.id.button_place_4x4_1,
                    R.id.button_place_4x4_2,
                    R.id.button_place_4x4_3,
                    R.id.button_place_4x4_4,
                    R.id.button_place_4x4_5,
                    R.id.button_place_4x4_6,
                    R.id.button_place_4x4_7,
                    R.id.button_place_4x4_8,
                    R.id.button_place_4x4_9,
                    R.id.button_place_4x4_10,
                    R.id.button_place_4x4_11,
                    R.id.button_place_4x4_12,
                    R.id.button_place_4x4_13,
                    R.id.button_place_4x4_14,
                    R.id.button_place_4x4_15,
                    R.id.button_place_4x4_16
            };
            currentButtonActive = listButtons[0];
            TextView scoreText = findViewById(R.id.score_place_4x4);
            View.OnClickListener commonClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getId() == currentButtonActive) {
                        score+=2;
                    } else {
                        score--;
                    }
                    scoreText.setText("Рахунок: " + Integer.toString(score));
//                    scoreText.startAnimation(fadeInAnimation);
                }
            };
            for (int i : listButtons) {
                findViewById(i).setOnClickListener((View.OnClickListener) commonClickListener);
            }

            Random rnd = new Random();






            Timer timerReaction = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {

                    findViewById(currentButtonActive).setBackgroundResource(R.drawable.button_game_gray);
                    currentButtonActive = listButtons[rnd.nextInt(16)];
                    findViewById(currentButtonActive).setBackgroundResource(R.drawable.button_game_red);
                }
            };
            CountDownTimer timer = new CountDownTimer(timeGame, 1000) {
                public void onTick(long millisUntilFinished) {
                    long secondsRemaining = millisUntilFinished / 1000;
                    timeView.setText(String.valueOf(secondsRemaining));
                    timeView.startAnimation(fadeInAnimation);
                }

                public void onFinish() {
                    timerReaction.cancel();
                    showResult();
                }
            };
            timer.start();
            timerReaction.schedule(task, 100, rnd.nextInt(max_reaction_time - min_reaction_time + 1) + max_reaction_time);

            callbackPressBack = new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    timer.cancel();
                    timerReaction.cancel();
                    showResult();
                }
            };

            getOnBackPressedDispatcher().addCallback(callbackPressBack);

        }
    }
    private void showResult(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Результат");
        builder.setMessage(String.format("Ваш рахунок: %d", score));
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                callbackPressBack.remove();
                getOnBackPressedDispatcher().onBackPressed();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        Intent intentObserver = new Intent(this, ObserverService.class);
        bindService(intentObserver, serviceConnection, Context.BIND_AUTO_CREATE);
    }
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ObserverService.LocalBinder binder = (ObserverService.LocalBinder) service;
            observer = binder.getService();
            observer.notificationClientEvent(new GameResult(namePlayer, timeGame/1000, score));
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    @Override
    protected void onDestroy() {
        unbindService(serviceConnection);
        super.onDestroy();
    }

}