package com.jadeinc.habitracker;

/**
 * Created by ali on 4/24/17.
 */
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.List;

public class StatsAdapter extends BaseAdapter {
    private List<Task> tasks;
    private Context context;

    public StatsAdapter(Context mcontext, List<Task> t) {
        context = mcontext;
        tasks = t;
    }

    @Override
    public int getCount() {
        try {
            int size = tasks.size();
            return size;
        } catch(NullPointerException o) {
            return 0;
        }
    }

    public void updateTasks(List<Task> t) {
        tasks.clear();
        tasks.addAll(t);
        this.notifyDataSetChanged();
    }

    @Override
    public Task getItem(int i) {
        try {
            Task t = tasks.get(i);
            return t;
        } catch (NullPointerException e) {
            return null;
        }
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.stats_row, parent, false);
        TextView name = (TextView) convertView.findViewById(R.id.textViewStats);
        name.setText(tasks.get(position).getTask());
        TextView bestStreak = (TextView) convertView.findViewById(R.id.bestStreak);
        bestStreak.setText("best streak: " + String.valueOf(tasks.get(position).bestStreak));
        TextView currStreak = (TextView) convertView.findViewById(R.id.currStreak);
        currStreak.setText("current streak: " + String.valueOf(tasks.get(position).currentStreak));
        TextView lastCompleted = (TextView) convertView.findViewById(R.id.lastCompleted);
        lastCompleted.setText("last completed: " + String.valueOf(tasks.get(position).getTimeCompleted()));
        return convertView;
    }
}
