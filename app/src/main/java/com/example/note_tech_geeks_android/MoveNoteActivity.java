package com.example.note_tech_geeks_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.note_tech_geeks_android.RecyclerAdapters.MoveNoteRecyclerAdapter;
import com.example.note_tech_geeks_android.RecyclerAdapters.NotesRecyclerAdapter;

import java.util.ArrayList;

public class MoveNoteActivity extends AppCompatActivity {

    //reference for recyclerView and adapter for that
    private RecyclerView recyclerView;
    private MoveNoteRecyclerAdapter moveNoteRecyclerAdapter;
    private int noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_note);
        //noteId is id of note from previous activity which we want to move.
        noteId = getIntent().getIntExtra("noteId",-1);
        //setting for recycler view and adapter for that
        setRecyclerView();
    }

    private void setRecyclerView(){
        recyclerView = findViewById(R.id.recycler_view_move_notes);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        moveNoteRecyclerAdapter = new MoveNoteRecyclerAdapter(new ArrayList<>(),this,this,noteId);
        recyclerView.setAdapter(moveNoteRecyclerAdapter);

        this.setTitle("Move note to folder");
    }


}