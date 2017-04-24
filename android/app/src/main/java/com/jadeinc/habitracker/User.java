package com.jadeinc.habitracker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by evan on 4/20/17.
 */

public class User {
    private String email;
    private String name;
    private String username;
    private String password;
    private String userid;
    private List<Task> tasks = new ArrayList<Task>();

    public User(String userid, String name, String email, String username, String password) {
        this.userid = userid;
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public User() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserid(String userid) { this.userid = userid; }

    public String getUserid() {return this.userid;}

    public void setTasks(List tasks) {
        this.tasks = tasks;
    }

    public List<Task> getTasks() {
        return this.tasks;
    }

    public void addTask(Task task) {
        this.tasks.add(task);
    }

    @Override
    public String toString() {
        String s = "name: " + this.name + ", tasks: ";
        s += this.tasks.toString();
        return s;
    }

}