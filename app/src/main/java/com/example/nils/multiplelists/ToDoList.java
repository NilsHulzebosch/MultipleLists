package com.example.nils.multiplelists;

import java.io.Serializable;
import java.util.ArrayList;

public class ToDoList implements Serializable {

    String title;
    ArrayList<ToDoItem> toDoArrayList;
    int id;

    public ToDoList(String title) {
        this.title = title;
        toDoArrayList = new ArrayList<>();
    }

    public ToDoList(String title, int id) {
        this.title = title;
        this.id = id;
        toDoArrayList = new ArrayList<>();
    }
}
