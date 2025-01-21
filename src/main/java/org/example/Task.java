package org.example;

import java.util.List;
import java.util.ArrayList;

public class Task {
    public int id;
    public List<Task> dependencies;
    public boolean isCompleted;
    public int[] array;
    public int start, end;


    public Task(int id) {
        this.id = id;
        this.dependencies = new ArrayList<>();
        this.isCompleted = false;
    }


    public Task(int id, int[] array, int start, int end) {
        this.id = id;
        this.array = array;
        this.start = start;
        this.end = end;
        this.dependencies = new ArrayList<>();
        this.isCompleted = false;
    }
}

