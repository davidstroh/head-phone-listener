package com.project.dstroh.bluetoothlistenerv2;

import android.content.Context;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;


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
    private DeviceList(Context context) {
        deviceMap = new HashMap<String,Device>();
        keyMap = new HashMap<Integer, String>();
        try {
            this.context = context;
            readFile();
        }
        catch(FileNotFoundException ex) {
            //throw ex;
            System.out.println("BABABABABA BONONO FILE YET");
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public static synchronized DeviceList getInstance(Context context) {
        if(instance == null) {
            instance = new DeviceList(context);
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
        return deviceMap.get(key).hasStarted();
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
                System.out.println("FILE-IN: "+ line);
            }
            inputStream.close();
        }
        catch(FileNotFoundException ex) {
            throw ex;
            //System.out.println("BABABABABA BONONO FILE YET");
        }
        catch(Exception ex) {
            throw ex;
        }
    }

    public void saveFile() throws Exception {

        /*if(currentSpot > -1) {
            String[] tempArray = { getName(currentSpot), getAddress(currentSpot), Double.toString(timer.lapse()) };
            set(currentSpot, tempArray);
        }*/

        //try {
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
        //}
        //catch(Exception ex) {
        //    throw ex;
        //}
    }

}






/*public class DeviceList {
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
}*/