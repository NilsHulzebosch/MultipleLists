package com.example.nils.multiplelists;

import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private ArrayList<ToDoList> toDoLists;
    private MasterAdapter toDoListAdapter;
    private ListView toDoListView;
    private EditText userInputET;
    private MainDBhelper dBhelper;
    private ToDoManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeParams();
        initializeAdapter();
        updateAdapter(); // update UI
    }

    public void initializeParams() {
        userInputET = (EditText) findViewById(R.id.inputEditText);
        dBhelper = new MainDBhelper(this, "myMasterDB");
        manager = ToDoManager.getInstance();
        toDoLists = manager.getToDoManager();
    }


    public void initializeAdapter() {
        toDoListView = (ListView) findViewById(R.id.toDoListView);
        toDoListAdapter = new MasterAdapter(this, toDoLists);
        toDoListView.setAdapter(toDoListAdapter);

        // on short click
        toDoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                goToList(i);
            }
        });

        // on long click
        toDoListView.setLongClickable(true);
        toDoListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                ToDoList toDoList = (ToDoList) toDoListView.getItemAtPosition(pos);
                int item_id = toDoList.id;
                dBhelper.delete(item_id);
                updateAdapter(); // update UI
                Toast.makeText(arg1.getContext(), "Removed from to-do list",
                        Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    public void updateAdapter() {
        toDoLists.clear(); // clear to make room for new database
        toDoLists.addAll(dBhelper.read()); // add database
        toDoListAdapter.notifyDataSetChanged();
    }

    // when pressing the save button, this method saves the user input to the database
    public void saveToDoList(View view) {
        String currentUserInput = userInputET.getText().toString();

        // check whether there is an input
        if (!(currentUserInput.length() == 0)) {
            ToDoList toDoList = new ToDoList(currentUserInput);
            dBhelper.create(toDoList);
            Toast.makeText(this, "Added to to-do list", Toast.LENGTH_SHORT).show();

            userInputET.setText("");
            InputMethodManager imm = (InputMethodManager) this.getSystemService(Service.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(userInputET.getWindowToken(), 0);

            updateAdapter(); // update UI
        } else {
            // notify to type something before saving
            Toast.makeText(this, "Enter something you need to do", Toast.LENGTH_SHORT).show();
        }
    }

    public void showDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Delete entry");
        dialog.setMessage("Are you sure you want to delete this entry?");

        dialog.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // continue with delete
            }
        });
        dialog.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // do nothing
            }
        });
        dialog.show();
    }

    public void goToList(int pos) {
        Intent goToList = new Intent(this, ShowToDoList.class);
        goToList.putExtra("toDoList", manager.getToDoManager().get(pos)); // pas toDoList object to next activity
        startActivity(goToList);
    }
}
