package com.example.note_tech_geeks_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.note_tech_geeks_android.RecyclerAdapters.MoveNoteRecyclerAdapter;
import com.example.note_tech_geeks_android.RecyclerAdapters.NotesRecyclerAdapter;
import com.example.note_tech_geeks_android.models.Folder;
import com.example.note_tech_geeks_android.models.Note;
import com.example.note_tech_geeks_android.viewmodel.FolderViewModel;

import java.util.ArrayList;

public class MoveNoteActivity extends AppCompatActivity {
    private FolderViewModel folderViewModel;
    //reference for recyclerView and adapter for that
    private RecyclerView recyclerView;
    private MoveNoteRecyclerAdapter moveNoteRecyclerAdapter;
    private Note note;
    private Folder folder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_note);
        folderViewModel = new ViewModelProvider(this).get(FolderViewModel.class);
        //noteId is id of note from previous activity which we want to move.
        note = (Note) getIntent().getSerializableExtra("note");
        folder = (Folder) getIntent().getSerializableExtra("folder");
        //setting for recycler view and adapter for that
        setRecyclerView();
    }

    private void setRecyclerView(){
        recyclerView = findViewById(R.id.recycler_view_move_notes);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        moveNoteRecyclerAdapter = new MoveNoteRecyclerAdapter(this, note, folder);
        recyclerView.setAdapter(moveNoteRecyclerAdapter);
        folderViewModel.getAllFolders().observe(this, data -> {
            moveNoteRecyclerAdapter.setData(data);
        });
        this.setTitle("Move Note");
    }


}