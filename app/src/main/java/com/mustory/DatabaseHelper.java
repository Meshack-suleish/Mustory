package com.mustory;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "songs.db";
    private static final int DATABASE_VERSION = 1;

    // Table and column names
    private static final String TABLE_NAME = "downloaded_songs";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PATH = "path";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_ARTISTNAME = "artistName";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_PATH + " TEXT, "
                + COLUMN_DESCRIPTION + " TEXT, "
                + COLUMN_ARTISTNAME + " TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertSong(String name, String path, String description, String artistName) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, name);
            values.put(COLUMN_PATH, path);
            values.put(COLUMN_DESCRIPTION, description);
            values.put(COLUMN_ARTISTNAME, artistName);

         long result =  db.insert(TABLE_NAME, null, values);

            if (result == -1) {
                android.util.Log.e("DatabaseHelper", "Failed to insert song: " + name);
            } else {
                android.util.Log.d("DatabaseHelper", "Song inserted: " + name);
            }


        } finally {
            db.close();
        }
    }

    public List<Songs> getAllDownloadedSongs() {
        List<Songs> songList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

            if (cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                    @SuppressLint("Range") String path = cursor.getString(cursor.getColumnIndex(COLUMN_PATH));
                    @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
                    @SuppressLint("Range") String artistName = cursor.getString(cursor.getColumnIndex(COLUMN_ARTISTNAME));
                    String songNumber;

                   // Songs song = new Songs(name, path, description, artistName,songNumber);
                    //songList.add(song);
                    android.util.Log.d("DatabaseHelper", "Retrieved Song - Name: " + name + ", Path: " + path);
                } while (cursor.moveToNext());
            }

            else {
                android.util.Log.d("DatabaseHelper", "No songs found in database.");
            }



        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return songList;
    }

    public void deleteAllSongs() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(TABLE_NAME, null, null);
        } finally {
            db.close();
        }
    }
}
