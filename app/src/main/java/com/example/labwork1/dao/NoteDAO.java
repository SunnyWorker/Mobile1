package com.example.labwork1.dao;

import com.example.labwork1.Note;

import java.util.List;

public interface NoteDAO {
    List<Note> getNoteList();
    void addNote(Note note);
    void deleteNote(Note note);
    void changeNote(int changeIndex, Note note);
}
