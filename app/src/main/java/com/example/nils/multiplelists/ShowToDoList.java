package com.example.nils.multiplelists;

import android.app.Service;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class ShowToDoList extends AppCompatActivity {

    private ArrayList<ToDoItem> toDoListItems;
    private CustomAdapter toDoListAdapter;
    private ListView toDoListView;
    private String currentUserInput;
    private EditText userInputET;
    private DBhelper dBhelper;
    private int changingItemID;

    private String dbName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_to_do_list);

        Intent intent = getIntent();
        ToDoList toDoList = (ToDoList) intent.getSerializableExtra("toDoList");
        dbName = toDoList.title;

        initializeParams();
        initializeAdapter();
        updateAdapter(); // update UI
    }


    public void initializeParams() {
        toDoListItems = new ArrayList<>();
        userInputET = (EditText) findViewById(R.id.inputEditText);
        dBhelper = new DBhelper(this, dbName);
        TextView textView = (TextView) findViewById(R.id.titleTextView);
        textView.setText(dbName);
    }

    @Override
    // this method saves the state of the program
    public void onSaveInstanceState(Bundle outState) {
        currentUserInput = userInputET.getText().toString();
        outState.putString("currentUserInput", currentUserInput);
        super.onSaveInstanceState(outState);
    }

    public void initializeAdapter() {
        toDoListView = (ListView) findViewById(R.id.toDoListView);
        toDoListAdapter = new CustomAdapter(this, toDoListItems);
        toDoListView.setAdapter(toDoListAdapter);

        // on short click
        toDoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                getID(i);
                ToDoItem toDoListItem = (ToDoItem) toDoListView.getItemAtPosition(i);
                toDoListItem.changeState();
                dBhelper.update(changingItemID, toDoListItem);
                updateAdapter(); // update UI
            }
        });

        // on long click
        toDoListView.setLongClickable(true);
        toDoListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                ToDoItem toDoListItem = (ToDoItem) toDoListView.getItemAtPosition(pos);
                int item_id = toDoListItem.id;
                dBhelper.delete(item_id);
                updateAdapter(); // update UI
                Toast.makeText(arg1.getContext(), "Removed from to-do list",
                        Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    public void updateAdapter() {
        toDoListItems.clear(); // clear to make room for new database
        toDoListItems.addAll(dBhelper.read()); // add database
        toDoListAdapter.notifyDataSetChanged();
    }

    // get the correct id matching the ToDoListItem
    public void getID(int pos) {
        ToDoItem item = toDoListItems.get(pos);
        changingItemID =  item.id;
    }

    // when pressing the save button, this method saves the user input to the database
    public void saveItem(View view) {
        currentUserInput = userInputET.getText().toString(); // turn user input into a string

        // check whether there is an input
        if (!(currentUserInput.length() == 0)) {
            ToDoItem toDoListItem = new ToDoItem(currentUserInput);
            dBhelper.create(toDoListItem);
            Toast.makeText(this, "Added to to-do list", Toast.LENGTH_SHORT).show();

            userInputET.setText("");
            InputMethodManager imm = (InputMethodManager) this.getSystemService(Service.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(userInputET.getWindowToken(), 0);
            userInputET.clearFocus();

            updateAdapter(); // update UI
        } else {
            // notify to type something before saving
            Toast.makeText(this, "Enter something you need to do", Toast.LENGTH_SHORT).show();
        }
    }
}
