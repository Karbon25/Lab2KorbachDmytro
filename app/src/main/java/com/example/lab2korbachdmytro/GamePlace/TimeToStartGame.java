package com.example.lab2korbachdmytro.GamePlace;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.lab2korbachdmytro.R;

public class TimeToStartGame extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_to_start_game);
        TextView secondTimeView = findViewById(R.id.time_to_start_finish);
        Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.animation_start_text);
        CountDownTimer countDownTimer = new CountDownTimer(5000, 1000) {
            public void onTick(long millisUntilFinished) {

                long secondsRemaining = millisUntilFinished / 1000;
                if(secondsRemaining == 0){
                    secondTimeView.setText("Почали!");
                    secondTimeView.startAnimation(fadeInAnimation);
                }else{
                    secondTimeView.setText(String.valueOf(secondsRemaining));
                    secondTimeView.startAnimation(fadeInAnimation);
                }

            }

            public void onFinish() {
                startGame();
            }
        };
        countDownTimer.start();
        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                countDownTimer.cancel();
                this.setEnabled(false);
                getOnBackPressedDispatcher().onBackPressed();
            }
        });
    }


    protected void startGame(){
        Intent intent = getIntent();
        if (intent != null) {
            String namePlayer = intent.getStringExtra("namePlayer");
            int gamePlace = intent.getIntExtra("gamePlace", 0);
            int timeGame = intent.getIntExtra("timeGame", 15000);
            int timeReactionMin = intent.getIntExtra("timeReactionMin", 1000);
            int timeReactionMax = intent.getIntExtra("timeReactionMax", 1500);
            Class gamePlaceClass = null;
            switch (gamePlace){
                case 0:{
                    gamePlaceClass = GamePlace3x3.class;
                    break;
                }
                case 1: {
                    gamePlaceClass = GamePlace4x4.class;
                    break;
                }
                case 2: {
                    gamePlaceClass = GamePlace5x5.class;
                    break;
                }
            }
            intent = new Intent(TimeToStartGame.this, gamePlaceClass);
            intent.putExtra("timeGame", timeGame);
            intent.putExtra("timeReactionMin", timeReactionMin);
            intent.putExtra("timeReactionMax", timeReactionMax);
            intent.putExtra("namePlayer", namePlayer);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        
    }
}