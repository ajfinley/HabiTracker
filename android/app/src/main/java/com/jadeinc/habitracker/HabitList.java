package com.jadeinc.habitracker;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import android.widget.CheckBox;
import android.util.Log;

import java.util.List;


public class HabitList extends AppCompatActivity {

    public static final String TAG = "HabitList";
    private ListView lv;
    private List<Task> taskDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeCallDB();

        setContentView(R.layout.activity_habit_list);
        lv = (ListView) findViewById(R.id.lv_today);

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

    private void makeCallDB() {
        new DBConnector().getUsers(new DBListener() {
            @Override
            public void onSuccess(List<User> users) {
                Log.v(TAG, users.toString());
                generateListTask(users);
            }
        });
    }

    private void generateListTask(List<User> users) {
        User user = users.get(0);
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
