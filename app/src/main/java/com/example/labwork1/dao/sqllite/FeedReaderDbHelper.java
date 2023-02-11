package com.example.labwork1.dao.sqllite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.labwork1.Note;

public class FeedReaderDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "notedb";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE if not exists " + Note.TABLE_NAME + " (" +
                    "timeOfCreation" + " TIMESTAMP PRIMARY KEY," +
                    "title" + " TEXT NOT NULL," +
                    "text" + " TEXT," +
                    "timeOfModification" + " TIMESTAMP NOT NULL," +
                    "passwordHash" + " BLOB)";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Note.TABLE_NAME;

    public FeedReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
    }

}

