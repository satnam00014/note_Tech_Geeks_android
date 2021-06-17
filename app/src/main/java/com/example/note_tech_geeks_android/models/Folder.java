package com.example.note_tech_geeks_android.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "folders")
public class Folder implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;
    private String title;

    public Folder(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}