package com.project.dstroh.bluetoothlistenerv2;

import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;


import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;


public class DeviceList {
    private static DeviceList instance;
    private Context context;

    Map<String, Device> deviceMap;
    Map<Integer, String> keyMap;

    private final String FILENAME = "btDevicesTimeFile";
    FileOutputStream outputStream;
    FileInputStream inputStream;


    // CONSTRUCTORS

    private DeviceList() {
        deviceMap = new HashMap<String,Device>();
        keyMap = new HashMap<Integer, String>();
    }

    public static synchronized DeviceList getInstance(Context context) {
        if(instance == null) {
            instance = new DeviceList();
        }
        instance.context = context.getApplicationContext();
        try {
            if(instance.timerOpen() < 0) {
                    instance.readFile();
            }
            else {
                instance.saveFile();
            }
        }
        catch(FileNotFoundException ex) {
            //throw ex;
            System.out.println("BABABABABA BONONO FILE YET");
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        return instance;
    }



    // MUTATORS

    public Device add(String name, String address) {
        Device input = new Device(name, address);
        deviceMap.put(name, input);
        keyMap.put(keyMap.size(), name);
        return input;
    }

    public Device add(String name, String address, double time) {
        Device input = new Device(name, address, time);
        deviceMap.put(name, input);
        keyMap.put(keyMap.size(), name);
        return input;
    }

    public boolean feed(String name, String address) {
        System.out.println(!deviceMap.containsKey(name));
        if(!deviceMap.containsKey(name)) {
            add(name, address);
            return true;
        }
        Device tempDevice = deviceMap.get(name);
        String[] addressList = tempDevice.getAddress().split("\n");
        String prevAddress = addressList[addressList.length - 1];
        if(!prevAddress.equals(address)) {
            tempDevice.appendAddress(address);
            deviceMap.put(name, tempDevice);
        }

        return false;
    }

    public void set(int index, String[] input) {
        //String[] newInput = {input[0], input[1], input[2]};
        //devices.set(index, newInput);
    }


    public void clear() {
        deviceMap = new HashMap<String,Device>();
        keyMap = new HashMap<Integer, String>();
    }

    public void start(String key) {
        System.out.println(deviceMap.get(key));
        if(deviceMap.get(key) != null) {
            deviceMap.get(key).start();
        }
        else {
            System.out.println("start method error: no device matches specified key: " + key);
        }
    }
    public void start(int index) {
        if(keyMap.get(index) != null && deviceMap.get(keyMap.get(index)) != null) {
            deviceMap.get(keyMap.get(index)).start();
        }
        else {
            System.out.println("start method error: no device matches specified index: " + index);
        }
    }
    public double stop(String key) {
        if(deviceMap.get(key) != null) {
            return deviceMap.get(key).stop();
        }
        else {
            System.out.println("DeviceList - start method - error: no device matches specified key: " + key);
            return -1;
        }
    }
    public double stop(int index) {
        if(keyMap.get(index) != null && deviceMap.get(keyMap.get(index)) != null) {
            return deviceMap.get(keyMap.get(index)).stop();
        }
        else {
            System.out.println("DeviceList - start method - error: no device matches specified index: " + index);
            return -1;
        }
    }


    // ACCESSORS

    public String getKey(int index) {
        return keyMap.get(index);
    }

    public String getName(String key) {
        return (deviceMap.get(key).getName());
    }
    public String getName(int index) {
        return deviceMap.get(keyMap.get(index)).getName();
    }
    public String getAddress(String key) {
        return (deviceMap.get(key).getAddress());
    }
    public String getAddress(int index) {
        return deviceMap.get(keyMap.get(index)).getAddress();
    }
    public Double getTime(String key) {
        return (deviceMap.get(key).getTime());
    }
    public Double getTime(int index) {
        return deviceMap.get(keyMap.get(index)).getTime();
    }

    public int getSize() {
        return deviceMap.size();
    }

    public int containsKey(String key) {
        for(int i = 0; i < keyMap.size(); i++)
            if(keyMap.get(i).equals(key))
                return i;

        return -1;
    }

    public boolean hasStarted(String key) {
        return deviceMap.get(key).isGoing();
    }

    public int timerOpen() {
        for(int i = 0; i < keyMap.size(); i++) {
            if(deviceMap.get(keyMap.get(i)).isGoing()) {
                return i;
            }
        }
        return -1;
    }


    // FILE HANDLING

    public void readFile() throws Exception {
        try{
            inputStream = context.openFileInput(FILENAME);
            clear();

            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while((line = br.readLine()) != null) {
                String[] words = line.split(",");
                add(words[0], words[1], Double.parseDouble(words[2]));
            }
            inputStream.close();
        }
        catch(FileNotFoundException ex) {
            throw ex;
        }
        catch(Exception ex) {
            throw ex;
        }
    }

    public void saveFile() throws Exception {
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(context.openFileOutput(FILENAME, Context.MODE_PRIVATE)));

        for(int i = 0; i < getSize(); i++) {
            out.write(getName(i) + ',' + getAddress(i) + ',' + getTime(i) +'\n');
        }
        if(out != null) {
            out.close();
        }
    }

}

