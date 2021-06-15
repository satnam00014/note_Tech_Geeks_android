package com.example.note_tech_geeks_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

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
        setRecyclerView();
    }

    private void setRecyclerView(){
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        folderRecyclerAdapter = new FolderRecyclerAdapter(new ArrayList<>(),this,this);
        recyclerView.setAdapter(folderRecyclerAdapter);

        this.setTitle("0 - folders");
    }
}