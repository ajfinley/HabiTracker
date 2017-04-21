package com.jadeinc.habitracker;
import android.util.Log;
import com.loopj.android.http.*;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.ResponseHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by evan on 4/20/17.
 */
public class DBConnector {

    public static final String DATABASE_ENDPOINT = "https://w06sur7dz1.execute-api.us-east-1.amazonaws.com/prod/HabiTrackerUpdate?TableName=HabitTracker";
    private AsyncHttpClient client;

    public DBConnector(AsyncHttpClient client) {
        this.client = client;
    }

    public void getUsers(final DBListener listener) {
        Log.e("connnector", "getting users");

        client.get(DATABASE_ENDPOINT, new TextHttpResponseHandler() {

            @Override
            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
                Log.v("DBConnector", s);
                listener.onFailure(s);
            }

            @Override
            public void onSuccess(int ii, Header[] headers, String s) {
                List<User> users = generateUsers(s);
                listener.onSuccess(users);
                Log.v("users", users.toString());
            }
        });
    }

    public void postUser(User user) {
        RequestParams params = new RequestParams();

        client.post(DATABASE_ENDPOINT, params, new TextHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

            }
        });
    }

    private List<User> generateUsers(String userJSON) {
        List<User> users = new ArrayList<User>();
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(userJSON);
            JSONObject jo = (JSONObject) obj;
            JSONArray userList = (JSONArray) jo.get("Items");
            for(Object userObj : userList) {
                User user = generateUser((JSONObject) userObj);
                JSONArray jTasks = (JSONArray) ((JSONObject) userObj).get("tasks");
                user.setTasks(generateTasks(jTasks));
                users.add(user);
            }
        } catch (ParseException pe) {
            Log.v("DBConnector", pe.toString());
        }
        return users;
    }

    private User generateUser(JSONObject jUser) {
        User user = new User();
        user.setName((String) jUser.get("name"));
        user.setEmail((String) jUser.get("email"));
        user.setUsername((String) jUser.get("userid"));
        user.setPassword((String) jUser.get("password"));
        return user;
    }

    private List<Task> generateTasks(JSONArray jTasks) {
        List<Task> tasks = new ArrayList<>();
        for(Object taskObj : jTasks) {
            Task task = new Task();
            task.setName((String) ((JSONObject) taskObj).get("task"));
            tasks.add(task);
        }
        return tasks;
    }

}
