package com.example.note_tech_geeks_android.models;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.io.Serializable;
import java.util.List;

public class FolderWithNotes implements Serializable {
    @Embedded
    public Folder folder;
    @Relation(parentColumn = "id", entityColumn = "folderId", entity = Note.class)
    public List<Note> notes;
}