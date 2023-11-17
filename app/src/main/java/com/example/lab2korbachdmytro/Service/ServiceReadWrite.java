package com.example.lab2korbachdmytro.Service;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.IntentService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.lab2korbachdmytro.ShowResult.GameResult;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ServiceReadWrite extends IntentService implements IConnectorObserver {

    private static final String ACTION_READ = "com.example.lab2korbachdmytro.action.Read";
    private static final String ACTION_WRITE = "com.example.lab2korbachdmytro.action.Write";

    private ObserverService observer;
    private boolean flagUpdating;

    public ServiceReadWrite() {
        super("ServiceReadWrite");

    }
    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }
    public static void startActionRead(Context context) {
        Intent intent = new Intent(context, ServiceReadWrite.class);
        intent.setAction(ACTION_READ);
        context.startService(intent);
    }

    public static void startActionWrite(Context context) {
        Intent intent = new Intent(context, ServiceReadWrite.class);
        intent.setAction(ACTION_WRITE);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_READ.equals(action)) {
                handleActionRead();
            } else if (ACTION_WRITE.equals(action)) {
                handleActionWrite();
            }
        }
    }

    private void handleActionWrite() {
        Intent intentObserver = new Intent(this, ObserverService.class);
        bindService(intentObserver, serviceConnectionWrite, Context.BIND_AUTO_CREATE);
    }

    private void handleActionRead() {
        Intent intentObserver = new Intent(this, ObserverService.class);
        bindService(intentObserver, serviceConnectionRead, Context.BIND_AUTO_CREATE);

    }


    @Override
    public void updateEventObserver(GameResult gameResult) {
        // Your implementation
    }

    @Override
    public void onConnectObserver(ArrayList<GameResult> objOnCreate) {

        try (FileOutputStream fos = openFileOutput("database1.dat", Context.MODE_PRIVATE);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(objOnCreate);
        } catch (IOException e) {
            Log.e(TAG, "Error writing to file", e);
        }
        observer.unregisterClientObserver(this);
        unbindService(serviceConnectionWrite);
    }

    @Override
    public void onReplaceDataObserver(ArrayList<GameResult> objOnReplace) {
        // Your implementation
    }

    @Override
    public void onDisconnectObserver() {
        // Your implementation
    }

    private ServiceConnection serviceConnectionRead = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ObserverService.LocalBinder binder = (ObserverService.LocalBinder) service;
            observer = binder.getService();
            List<GameResult> results;
            try (FileInputStream fis = openFileInput("database1.dat");
                 ObjectInputStream ois = new ObjectInputStream(fis)) {
                results = (List<GameResult>) ois.readObject();
            } catch (Exception e) {
                results = new ArrayList<>();
            }
            observer.setListResult(new ArrayList<>(results));
            unbindService(serviceConnectionRead);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // Your implementation
        }
    };
    private ServiceConnection serviceConnectionWrite = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ObserverService.LocalBinder binder = (ObserverService.LocalBinder) service;
            observer = binder.getService();
            observer.regiserClientObserver(ServiceReadWrite.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // Your implementation
        }
    };
}