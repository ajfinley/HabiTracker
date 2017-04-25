package com.jadeinc.habitracker;

/**
 * Created by ali on 4/24/17.
 */
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class Adapter extends ArrayAdapter<ModelTask>{
        ModelTask[] tasks = null;
        Context context;

        public Adapter(Context context, ModelTask[] resource) {
            super(context,R.layout.task_row, resource);
            this.context = context;
            this.tasks = resource;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.task_row, parent, false);
            TextView name = (TextView) convertView.findViewById(R.id.textView1);
            CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkBox1);
            name.setText(tasks[position].getName());
            if(tasks[position].isEnabled()) {
                cb.setChecked(true);
            } else {
                cb.setChecked(false);
            }
            return convertView;
        }
}
