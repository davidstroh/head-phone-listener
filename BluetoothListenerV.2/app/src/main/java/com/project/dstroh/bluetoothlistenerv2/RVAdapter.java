package com.project.dstroh.bluetoothlistenerv2;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class RVAdapter extends RecyclerView.Adapter<RVAdapter.DeviceViewHolder> {

    List<Person> persons;
    DeviceList currentDevices;

    public static class DeviceViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView deviceName;
        TextView deviceAddress;
        TextView deviceDuration;

        DeviceViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            deviceName=(TextView)itemView.findViewById(R.id.device_name);
            deviceAddress = (TextView)itemView.findViewById(R.id.device_address);
            deviceDuration = (TextView)itemView.findViewById(R.id.device_duration);
        }
    }


    //public RVAdapter(List<Person> persons) {
    public RVAdapter(Context context) {
        currentDevices = DeviceList.getInstance(context);
        System.out.println("SIZEHERESIZEHERE:::::: " + currentDevices.getSize());
    }

    @Override
    public int getItemCount() {
        return currentDevices.getSize();
    }

    public void updateData() {
        notifyDataSetChanged();
    }

    @Override
    public DeviceViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        DeviceViewHolder dvh = new DeviceViewHolder(v);
        return dvh;
    }

    @Override
    public void onBindViewHolder(DeviceViewHolder personViewHolder, int i) {
        //personViewHolder.personName.setText(persons.get(i).name);
        personViewHolder.deviceName.setText(currentDevices.getName(i));
        //personViewHolder.personAge.setText(persons.get(i).age);
        personViewHolder.deviceAddress.setText(currentDevices.getAddress(i));
        personViewHolder.deviceDuration.setText(Double.toString(currentDevices.getTime(i)));
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
