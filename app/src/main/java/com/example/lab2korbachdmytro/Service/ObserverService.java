package com.example.lab2korbachdmytro.Service;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.example.lab2korbachdmytro.ShowResult.GameResult;

import java.util.ArrayList;
import java.util.List;

public class ObserverService extends Service {

    private final IBinder binder = new LocalBinder();
    private List<GameResult> listResult;
    private List<IConnectorObserver> listConnectionObserver;

    public ObserverService() {
        listResult = new ArrayList<>();
        listConnectionObserver = new ArrayList<>();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ServiceReadWrite.startActionRead(this);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        for(IConnectorObserver client :listConnectionObserver){
            unregisterClientObserver(client);
        }
        super.onDestroy();
    }

    public void regiserClientObserver(IConnectorObserver client) {
        listConnectionObserver.add(client);
        client.onConnectObserver(new ArrayList<>(listResult));
    }

    public void unregisterClientObserver(IConnectorObserver client) {
        listConnectionObserver.remove(client);
        client.onDisconnectObserver();
    }

    public void notificationClientEvent(GameResult result) {
        listResult.add(0, result);
        notifyClients();
        ServiceReadWrite.startActionWrite(this);
    }

    public void setListResult(ArrayList<GameResult> listResult) {
        this.listResult = new ArrayList<>(listResult);
        notifyClients();
        ServiceReadWrite.startActionWrite(this);
    }

    private void notifyClients() {
        for (IConnectorObserver client : listConnectionObserver) {
            client.updateEventObserver(listResult.get(0));
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class LocalBinder extends Binder {
        public ObserverService getService() {
            return ObserverService.this;
        }
    }
}