package com.example.lab2korbachdmytro.ShowResult;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;

import com.example.lab2korbachdmytro.R;
import com.example.lab2korbachdmytro.Service.IConnectorObserver;
import com.example.lab2korbachdmytro.Service.ObserverService;
import com.example.lab2korbachdmytro.Service.ServiceReadWrite;

import java.util.ArrayList;
import java.util.List;

public class ViewListResult extends AppCompatActivity implements IConnectorObserver {

    private ObserverService observer;
    private RecyclerView resultView;
    private GameResultAdapter adapter;
    private List<GameResult> gameResults;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list_result);
        Intent intentObserver = new Intent(this, ObserverService.class);
        bindService(intentObserver, serviceConnection, Context.BIND_AUTO_CREATE);
    }
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ObserverService.LocalBinder binder = (ObserverService.LocalBinder) service;
            observer = binder.getService();
            observer.regiserClientObserver(ViewListResult.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            observer.unregisterClientObserver(ViewListResult.this);

        }
    };

    @Override
    public void updateEventObserver(GameResult gameResult) {
        gameResults.add(0, gameResult);
        updateGameResult();
    }

    @Override
    public void onConnectObserver(ArrayList<GameResult> objOnCreate) {
        gameResults = (ArrayList<GameResult>)(Object)objOnCreate.clone();
        updateGameResult();
    }

    private void updateGameResult() {
        resultView = findViewById(R.id.result_game_list);
        resultView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GameResultAdapter(this, gameResults);
        resultView.setAdapter(adapter);
    }

    @Override
    public void onReplaceDataObserver(ArrayList<GameResult> objOnCreate) {
        gameResults = (ArrayList<GameResult>)(Object)objOnCreate.clone();
        updateGameResult();
    }

    @Override
    public void onDisconnectObserver() {

    }

    @Override
    protected void onDestroy() {
        unbindService(serviceConnection);

        super.onDestroy();
    }
}