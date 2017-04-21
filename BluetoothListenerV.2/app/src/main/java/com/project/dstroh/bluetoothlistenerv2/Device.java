package com.project.dstroh.bluetoothlistenerv2;



public class Device {
    String name;
    String address;
    Timer duration;
    boolean ignore;

    // CONSTRUCTORS
    public Device() {
        name = "";
        address = "";
        //duration = new Timer();
        duration = null;
    }
    public Device(String name, String address) {
        this.name = name;
        this.address = address;
        duration = new Timer();
    }
    public Device(String name, String address, double elapsedTime) {
        this.name = name;
        this.address = address;
        duration.setTime(elapsedTime);
    }

    // ACCESSORS
    public String getName() {
        return name;
    }
    public String getAddress() {
        return address;
    }
    public double getTime() {
        return duration.lapse();
    }
    public boolean hasStarted() {
        return duration.hasBegun();
    }

    // MUTATORS
    public void appendAddress(String address) {
        this.address += "\n" + address;
    }
    public void setDuration(double duration) {
        this.duration.setTime(duration);
    }
    public void start() {
        duration.start();
    }
    public double stop() {
        return duration.stop();
    }
}
