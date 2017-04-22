package com.jadeinc.habitracker;

/**
 * Created by evan on 4/20/17.
 */
public class Task {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
