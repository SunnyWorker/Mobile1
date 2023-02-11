package com.example.labwork1.dao.file;

import android.content.Context;

import com.example.labwork1.Note;
import com.example.labwork1.dao.NoteDAO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class FileNoteDAO implements NoteDAO {

    private final File rootDirectory;
    private static List<Note> noteList;

    public FileNoteDAO(Context context) {
        rootDirectory = new File(context.getExternalFilesDir(null).getPath());
    }

    @Override
    public List<Note> getNoteList() {
        File file = new File(rootDirectory.getAbsolutePath()+"/saved_data.ini");
        try (FileInputStream in = new FileInputStream(file); ObjectInputStream objectStream = new ObjectInputStream(in)) {
            noteList = (List<Note>)objectStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            noteList = new ArrayList<>();
        }
        return noteList;
    }

    @Override
    public void addNote(Note note) {
        noteList.add(note);
        saveNoteList();
    }

    @Override
    public void deleteNote(Note note) {
        noteList.remove(note);
        saveNoteList();
    }

    @Override
    public void changeNote(int changeIndex, Note note) {
        noteList.set(changeIndex,note);
        saveNoteList();
    }

    private void saveNoteList() {
        File file = new File(rootDirectory.getAbsolutePath()+"/saved_data.ini");
        try(FileOutputStream stream = new FileOutputStream(file); ObjectOutputStream objectStream = new ObjectOutputStream(stream)) {
            objectStream.writeObject(noteList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
