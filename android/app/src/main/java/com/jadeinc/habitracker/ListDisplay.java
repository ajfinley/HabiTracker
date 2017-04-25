package com.jadeinc.habitracker;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

import java.util.List;

public class ListDisplay extends Activity {
    // Array of strings...
    String[] mobileArray = {"Android","IPhone","WindowsMobile","Blackberry",
            "WebOS","Ubuntu","Windows7","Max OS X"};

    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callDB();
        setContentView(R.layout.activity_list_main);


        adapter = new ArrayAdapter<String>(this, R.layout.list_row, mobileArray);


        ListView listView = (ListView) findViewById(R.id.mobile_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

    }

    public void callDB() {
        new DBConnector().getUsers(new DBListener() {
            @Override
            public void onSuccess(List<User> users) {
                mobileArray[0] = users.get(0).getTasks().get(0).getTask();
                adapter.notifyDataSetChanged();
                Log.v("fuck", "this");
            }
        });
    }

    public void onCheckboxClicked(View view) {
        CheckedTextView ctv = (CheckedTextView) view;
        ctv.setChecked(!ctv.isChecked());
        Log.v("hi", ctv.getText().toString());
    }
}