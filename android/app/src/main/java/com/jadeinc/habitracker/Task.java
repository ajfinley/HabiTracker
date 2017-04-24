package com.jadeinc.habitracker;

/**
 * Created by evan on 4/20/17.
 */
public class Task {
    private String task;
    private String time;
    private String frequency;
    private String timeCompleted;

    public Task() {}

    public Task(String task) {
        this.task = task;
    }

    public String getTask() {
        return task;
    }
    public void setTask(String task) {
        this.task = task;
    }

    public String getTime() { return this.time;}
    public void setTime(String time) { this.time = time;}

    public String getFrequency() { return this.frequency; }
    public void setFrequency(String frequency) { this.frequency = frequency; }

    public String getTimeCompleted() {return this.timeCompleted;}
    public void setTimeCompleted(String timeCompleted) {this.timeCompleted = timeCompleted;}

    @Override
    public String toString() {
        return this.task;
    }
}
