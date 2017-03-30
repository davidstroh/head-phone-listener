package com.project.dstroh.bluetoothlistenerv2;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;



public class MyReceiver extends BroadcastReceiver {

    public MyReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if(BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if(device.getName().equals("G18"))
                Toast.makeText(context, "G18: Name: " + device.getName() +
                        "\nBluetooth Device Address: " + device.getAddress(), Toast.LENGTH_LONG).show();
            else
                Toast.makeText(context, "Anotha one: Name: " + device.getName() +
                                "\nBluetooth Device Address: " + device.getAddress(), Toast.LENGTH_LONG).show();
        }
        else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if(device.getName().equals("G18"))
                Toast.makeText(context, "This one: " + device.getName() + " disconnected.", Toast.LENGTH_LONG).show();

            else
                Toast.makeText(context, "A different BT Device  " + device.getName() +
                        " disconnected.", Toast.LENGTH_LONG).show();

        }
        else {
            Toast.makeText(context, "Non-Bluetooth Broadcast Intent Detected: " + action, Toast.LENGTH_LONG).show();
        }
    }
}