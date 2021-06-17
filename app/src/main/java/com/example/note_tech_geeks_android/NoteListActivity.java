package com.example.note_tech_geeks_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.note_tech_geeks_android.RecyclerAdapters.FolderRecyclerAdapter;
import com.example.note_tech_geeks_android.RecyclerAdapters.NotesRecyclerAdapter;

import java.util.ArrayList;

public class NoteListActivity extends AppCompatActivity {

    //reference for recyclerView and adapter for that
    private RecyclerView recyclerView;
    private NotesRecyclerAdapter notesRecyclerAdapter;
    private int folderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        //noteId is id of note from previous activity which we want to move.
        folderId = getIntent().getIntExtra("folderId",-1);

        findViewById(R.id.add_note_btn).setOnClickListener(v -> {startActivity(new Intent(this,CreateNoteActivity.class));});

        //setting for recycler view and adapter for that
        setRecyclerView();
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    // logic to refresh Recycler view data when user return to this activity
    private void loadData(){

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // below line is to get our inflater
        MenuInflater inflater = getMenuInflater();

        // inside inflater we are inflating our menu file.
        inflater.inflate(R.menu.notes_list_menu, menu);

        // below line is to get our menu item.
        MenuItem searchItem = menu.findItem(R.id.actionSearch_notes);

        // getting search view of our item.
        SearchView searchView = (SearchView) searchItem.getActionView();

        // below line is to call set on query text listener method.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //create filter class before apply below line otherwise app will crash
                //notesRecyclerAdapter.getFilter().filter(newText);
                return true;
            }
        });
        return true;
    }

    private void setRecyclerView(){
        recyclerView = findViewById(R.id.recycler_view_notes);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //change constructor of adapter accordingly ...
        //This constructor is just a sample ...
        notesRecyclerAdapter = new NotesRecyclerAdapter(new ArrayList<>(),this,this,folderId);
        recyclerView.setAdapter(notesRecyclerAdapter);

        this.setTitle("0 - Notes");
    }
}