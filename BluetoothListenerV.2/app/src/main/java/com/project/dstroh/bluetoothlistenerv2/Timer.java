package com.project.dstroh.bluetoothlistenerv2;

import java.util.concurrent.TimeUnit;


public class Timer {
    private static Timer instance;
    private double elapsedSeconds;
    private long begin;
    //public boolean started = false;

    private Timer() {
        elapsedSeconds = 0;
    }

    public static synchronized Timer getInstance() {
        if(instance == null) {
            instance = new Timer();
        }
        return instance;
    }

    public void setTime(double previousElapsedSeconds) {
        elapsedSeconds = previousElapsedSeconds;
    }
    public void reset() {
        elapsedSeconds = 0;
    }

    public void start() {
        begin = System.currentTimeMillis();
        //started = true;
    }
    public double stop() {
        long end = System.currentTimeMillis();
        long elapsedMillis = (end - begin);
        System.out.println("Timer stopped at " + TimeUnit.MILLISECONDS.toSeconds(elapsedMillis));
        elapsedSeconds += TimeUnit.MILLISECONDS.toSeconds(elapsedMillis);
        return elapsedSeconds;
    }
}