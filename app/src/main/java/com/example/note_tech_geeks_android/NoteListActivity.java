package com.example.note_tech_geeks_android;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.note_tech_geeks_android.RecyclerAdapters.NotesRecyclerAdapter;
import com.example.note_tech_geeks_android.models.FolderWithNotes;
import com.example.note_tech_geeks_android.models.Note;
import com.example.note_tech_geeks_android.viewmodel.FolderViewModel;

public class NoteListActivity extends AppCompatActivity  implements NotesRecyclerAdapter.OnNoteClickListener {

    //reference for recyclerView and adapter for that
    private RecyclerView recyclerView;
    private NotesRecyclerAdapter notesRecyclerAdapter;
    private FolderWithNotes folderWithNotes;
    private FolderViewModel folderViewModel;
    private int folderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        folderViewModel = new ViewModelProvider(this).get(FolderViewModel.class);
        folderWithNotes = (FolderWithNotes) getIntent().getSerializableExtra("data");
        folderId = folderWithNotes.folder.getId();
        findViewById(R.id.add_note_btn).setOnClickListener(v -> {
            Intent i = new Intent(this, CreateNoteActivity.class);
            i.putExtra("folderId", folderWithNotes.folder.getId());
            startActivity(i);
        });

        //setting for recycler view and adapter for that
        setRecyclerView();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        folderViewModel.getFolderWithNotesById(folderId).observe(this, data-> {
            folderWithNotes = data;
            setRecyclerView();
        });
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
                notesRecyclerAdapter.getFilter().filter(newText);
                return false;
            }
        });
        MenuItem sortASC = menu.findItem(R.id.asc_name);
        MenuItem sortDESC = menu.findItem(R.id.desc_name);
        MenuItem sortDateASC = menu.findItem(R.id.asc_date);
        MenuItem sortDateDESC = menu.findItem(R.id.desc_date);
        sortASC.setOnMenuItemClickListener(item -> {
            notesRecyclerAdapter.sortASC();
            return false;
        });
        sortDateASC.setOnMenuItemClickListener(item ->{
            notesRecyclerAdapter.sortDateASC();
            return false;
        });
        sortDateDESC.setOnMenuItemClickListener(item ->{
            notesRecyclerAdapter.sortDateDESC();
            return false;
        });

        sortDESC.setOnMenuItemClickListener(item -> {
            notesRecyclerAdapter.sortDESC();
            return false;
        });


        return true;
    }


    private void setRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view_notes);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        notesRecyclerAdapter = new NotesRecyclerAdapter(this, this);
        recyclerView.setAdapter(notesRecyclerAdapter);
        notesRecyclerAdapter.setData(folderWithNotes);
        notesRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNoteClick(Note note) {
        Intent i = new Intent(this, EditNoteActivity.class);
        i.putExtra("noteId", note.getId());
        this.startActivity(i);
    }
}