package com.example.nils.multiplelists;

import java.io.Serializable;

public class ToDoItem implements Serializable {

    String item;
    Boolean completed;
    int id;

    public ToDoItem(String item) {
        this.item = item;
        this.completed = false;
    }

    public ToDoItem(String item, Boolean completed, int id) {
        this.item = item;
        this.completed = completed;
        this.id = id;
    }

    public void changeState() {
        completed ^= true;
    }

}
