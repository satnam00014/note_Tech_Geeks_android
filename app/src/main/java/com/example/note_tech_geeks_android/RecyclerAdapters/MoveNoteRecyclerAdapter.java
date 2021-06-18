package com.example.note_tech_geeks_android.RecyclerAdapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.note_tech_geeks_android.MainActivity;
import com.example.note_tech_geeks_android.MoveNoteActivity;
import com.example.note_tech_geeks_android.NoteListActivity;
import com.example.note_tech_geeks_android.R;
import com.example.note_tech_geeks_android.models.Folder;
import com.example.note_tech_geeks_android.models.FolderWithNotes;
import com.example.note_tech_geeks_android.models.Note;
import com.example.note_tech_geeks_android.viewmodel.NoteViewModel;

import java.util.ArrayList;
import java.util.List;

public class MoveNoteRecyclerAdapter extends RecyclerView.Adapter<MoveNoteRecyclerAdapter.ViewHolder> implements Filterable {

    List<String > folders;
    Context context;
    //activity reference is need to close current activity after moving note to a folder.
    Activity parentActivity;
    int noteId;

    public MoveNoteRecyclerAdapter(List<String> folderList, Context context,Activity parentActivity,int noteId) {
        this.folders = folderList;
        this.context = context;
        this.parentActivity = parentActivity;
        this.noteId = noteId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //this is where where view in inflated and will return view holder with view(that means card)
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.move_note_card, parent, false);
        return new ViewHolder((CardView) view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //instance of card on which we are performing operations.
        CardView localCardView = holder.currentCardView;
        TextView folderName = localCardView.findViewById(R.id.folder_name_move_card);
        TextView numberOfNotes = localCardView.findViewById(R.id.note_numbers_move_card);
        ImageView folderImageView = localCardView.findViewById(R.id.folder_image_move_card);
        Glide.with(context).load(R.drawable.folder_icon)
                .apply(RequestOptions.circleCropTransform()).thumbnail(0.3f).into(folderImageView);
        folderName.setText("Folder");
        numberOfNotes.setText("Total notes - 20");
        localCardView.setOnClickListener(v -> {
            Toast.makeText(context,"Folder "+(position+1)+" is clicked",Toast.LENGTH_SHORT).show();
            //Logic to move note.
            //following line is to close activity after note was moved.
            parentActivity.finish();
        });
    }

    @Override
    public int getItemCount() {
        return 15;
    }

    // this is the view holder which holds the view
    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView currentCardView;

        public ViewHolder(@NonNull CardView cardView) {
            super(cardView);
            this.currentCardView = cardView;
        }
    }

    @Override
    public Filter getFilter() {
        return null;
    }


}
