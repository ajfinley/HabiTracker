package com.jadeinc.habitracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aligator on 4/25/17.
 */

public class NewTask extends AppCompatActivity {
    public static final String TAG = "NewTask";
    EditText title;
    Spinner freq;
    Spinner time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_task);
        freq = (Spinner) findViewById(R.id.frequency);
        time = (Spinner) findViewById(R.id.time);
        //Button add = (Button) findViewById(R.id.add);
        title = (EditText) findViewById(R.id.name);

        List<String> repeat = new ArrayList<String>();
        repeat.add("Daily");
        repeat.add("Weekly");
        repeat.add("Monthly");

        List<String> times = new ArrayList<String>();
        times.add("Morning");
        times.add("Afternoon");
        times.add("Evening");

        ArrayAdapter<String> a1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, repeat);
        ArrayAdapter<String> a2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, times);

        a1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        a2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        freq.setAdapter(a1);
        time.setAdapter(a2);

//        add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(title.getText().toString().equals("") ||
//                        title == null) {
//                    Toast.makeText(getApplicationContext(),
//                            "Please enter a habit title",Toast.LENGTH_SHORT).show();
//                } else{
//                    Task t = new Task(title.getText().toString());
//                    t.setFrequency(freq.getSelectedItem().toString());
//                    t.setTime(time.getSelectedItem().toString());
//                    t.setTimeCompleted("0");
//                }
//            }
//        });
    }
}