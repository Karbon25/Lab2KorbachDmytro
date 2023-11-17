package com.example.lab2korbachdmytro.Service;

import com.example.lab2korbachdmytro.ShowResult.GameResult;

import java.util.ArrayList;
import java.util.List;

public interface IConnectorObserver {
    public void updateEventObserver(GameResult gameResult);
    public void onConnectObserver(ArrayList<GameResult> objOnCreate);
    public void onReplaceDataObserver(ArrayList<GameResult> objOnCreate);
    public void onDisconnectObserver();
}
