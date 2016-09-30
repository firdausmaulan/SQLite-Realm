package com.sqlite.realm.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class SQLiteDB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "local.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "t_name";

    // Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";

    public SQLiteDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_NAME + " TEXT" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // insert data to table mylibs
    public void insert(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT " + KEY_NAME + " FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);
        boolean write = true;
        if (cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    if (name.equals(cursor.getString(cursor.getColumnIndex(KEY_NAME)))) {
                        write = false;
                    }
                } while (cursor.moveToNext());
            }
        }

        if (write == true) {
            ContentValues values = new ContentValues();
            values.put(KEY_NAME, name);
            db.insert(TABLE_NAME, null, values); // Inserting Row
        }
        db.close(); // Closing database connection
    }

    // Select & Show All Data
    public List<String> selectAll() {
        List<String> data = new ArrayList<String>();
        String selectQuery = "SELECT " + KEY_NAME + " FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                data.add(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return data;
    }

    public void delete(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "name='" + name + "'", null);
        db.close(); // Closing database connection
    }

    public void update(String oldName, String newName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, newName);
        db.update(TABLE_NAME, values, "name='" + oldName + "'", null);
        db.close();
    }
}
