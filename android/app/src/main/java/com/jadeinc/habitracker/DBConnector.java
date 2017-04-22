package com.jadeinc.habitracker;
import android.content.Context;
import android.util.Log;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.loopj.android.http.*;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by evan on 4/20/17.
 */
public class DBConnector {

    public static final String TAG = "DBConnector";

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

    //BROKEN!!
    public void postUser(User user) {
        RequestParams params = new RequestParams();
        params.add("TableName", "HabitTracker");
        ObjectWriter ow = new ObjectMapper().writerWithDefaultPrettyPrinter();
        Log.v(TAG, "before json: " + user.toString());
        String json = "";
        try {
            json = ow.writeValueAsString(user);
            Log.v(TAG, json);
        } catch (JsonProcessingException jpe) {
            Log.e(TAG, "failed to convert user to json: " + jpe.toString());
        }
        //params.add("Items", json);

        client.post(DATABASE_ENDPOINT, params, new TextHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e(TAG, responseString + throwable.toString());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.v(TAG, "worked");
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
