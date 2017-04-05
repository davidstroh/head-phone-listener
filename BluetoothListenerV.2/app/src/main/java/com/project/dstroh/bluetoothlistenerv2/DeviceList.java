package com.project.dstroh.bluetoothlistenerv2;

import java.util.ArrayList;


public class DeviceList {
    private static DeviceList instance;
    ArrayList<String[]> devices;
    private int currentSpot;

    private DeviceList() {
        devices = new ArrayList();
        currentSpot = -1;
    }

    public static synchronized DeviceList getInstance() {
        if(instance == null) {
            instance = new DeviceList();
        }
        return instance;
    }

    public void add(String[] input){
        String[] newInput = {input[0], input[1], input[2]};
        devices.add(newInput);
    }
    public void set(int index, String[] input) {
        String[] newInput = {input[0], input[1], input[2]};
        devices.set(index, newInput);
    }
    public int setSpot(int index) {
        return currentSpot = index;
    }
    public int getSpot() {
        return currentSpot;
    }
    public void clear() {
        devices = new ArrayList<String[]>();
    }

    public ArrayList<String[]> getList() {
        return devices;
    }
    public String getName(int index) {
        return ((String[])devices.get(index))[0];
    }
    public String getAddress(int index) {
        return ((String[])devices.get(index))[1];
    }
    public Double getTime(int index) {
        return Double.parseDouble(((String[])devices.get(index))[2]);
    }
    public int getSize() {
        return devices.size();
    }

    public int containsName(String input) {
        for(int i = 0; i < devices.size(); i++) {
            System.out.println(i +"=  "+ ((String[])devices.get(i))[0] +"  :  "+ input +" || "+ ((String[])devices.get(i))[0].equals(input));
            if ( ((String[])devices.get(i))[0].equals(input) ) {
                return i;
            }
        }
        return -1;
    }

    public String print() {
        String toReturn = "";
        for(String[] device : devices){
            toReturn += "-- " + device[0] + ", " + device[1] + ", " + device[2] + "\n";
        }
        return toReturn;
    }
}