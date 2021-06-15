package com.example.note_tech_geeks_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.note_tech_geeks_android.RecyclerAdapters.FolderRecyclerAdapter;
import com.example.note_tech_geeks_android.RecyclerAdapters.NotesRecyclerAdapter;

import java.util.ArrayList;

public class NoteListActivity extends AppCompatActivity {

    //reference for recyclerView and adapter for that
    private RecyclerView recyclerView;
    private NotesRecyclerAdapter notesRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        //setting for recycler view and adapter for that
        setRecyclerView();
    }

    private void setRecyclerView(){
        recyclerView = findViewById(R.id.recycler_view_notes);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        notesRecyclerAdapter = new NotesRecyclerAdapter(new ArrayList<>(),this,this);
        recyclerView.setAdapter(notesRecyclerAdapter);

        this.setTitle("0 - Notes");
    }
}