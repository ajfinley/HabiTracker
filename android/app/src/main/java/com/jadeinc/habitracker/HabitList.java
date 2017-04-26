package com.jadeinc.habitracker;


import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.util.Log;

import java.util.List;

import static android.provider.AlarmClock.EXTRA_MESSAGE;


public class HabitList extends AppCompatActivity {



    public static final String TAG = "HabitList";
    private ListView lv;
    private List<Task> taskDisplay;
    private DBDataReceiver receiver;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IntentFilter filter = new IntentFilter(DBDataReceiver.ACTION_RESP);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new DBDataReceiver();
        registerReceiver(receiver, filter);
        Intent dbIntent = new Intent(this, DBService.class);
        startService(dbIntent);

        setContentView(R.layout.activity_habit_list);
        lv = (ListView) findViewById(R.id.lv_today);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Log.v(TAG, "working");
                Intent intent = new Intent(HabitList.this, Adapter.class);
                String message = "abc";
                intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(intent);
            }
        });

        TextView tvDisplayDate0 = (TextView) findViewById(R.id.tvDate0);
        //TextView tvDisplayDate1 = (TextView) findViewById(R.id.tvDate1);
        //TextView tvDisplayDate2 = (TextView) findViewById(R.id.tvDate2);


        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();

        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrowtomorrow = calendar.getTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMM dd, yyyy");

        String todayAsString = dateFormat.format(today);
        String tomorrowAsString = dateFormat.format(tomorrow);
        String tomorrowtomorrowAsString = dateFormat.format(tomorrowtomorrow);


        tvDisplayDate0.setText(todayAsString);
        //tvDisplayDate1.setText(tomorrowAsString);
        //tvDisplayDate2.setText(tomorrowtomorrowAsString);
        this.taskDisplay = new ArrayList<Task>();//(Task[]) tasks.toArray();
        taskDisplay.add(new Task("water plants"));
        Adapter adapter = new Adapter(this, taskDisplay);
        lv.setAdapter(adapter);
    }

    public void onCheckBoxClicked(View view) {
        CheckBox checkBox = (CheckBox) view;
        String taskName = checkBox.getTag().toString();
        Task task = user.getTaskByName(taskName);
        if(checkBox.isChecked()) {
            task.complete();
            Log.v(TAG, "completed at: " + user.getTaskByName(taskName).getTimeCompleted());
        } else {
            task.setTimeCompleted(task.getTimeCompleted() - 90000);
        }
        new DBService().postUser(this.user);
    }


    public void generateTaskList(List<User> users) {
        user = users.get(0);
        List<Task> tasks = user.getTasks();
        ((Adapter)lv.getAdapter()).updateTasks(tasks);
    }







//    public void onCheckboxClicked(View view) {
//        boolean checked = ((CheckBox) view).isChecked();
//
//        switch(view.getId()) {
//            case R.id.checkbox_item0:
//                if (checked) {
//                    System.out.print("This is if statement");
//                }
//                else
//                    break;
//                // TODO: We need to think of a way to populate the list to come up
//        }
//    }


}
