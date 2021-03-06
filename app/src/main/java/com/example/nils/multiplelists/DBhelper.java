package com.example.nils.multiplelists;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBhelper extends SQLiteOpenHelper {

    // set fields of database schema
    private static final String DATABASE_NAME = "myDetailedDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "contacts";

    private String todo_id = "toDo";
    private String todo_check = "completed";

    // constructor
    public DBhelper(Context context, String dbName) {
        super(context, dbName, null, DATABASE_VERSION);
    }

    // onCreate: create new table (this function will be executed automatically)
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
                " ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                todo_id + " TEXT, " +
                todo_check + " TEXT)";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    // onUpgrade: delete existing table & create new one (will be executed automatically)
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }


    // CRUD methods (create, read, update, delete)

    // create
    void create(ToDoItem toDoItem) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(todo_id, toDoItem.item);
        values.put(todo_check, toDoItem.completed);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    // read
    ArrayList<ToDoItem> read() {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT _id , " + todo_id + " , " + todo_check + " FROM " + TABLE_NAME;

        ArrayList<ToDoItem> toDoListItems = new ArrayList<>();
        Cursor cursor = db.rawQuery(query, null);
        ToDoItem toDoListItem;
        if (cursor.moveToFirst()) {
            do {
                String toDo = cursor.getString(cursor.getColumnIndex(todo_id));
                int id = cursor.getInt(cursor.getColumnIndex("_id"));
                Boolean checkedItem = cursor.getInt(cursor.getColumnIndex(todo_check)) > 0;
                toDoListItem = new ToDoItem(toDo, checkedItem, id);
                toDoListItems.add(toDoListItem);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return toDoListItems;
    }

    // update
    void update(int id, ToDoItem toDoItem) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(todo_id, toDoItem.item);
        values.put(todo_check, toDoItem.completed);
        db.update(TABLE_NAME, values, "_id = ? ", new String[] {
                String.valueOf(id)
        });
        db.close();
    }

    // delete
    void delete(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, " _id = ? ", new String[]{
                String.valueOf(id)
        });
        db.close();
    }
}
