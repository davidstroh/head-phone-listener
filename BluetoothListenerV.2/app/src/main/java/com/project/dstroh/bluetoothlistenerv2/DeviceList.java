package com.project.dstroh.bluetoothlistenerv2;

import android.content.Context;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class DeviceList {
    private static DeviceList instance;
    ArrayList<String[]> devices;
    private int currentSpot;

    Timer timer;

    private final String FILENAME = "btDevicesTimeFile";
    FileOutputStream outputStream;
    FileInputStream inputStream;


    private DeviceList() {
        devices = new ArrayList();
        timer = Timer.getInstance();
        currentSpot = -1;
    }

    public static synchronized DeviceList getInstance() {
        if(instance == null) {
            instance = new DeviceList();
        }
        return instance;
    }

    public void add(String[] input) {
        String[] newInput = {input[0], input[1], input[2]};
        devices.add(newInput);
    }
    public void feed(String name, String address) {
        if( setSpot(containsName(name)) > -1 ) {
            double tempTime;
            //if lapsed time is 0, then a bluetooth device was connected, otherwise a bluetooth device was disconnected
            if((tempTime = timer.lapse()) != 0) {
                String[] tempArray = { name, address, Double.toString(tempTime) };
                set(getSpot(), tempArray);
            }
            else {
                timer.setTime(getTime(getSpot()));
            }

            System.out.println("YAYAYAYAYA   " + name +" FOUND IN FILE @"+ getSpot());
        }
        else {
            String[] tempDevice={ name, address, Double.toString(timer.lapse()) } ;
            add(tempDevice);
            setSpot(getSize() - 1);
            System.out.println("NANANANANA   "+ name +" NOTNONO FOUND IN FILE NOW IS "+ getSpot());
        }
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

    public void readFile(Context context) throws Exception {
        try{
            inputStream = context.openFileInput(FILENAME);
            clear();

            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while((line = br.readLine()) != null) {
                String[] words = line.split(",");
                add(words);
                System.out.println("FILE-IN: "+ line);
            }
            inputStream.close();
            System.out.println(print());
        }
        catch(FileNotFoundException ex) {
            throw ex;
            //System.out.println("BABABABABA   "+ getName() +" BONONO FILE YET");
        }
        catch(Exception ex) {
            throw ex;
        }
    }

    public void save(Context context) throws Exception {

        if(currentSpot > -1) {
            String[] tempArray = { getName(currentSpot), getAddress(currentSpot), Double.toString(timer.lapse()) };
            set(currentSpot, tempArray);
        }

        try {
            outputStream = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            FileWriter fw = new FileWriter(outputStream.getFD());

            String output = "";
            System.out.println("device size = "+ getSize());
            for(int i = 0; i < getSize(); i++) {
                output += getName(i) + ',' + getAddress(i) + ',' + getTime(i) +'\n';
            }
            System.out.println("WRITE-OUT: " + output);
            fw.write(output);
            fw.close();
        }
        catch(Exception ex) {
            throw ex;
        }
    }



    public String print() {
        String toReturn = "";
        for(String[] device : devices){
            toReturn += "-- " + device[0] + ", " + device[1] + ", " + device[2] + "\n";
        }
        return toReturn;
    }
}