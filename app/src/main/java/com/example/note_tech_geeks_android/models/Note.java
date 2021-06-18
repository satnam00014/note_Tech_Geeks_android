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
    private String imageURL;
    private String voiceURL;
    private String location;
    private double latitude;
    private double longitude;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    private byte[] imageData;

    public Note(int folderId, String noteContent, String noteTitle, String noteDate) {
        this.folderId = folderId;
        this.noteContent = noteContent;
        this.noteTitle = noteTitle;
        this.noteDate = noteDate;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
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
    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getVoiceURL() {
        return voiceURL;
    }

    public void setVoiceURL(String voiceURL) {
        this.voiceURL = voiceURL;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
