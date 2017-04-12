package com.project.dstroh.bluetoothlistenerv2;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String FILENAME = "btDevicesTimeFile";

    private ListView mListView;
    DeviceList devices;

    private List<Person> persons;
    private RecyclerView rv;
    RVAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //deleteFile(FILENAME);

        rv = (RecyclerView)findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        initializeData();
        initializeAdapter();



        //mListView = (ListView)findViewById(R.idcard.recipe_list_view);

        final ArrayList<String> recipeList = new ArrayList();
        recipeList.add("hey");recipeList.add("there");recipeList.add("dewy");

        String[] listItems = {"hey", "there", "dewy", "there", "dewy", "there", "dewy", "there", "dewy", "there", "dewy", "there", "dewy", "there", "dewy", "there", "dewy", "there", "dewy", "there", "dewy"};

        /*ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems);
        mListView.setAdapter(adapter);*/
    }


    //This method creates an ArrayList that has three Person objects
    // Checkout the project associated with this tutorial on Github if
    // you want to use the same images.
    private void initializeData() {
        devices = DeviceList.getInstance();
        try{devices.readFile(this);}
        catch(Exception ex){ex.printStackTrace();}
        //persons = new ArrayList<>();
        //persons.add(new Person("Emma Wilson", "23 years old"));
        //persons.add(new Person("Lavery Maiss", "25 years old"));
        //persons.add(new Person("Lillie Watts", "35 years old"));
    }

    private void initializeAdapter() {
        //RVAdapter adapter = new RVAdapter(persons);
        adapter = new RVAdapter();
        rv.setAdapter(adapter);
    }

    public void updateData(View view) {
        try{devices.save(this);}
        catch(Exception ex){ex.printStackTrace();}
        adapter.updateData();
    }

}
