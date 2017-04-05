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

    public MyReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        timer = Timer.getInstance();
        currentDevices = DeviceList.getInstance();

        if(BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Toast.makeText(context, "BT: Name: " + device.getName() +
                        "\nBluetooth Device Address: " + device.getAddress(), Toast.LENGTH_LONG).show();

                try{
                    inputStream = context.openFileInput(FILENAME);
                    timer.reset();
                    currentDevices.clear();
                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    while((line = br.readLine()) != null) {
                        String[] words = line.split(",");
                        currentDevices.add(words);
                        System.out.println("FILE-IN: "+ line);
                    }
                    if( currentDevices.setSpot(currentDevices.containsName(device.getName())) > -1 ) {
                        timer.setTime(currentDevices.getTime(currentDevices.getSpot()));
                        System.out.println("YAYAYAYAYA   " + device.getName() +" FOUND IN FILE @"+ currentDevices.getSpot());
                    }
                    else {
                        String[] tempDevice={ device.getName(), device.getAddress(), "0" } ;
                        currentDevices.add(tempDevice);
                        currentDevices.setSpot(currentDevices.getSize() - 1);
                        System.out.println("NANANANANA   "+ device.getName() +" NOTNONO FOUND IN FILE");
                    }
                    System.out.println(currentDevices.print());
                }
                catch(FileNotFoundException ex) {
                    timer.reset();
                    String[] tempDevice={ device.getName(), device.getAddress(), "0" } ;
                    currentDevices.add(tempDevice);
                    currentDevices.setSpot(currentDevices.getSize() - 1);
                    System.out.println("BABABABABA   "+ device.getName() +" BONONO FILE YET");
                }
                catch(Exception ex) {
                    ex.printStackTrace();
                }
                timer.start();
        }
        else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            try {
                double stopTime = timer.stop();
                String[] tempList = {device.getName(), device.getAddress(), String.valueOf(stopTime)};
                System.out.println("FIRST "+ currentDevices.getSpot() +": "+ currentDevices.print());
                if(currentDevices.getSpot() > -1) {
                    currentDevices.set(currentDevices.getSpot(), tempList);
                }
                else {
                    currentDevices.add(tempList);
                }
                System.out.println("AFTER "+ currentDevices.getSpot() +": "+ currentDevices.print());

                outputStream = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
                FileWriter fw = new FileWriter(outputStream.getFD());

                String output = "";
                System.out.println("device size = "+ currentDevices.getSize());
                for(int i = 0; i < currentDevices.getSize(); i++) {
                    output += currentDevices.getName(i) + ',' + currentDevices.getAddress(i) + ',' + currentDevices.getTime(i) +'\n';
                }
                System.out.println("WRITE-OUT: " + output);
                fw.write(output);
                fw.close();

                inputStream = context.openFileInput(FILENAME);
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder builder = new StringBuilder();
                String line;
                while((line = br.readLine()) != null) {
                    builder.append("++ " + line);
                    builder.append('\n');
                }
                System.out.println("We built this house: " + builder.toString());

                Toast.makeText(context, "This one has lasted: " + stopTime + " ayyy.", Toast.LENGTH_LONG).show();

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





