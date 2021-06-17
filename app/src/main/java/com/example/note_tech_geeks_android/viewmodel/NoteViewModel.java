package com.example.note_tech_geeks_android.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.note_tech_geeks_android.dao.NoteRepository;
import com.example.note_tech_geeks_android.models.Note;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {
    private final LiveData<List<Note>> allNotes;
    private final NoteRepository noteRepository;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        noteRepository = new NoteRepository(application);
        allNotes = noteRepository.getAllNotes();
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

    public LiveData<List<Note>> getNotesFromFolderID(int id) {
        return noteRepository.getNotesFromFolderID(id);
    }

    public void insert(Note note) {
        noteRepository.insert(note);
    }

    public void update(Note note) {
        noteRepository.update(note);
    }

    public void delete(Note note) {
        noteRepository.delete(note);
    }
}

