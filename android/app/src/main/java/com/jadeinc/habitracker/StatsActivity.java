package com.jadeinc.habitracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class StatsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    public void onTasksClick(View view) {
        Intent myIntent = new Intent(this, HabitList.class);
        startActivity(myIntent);
    }

    public void onAddTaskClick(View view) {
        Intent i = new Intent(this, NewTask.class);
        startActivity(i);
    }

}
