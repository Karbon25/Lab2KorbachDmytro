package com.example.lab2korbachdmytro;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.lab2korbachdmytro.GamePlace.TimeToStartGame;
import com.example.lab2korbachdmytro.ShowResult.ViewListResult;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);


        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                SettingActivity.super.finishAffinity();
            }
        });

        findViewById(R.id.button_show_results).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, ViewListResult.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.button_start_game).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int gamePlace = 0;

                int idGamePlace = ((RadioGroup) findViewById(R.id.setting_game_place_size)).getCheckedRadioButtonId();
                if(idGamePlace == R.id.r_button_place_3_3){
                    gamePlace = 0;
                }else if(idGamePlace == R.id.r_button_place_4_4){
                    gamePlace = 1;
                } else if (idGamePlace == R.id.r_button_place_5_5) {
                    gamePlace = 2;
                }

                int timeGame = 0;
                int idTimeGame = ((RadioGroup) findViewById(R.id.setting_game_time)).getCheckedRadioButtonId();

                if (idTimeGame == R.id.r_button_time_15) {
                    timeGame = 15000;
                } else if (idTimeGame == R.id.r_button_time_30) {
                    timeGame = 30000;
                } else if (idTimeGame == R.id.r_button_time_45) {
                    timeGame = 45000;
                }

                int timeReactionMin = 0, timeReactionMax = 0;
                int idTimeReaction = ((RadioGroup) findViewById(R.id.setting_reaction_time)).getCheckedRadioButtonId();
                if (idTimeReaction == R.id.r_button_time_reaction_1000_1500) {
                    timeReactionMin = 1000;
                    timeReactionMax = 1500;
                } else if (idTimeReaction == R.id.r_button_time_reaction_700_1000) {
                    timeReactionMin = 700;
                    timeReactionMax = 1000;
                } else if (idTimeReaction == R.id.r_button_time_reaction_500_700) {
                    timeReactionMin = 500;
                    timeReactionMax = 700;
                } else if (idTimeReaction == R.id.r_button_time_reaction_300_600) {
                    timeReactionMin = 300;
                    timeReactionMax = 600;
                }
                TextView textViewName = findViewById(R.id.setting_name_player);
                startGame(textViewName.getText().toString(), gamePlace, timeGame, timeReactionMin, timeReactionMax);
            }
        });
    }



    protected void startGame(String namePlayer, int place, int timePlay, int timeReactionMin, int timeReactionMax){
        Intent intent = new Intent(SettingActivity.this, TimeToStartGame.class);
        intent.putExtra("namePlayer", namePlayer);
        intent.putExtra("gamePlace", place);
        intent.putExtra("timeGame", timePlay);
        intent.putExtra("timeReactionMin", timeReactionMin);
        intent.putExtra("timeReactionMax", timeReactionMax);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

}