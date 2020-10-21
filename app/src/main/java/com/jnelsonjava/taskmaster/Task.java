package com.jnelsonjava.taskmaster;

public class Task {
    public String title;
    public String body;

    // state should only be new, assigned, in progress, or complete
    // consider changing this to a reference to the options
    public String state;

    public Task(String title, String body, String state) {
        this.title = title;
        this.body = body;
        this.state = state;
    }
}
