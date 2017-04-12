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
                //Toast.makeText(context, "BT: Name: " + device.getName() +
                //        "\nBluetooth Device Address: " + device.getAddress(), Toast.LENGTH_LONG).show();

            timer.reset();

                try{
                    currentDevices.readFile(context);

                    currentDevices.feed(device.getName(), device.getAddress());

                    System.out.println(currentDevices.print());
                }
                catch(FileNotFoundException ex) {
                    currentDevices.feed(device.getName(), device.getAddress());
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
                System.out.println("FIRST "+ currentDevices.getSpot() +": "+ currentDevices.print());
                currentDevices.feed(device.getName(), device.getAddress());
                System.out.println("AFTER "+ currentDevices.getSpot() +": "+ currentDevices.print());

                currentDevices.save(context);

                /*inputStream = context.openFileInput(FILENAME);
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder builder = new StringBuilder();
                String line;
                while((line = br.readLine()) != null) {
                    builder.append("++ " + line);
                    builder.append('\n');
                }
                System.out.println("We built this house: " + builder.toString());

                Toast.makeText(context, "This one has lasted: " + lapsedTime + " ayyy.", Toast.LENGTH_LONG).show();*/

            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
            timer.stop();
        }
        else {
            //Toast.makeText(context, "Non-Bluetooth Broadcast Intent Detected: " + action, Toast.LENGTH_LONG).show();
        }
    }
}





