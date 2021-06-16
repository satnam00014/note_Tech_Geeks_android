package com.example.note_tech_geeks_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.note_tech_geeks_android.RecyclerAdapters.FolderRecyclerAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //reference for recyclerView and adapter for that
    private RecyclerView recyclerView;
    private FolderRecyclerAdapter folderRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setting for recycler view and adapter for that
        findViewById(R.id.add_folder_btn).setOnClickListener(v -> { this.createFolderDialog(); });
        setRecyclerView();
    }

    private void createFolderDialog() {
        // create a dialog box from layout using layout inflater.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.dialog_add_folder, null);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        //following is to disable dismiss if user touches outside the dialog box area
        alertDialog.setCanceledOnTouchOutside(false);
        //following is to add transparent background for roundedges other wise white corner will be shown
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        view.findViewById(R.id.cancel_folder_dialog_bt).setOnClickListener(v -> {alertDialog.dismiss();});
        view.findViewById(R.id.create_folder_dialog_bt).setOnClickListener(v -> { this.addFolder(); });
    }

    //add folder logic in below function
    private void addFolder(){
        Toast.makeText(this,"Create button",Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // below line is to get our inflater
        MenuInflater inflater = getMenuInflater();

        // inside inflater we are inflating our menu file.
        inflater.inflate(R.menu.folder_search_menu, menu);

        // below line is to get our menu item.
        MenuItem searchItem = menu.findItem(R.id.actionSearch_folder);

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
                //folderRecyclerAdapter.getFilter().filter(newText);
                return true;
            }
        });
        return true;
    }

    private void setRecyclerView(){
        recyclerView = findViewById(R.id.recycler_view_folder);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        folderRecyclerAdapter = new FolderRecyclerAdapter(new ArrayList<>(),this,this);
        recyclerView.setAdapter(folderRecyclerAdapter);

        this.setTitle("0 - folders");
    }
}