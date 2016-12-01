package com.example.nils.multiplelists;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class MasterAdapter extends ArrayAdapter<ToDoList> {

    public MasterAdapter(Context context, ArrayList<ToDoList> items) {
        super(context, R.layout.simplest_row_layout, items);
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.simplest_row_layout, parent, false);

        final ToDoList toDoListItem = getItem(position);
        String item = "No entries yet";
        if (toDoListItem != null) {
            item = toDoListItem.title;
        }

        TextView textView = (TextView) view.findViewById(R.id.textView1);
        textView.setText(item);

        return view;

    }

}
