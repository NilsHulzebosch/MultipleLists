package com.example.nils.multiplelists;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class MainDBhelper extends SQLiteOpenHelper {

    // set fields of database schema
    private static final String DATABASE_NAME = "myMasterDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "contacts";

    private String todo_id = "toDo";

    // constructor
    public MainDBhelper(Context context, String db) {
        super(context, db, null, DATABASE_VERSION);
    }

    // onCreate: create new table (this function will be executed automatically)
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
                " ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                todo_id + " TEXT)";
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
    void create(ToDoList toDoList) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(todo_id, toDoList.title);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    // read
    ArrayList<ToDoList> read() {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT _id , " + todo_id + " FROM " + TABLE_NAME;

        ArrayList<ToDoList> toDoListList = new ArrayList<>();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                String toDo = cursor.getString(cursor.getColumnIndex(todo_id));
                int id = cursor.getInt(cursor.getColumnIndex("_id"));
                toDoListList.add(new ToDoList(toDo, id));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return toDoListList;
    }

    // update
    void update(int id, ToDoList toDoList) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(todo_id, toDoList.title);
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
