package com.example.lab2korbachdmytro.ShowResult;

import java.io.Serializable;

public class GameResult implements Serializable {
    private static final long serialVersionUID = 1L;
    public GameResult(String name, int time, int score) {
        this.name = name;
        this.time = time;
        this.score = score;
    }

    public GameResult() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    private String name;
    private int time;
    private int score;

}
