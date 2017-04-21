package com.jadeinc.habitracker;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.loopj.android.http.AsyncHttpClient;

import java.util.List;

public class HabitList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("hi", "trying");
        DBConnector dbc = new DBConnector(new AsyncHttpClient());
        dbc.getUsers(new DBListener() {
            @Override
            public void onSuccess(List<User> users) {
                Log.v("HabitList", users.toString());
            }

            @Override
            public void onFailure(String s) {

            }
        });

        setContentView(R.layout.activity_habit_list);
    }
}
