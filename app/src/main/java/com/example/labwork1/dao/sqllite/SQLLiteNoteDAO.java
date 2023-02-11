package com.example.labwork1.dao.sqllite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.provider.BaseColumns;

import androidx.annotation.RequiresApi;

import com.example.labwork1.Note;
import com.example.labwork1.dao.NoteDAO;

import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;

public class SQLLiteNoteDAO implements NoteDAO {

    private static FeedReaderDbHelper dbHelper;

    public SQLLiteNoteDAO(Context context) {
        dbHelper = new FeedReaderDbHelper(context);
    }

    @Override
    public List<Note> getNoteList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                Note.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        List<Note> notes = new ArrayList<>();
        while(cursor.moveToNext()) {
            System.out.println(cursor.getString(1));
            notes.add(new Note(
                    cursor.getString(1),
                    cursor.getString(2),
                    Timestamp.valueOf(cursor.getString(0)),
                    Timestamp.valueOf(cursor.getString(3)),
                    null,
                    cursor.getBlob(4)
            ));
        }
        cursor.close();
        return notes;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void addNote(Note note) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("title", note.getTitle());
        values.put("text", note.getText());
        values.put("timeOfCreation", note.getTimeOfCreation().toString());
        values.put("timeOfModification", note.getTimeOfModification().toString());
        values.put("passwordHash", note.getPasswordHash());

        db.insert(Note.TABLE_NAME, null, values);
    }

    @Override
    public void deleteNote(Note note) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = "timeOfCreation = ?";
        String[] selectionArgs = { note.getTimeOfCreation().toString() };
        int deletedRows = db.delete(Note.TABLE_NAME, selection, selectionArgs);
        System.out.println(deletedRows);
    }

    @Override
    public void changeNote(int changeIndex, Note note) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("title", note.getTitle());
        values.put("text", note.getText());
        values.put("timeOfModification", note.getTimeOfModification().toString());
        values.put("passwordHash", note.getPasswordHash());

        String selection = "timeOfCreation = ?";
        String[] selectionArgs = { note.getTimeOfCreation().toString() };

        int count = db.update(
                Note.TABLE_NAME,
                values,
                selection,
                selectionArgs);
        System.out.println(count);
    }
}
