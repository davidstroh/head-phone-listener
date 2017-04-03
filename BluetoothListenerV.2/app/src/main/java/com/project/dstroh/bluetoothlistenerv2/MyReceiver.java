package com.project.dstroh.bluetoothlistenerv2;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class MyReceiver extends BroadcastReceiver {

    private final String FILENAME = "btDevicesTimeFile";
    private final double MAXTIME = 360;
    FileOutputStream outputStream;
    FileInputStream inputStream;
    Timer timer;
    DeviceList currentDevices;
    int currentSpot = -1;

    public MyReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        timer = Timer.getInstance();
        currentDevices = DeviceList.getInstance();

        if(BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if(device.getName().equals("G18")) {
                Toast.makeText(context, "G18: Name: " + device.getName() +
                        "\nBluetooth Device Address: " + device.getAddress(), Toast.LENGTH_LONG).show();

                try{
                    inputStream = context.openFileInput(FILENAME);
                    int lineCount = 0;
                    timer.reset();
                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder builder = new StringBuilder();
                    String line;
                    while((line = br.readLine()) != null) {
                        String[] words = line.split(",");
                        if(words[1].equals(device.getAddress())) {
                            currentSpot = lineCount;
                            timer.setTime(Double.parseDouble(words[2]));
                        }
                        currentDevices.add(words);
                        lineCount++;
                        //builder.append(line);
                        //builder.append('\n');
                    }
                }
                catch(FileNotFoundException ex) {
                    timer.reset();
                }
                catch(Exception ex) {
                    ex.printStackTrace();
                }
                timer.start();
            }
            else
                Toast.makeText(context, "Anotha one: Name: " + device.getName() +
                                "\nBluetooth Device Address: " + device.getAddress(), Toast.LENGTH_LONG).show();
        }
        else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            try {
                double stopTime = timer.stop();
                String[] tempList = {device.getName(), device.getAddress(), String.valueOf(stopTime)};
                if(currentSpot > -1) {
                    currentDevices.set(currentSpot, tempList);
                }
                else {
                    currentDevices.add(tempList);
                }

                outputStream = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
                FileWriter fw = new FileWriter(outputStream.getFD());

                String output = "";
                for(String[] outDevice : currentDevices.getList()){
                    output = outDevice[0] + ',' + outDevice[1] + ',' + outDevice[2] +'\n';
                    //outputStream.write(output.getBytes());
                }
                fw.write(output);
                fw.close();
                //outputStream.close();

                inputStream = context.openFileInput(FILENAME);
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder builder = new StringBuilder();
                String line;
                while((line = br.readLine()) != null) {
                    builder.append(line);
                    builder.append('\n');
                }
                System.out.println("We built this house: " + builder.toString());

                Toast.makeText(context, "This one has lasted: " + stopTime + " ayyy.", Toast.LENGTH_LONG).show();
                //Toast.makeText(context, "This one: " + builder.toString() + " disconnected.", Toast.LENGTH_LONG).show();

            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
        }
        else {
            Toast.makeText(context, "Non-Bluetooth Broadcast Intent Detected: " + action, Toast.LENGTH_LONG).show();
        }
    }
}



class Timer {
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

class DeviceList {
    private static DeviceList instance;
    ArrayList<String[]> devices;

    private DeviceList() {
        devices = new ArrayList();
    }

    public static synchronized DeviceList getInstance() {
        if(instance == null) {
            instance = new DeviceList();
        }
        return instance;
    }

    public void add(String[] input){
        devices.add(input);
    }
    public void set(int index, String[] input) {
        devices.set(index, input);
    }
    public ArrayList<String[]> getList() {
        return devices;
    }
}