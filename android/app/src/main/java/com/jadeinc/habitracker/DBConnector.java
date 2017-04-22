package com.jadeinc.habitracker;
import android.util.Log;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by evan on 4/20/17.
 */
public class DBConnector {

    public static final String TAG = "DBConnector";

    public static final String DATABASE_ENDPOINT = "https://w06sur7dz1.execute-api.us-east-1.amazonaws.com/prod/HabiTrackerUpdate?TableName=HabitTracker";

    public DBConnector() {
    }

    public void postUser(final User user) {
        new Thread() {
            public void run() {
                String rootString = getPostString(user);
                Unirest.post(DATABASE_ENDPOINT)
                        .header("accept", "application/json")
                        .body(rootString)
                        .asStringAsync(new Callback<String>() {
                    @Override
                    public void completed(HttpResponse<String> httpResponse) {
                        Log.v(TAG, "completed: " + httpResponse.getBody().toString());
                    }

                    @Override
                    public void failed(UnirestException e) {
                        Log.v(TAG, "failed");
                    }

                    @Override
                    public void cancelled() {

                    }
                });
            }
        }.start();
    }

    private String getPostString(User user) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String userString = mapper.writeValueAsString(user);
            JsonNode node = mapper.readTree(userString);
            ObjectNode root = mapper.createObjectNode();
            root.put("TableName", "HabitTracker");
            root.put("Item", node);
            return mapper.writeValueAsString(root);
        } catch (Exception e) {
            Log.e(TAG, "error making string", e);
        }
        return null;
    }

    public void getUsers(final DBListener dbListener) {
        new Thread() {
            public void run() {
                Unirest.get(DATABASE_ENDPOINT).asStringAsync(new Callback<String>() {
                    @Override
                    public void completed(HttpResponse<String> httpResponse) {
                        List<User> users = generateUsers(httpResponse.getBody());
                        dbListener.onSuccess(users);
                    }

                    @Override
                    public void failed(UnirestException e) {
                        Log.e(TAG, e.toString());
                    }

                    @Override
                    public void cancelled() {
                        Log.e(TAG, "cancelled");
                    }
                });
            }
        }.start();
    }

    private List<User> generateUsers(String response) {
        List<User> users = new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node =  mapper.readTree(response).get("Items");
            for (JsonNode userJson : node) {
                User user = mapper.readValue(userJson.toString(), User.class);
                List<Task> tasks = new ArrayList<>();
                for (JsonNode taskJson : userJson.get("tasks")) {
                    Task task = mapper.readValue(taskJson.toString(), Task.class);
                    tasks.add(task);
                }
                user.setTasks(tasks);
                users.add(user);
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            Log.e(TAG, sw.toString());
        }
        return users;
    }

}
