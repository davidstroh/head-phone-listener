package com.project.dstroh.bluetoothlistenerv2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;



public class MyReceiver extends BroadcastReceiver {

    public MyReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Broadcast Intent Detected: " + intent.getAction(), Toast.LENGTH_LONG).show();
    }
}