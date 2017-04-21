package com.jadeinc.habitracker;

import java.util.List;

/**
 * Created by evan on 4/20/17.
 */

public class User {
    private String email;
    private String name;
    private String username;
    private String password;
    private List<Task> tasks;

    public User(String name, String email, String username, String password) {
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
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setTasks(List tasks) {
        this.tasks = tasks;
    }

    public List<Task> getTasks() {
        return this.tasks;
    }

    @Override
    public String toString() {
        String s = "name: " + this.name + ", tasks: ";
        for(Task task : this.tasks) {
            s += task.toString() + ", ";
        }
        return s;
    }

}
