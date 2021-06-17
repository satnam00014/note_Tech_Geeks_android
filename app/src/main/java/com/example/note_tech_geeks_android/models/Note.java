package com.example.note_tech_geeks_android.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "notes")
public class Note implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;
    private int folderId;
    private String noteTitle;
    private String noteContent;
    private String noteDate;

    public Note(int folderId, String noteContent, String noteTitle, String noteDate) {
        this.folderId = folderId;
        this.noteContent = noteContent;
        this.noteTitle = noteTitle;
        this.noteDate = noteDate;
    }

    public int getId() {
        return id;
    }

    public void setFolderId(int id) {this.folderId = id;}

    public int getFolderId() {
        return folderId;
    }

    public String getNoteTitle() {
        return this.noteTitle;
    }

    public void setNoteTitle(String content) {
        this.noteTitle = content;
    }

    public String getNoteContent() {
        return this.noteContent;
    }

    public void setNoteContent(String content) {
        this.noteContent = content;
    }

    public String getNoteDate() {
        return this.noteDate;
    }

    public void setNoteDate(String date) {
        this.noteDate = date;
    }
}
