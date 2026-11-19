package com.example.notesapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class NotesDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "notes.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NOTES = "notes";
    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_CONTENT = "content";

    public NotesDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTable = "CREATE TABLE " + TABLE_NOTES + "(" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_NAME + " TEXT," +
                COL_CONTENT + " TEXT" +
                ")";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        onCreate(db);
    }


    public boolean addNote(String name, String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_CONTENT, content);

        long result = db.insert(TABLE_NOTES, null, values);
        db.close();
        return result != -1;
    }

    public ArrayList<String> getAllNoteNames() {
        ArrayList<String> names = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT " + COL_NAME + " FROM " + TABLE_NOTES + " ORDER BY " + COL_ID + " DESC",
                null
        );

        if (cursor.moveToFirst()) {
            do {
                names.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return names;
    }

    public boolean deleteNoteByName(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        int deletedRows = db.delete(TABLE_NOTES, COL_NAME + " = ?", new String[]{name});
        db.close();
        return deletedRows > 0;
    }


    public String getNoteContentByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String content = "";

        Cursor cursor = db.rawQuery(
                "SELECT " + COL_CONTENT + " FROM " + TABLE_NOTES + " WHERE " + COL_NAME + " = ? LIMIT 1",
                new String[]{name}
        );

        if (cursor.moveToFirst()) {
            content = cursor.getString(0);
        }

        cursor.close();
        db.close();
        return content;
    }
}
