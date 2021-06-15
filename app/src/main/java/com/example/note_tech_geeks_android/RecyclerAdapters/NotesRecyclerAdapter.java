package com.example.note_tech_geeks_android.RecyclerAdapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.note_tech_geeks_android.R;

import java.util.List;

public class NotesRecyclerAdapter extends RecyclerView.Adapter<NotesRecyclerAdapter.ViewHolder> implements Filterable {

    List<String > notes;
    List<String> totalNotes;
    Context context;
    Activity noteListActivity;

    public NotesRecyclerAdapter(List<String> notesList, Context context,Activity noteListActivity) {
        this.notes = notesList;
        this.totalNotes = notesList;
        this.context = context;
        //following is passed to access titlebar or similar properties from adapter.
        this.noteListActivity = noteListActivity;
    }
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //this is where where view in inflated and will return view holder with view(that means card)
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_card, parent, false);
        noteListActivity.setTitle(" 20 - Notes");
        return new ViewHolder((CardView) view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //instance of card on which we are performing operations.
        CardView localCardView = holder.currentCardView;
        TextView noteName = localCardView.findViewById(R.id.note_name_card);
        TextView noteDate = localCardView.findViewById(R.id.note_date_card);
        ImageView noteImageView = localCardView.findViewById(R.id.note_image_card);
        Glide.with(context).load(R.drawable.note_icon)
                .apply(RequestOptions.circleCropTransform()).thumbnail(0.3f).into(noteImageView);
        noteName.setText("Sample Title");
        noteDate.setText("Date: dd/mm/yyyy");
    }

    @Override
    public int getItemCount() {
        return 20;
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    // this is the view holder which holds the view
    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView currentCardView;

        public ViewHolder(@NonNull CardView cardView) {
            super(cardView);
            this.currentCardView = cardView;
        }
    }
}
