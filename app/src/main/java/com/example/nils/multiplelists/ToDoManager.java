package com.example.nils.multiplelists;


import java.util.ArrayList;

public class ToDoManager {
    private static ToDoManager ourInstance = new ToDoManager();
    private static ArrayList<ToDoList> toDoListArrayList;

    public static ToDoManager getInstance() {
        return ourInstance;
    }

    // constructor is private!
    private ToDoManager() {
        toDoListArrayList = new ArrayList<>();
    }

    public void setToDoManager(ArrayList<ToDoList> manager) {
        toDoListArrayList = manager;
    }

    public ArrayList<ToDoList> getToDoManager() {
        return toDoListArrayList;
    }
}
