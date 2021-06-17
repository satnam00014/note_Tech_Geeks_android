package com.example.note_tech_geeks_android.dao;


import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.note_tech_geeks_android.db.NoteAppDatabase;
import com.example.note_tech_geeks_android.models.Note;

import java.util.List;

public class NoteRepository {
    private final NoteDao noteDao;
    private final LiveData<List<Note>> allNotes;

    public NoteRepository(Application application) {
        NoteAppDatabase db = NoteAppDatabase.getInstance(application);
        noteDao = db.noteDao();
        allNotes = noteDao.getAllNotes();
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

    public LiveData<List<Note>> getNotesFromFolderID(int id) {
        return noteDao.getNotesFromFolderID(id);
    }

    public void insert(Note note) {
        NoteAppDatabase.databaseWriteExecutor.execute(() -> noteDao.insertNote(note));
    }

    public void update(Note note) {
        NoteAppDatabase.databaseWriteExecutor.execute(() -> noteDao.updateNote(note));
    }

    public void delete(Note note) {
        NoteAppDatabase.databaseWriteExecutor.execute(() -> noteDao.deleteNote(note));
    }
}
