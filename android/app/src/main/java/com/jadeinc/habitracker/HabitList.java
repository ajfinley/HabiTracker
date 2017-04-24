package com.jadeinc.habitracker;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;


public class HabitList extends AppCompatActivity {

    public static final String TAG = "HabitList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new DBConnector().getUsers(new DBListener() {
            @Override
            public void onSuccess(List<User> users) {
                Log.v(TAG, users.toString());
            }
        });
        setContentView(R.layout.activity_habit_list);
    }
}
