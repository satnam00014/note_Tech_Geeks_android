package com.example.note_tech_geeks_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class EditNoteActivity extends AppCompatActivity {

    private int noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        //to get id of note from previous activity and edit note using that.
        noteId = getIntent().getIntExtra("noteId",-1);
    }
}