package com.jadeinc.habitracker;

/**
 * Created by ali on 4/24/17.
 */

public class ModelTask {
    String task;
    boolean enable;

    ModelTask(String name, boolean val) {
        task = name;
        enable = val;
    }

    public String getName() {
        return task;
    }

    public boolean isEnabled() {
        return enable;
    }
}
