package com.project.dstroh.bluetoothlistenerv2;

import java.util.concurrent.TimeUnit;


public class Timer {
    //private static Timer instance;
    private double elapsedSeconds;
    private long begin;
    //public boolean started = false;

    public Timer() {
        elapsedSeconds = 0;
    }

    /*public static synchronized Timer getInstance() {
        if(instance == null) {
            instance = new Timer();
        }
        return instance;
    }*/

    public void setTime(double previousElapsedSeconds) {
        elapsedSeconds = previousElapsedSeconds;
    }
    public void reset() {
        elapsedSeconds = 0;
        begin = 0;
    }

    public void start() {
        begin = System.currentTimeMillis();
        System.out.println("Started");
        //started = true;
    }

    public double lapse() {
        double toReturn = elapsedSeconds;
        if(begin != 0) {
            long end = System.currentTimeMillis();
            long elapsedMillis = (end - begin);
            System.out.println("Timer elapsed at " + TimeUnit.MILLISECONDS.toSeconds(elapsedMillis));
            toReturn += TimeUnit.MILLISECONDS.toSeconds(elapsedMillis);
        }

        return toReturn;
    }

    public double stop() {
        System.out.println("Stopped: " + (begin != 0));
        if(begin != 0) {
            long end = System.currentTimeMillis();
            long elapsedMillis = (end - begin);
            System.out.println("Timer stopped at " + TimeUnit.MILLISECONDS.toSeconds(elapsedMillis));
            elapsedSeconds += TimeUnit.MILLISECONDS.toSeconds(elapsedMillis);
        }
        begin = 0;
        return elapsedSeconds;
    }

    public boolean hasBegun() {
        return begin > 0;
    }

}