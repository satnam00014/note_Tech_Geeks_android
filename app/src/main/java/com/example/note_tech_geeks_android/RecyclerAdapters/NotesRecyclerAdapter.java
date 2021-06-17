package com.example.note_tech_geeks_android.RecyclerAdapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.example.note_tech_geeks_android.MoveNoteActivity;
import com.example.note_tech_geeks_android.NoteListActivity;
import com.example.note_tech_geeks_android.R;
import com.example.note_tech_geeks_android.models.Folder;
import com.example.note_tech_geeks_android.models.FolderWithNotes;
import com.example.note_tech_geeks_android.models.Note;
import com.example.note_tech_geeks_android.viewmodel.NoteViewModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NotesRecyclerAdapter extends RecyclerView.Adapter<NotesRecyclerAdapter.ViewHolder> implements Filterable {
    NoteViewModel noteViewModel;
    List<Note> notes;
    List<Note> totalNotes;
    Context context;
    Folder folder;
    public NotesRecyclerAdapter(Context context) {
        this.notes = new ArrayList<>();
        this.context = context;
        noteViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(NoteViewModel.class);
    }
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //this is where where view in inflated and will return view holder with view(that means card)
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_card, parent, false);
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
        noteName.setText(notes.get(position).getNoteTitle());
        noteDate.setText("Date: " + notes.get(position).getNoteDate());
        localCardView.findViewById(R.id.edit_bt_note_card).setOnClickListener(v -> {
            Intent intent = new Intent(context, MoveNoteActivity.class);
            intent.putExtra("note",notes.get(position));
            intent.putExtra("folder", folder);
            context.startActivity(intent);
        });
        // Delete Part 3

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }



    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Note> filteredNotes = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredNotes.addAll(totalNotes);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Note note : totalNotes) {
                    if (note.getNoteTitle().toLowerCase().contains(filterPattern)) {
                        filteredNotes.add(note);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredNotes;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            notes.clear();
            notes.addAll((List<Note>) results.values);
            notifyDataSetChanged();
        }
    };

    // this is the view holder which holds the view
    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView currentCardView;

        public ViewHolder(@NonNull CardView cardView) {
            super(cardView);
            this.currentCardView = cardView;
        }
    }
        // Delete part 4


    public void setData(FolderWithNotes data){
        if (data != null) {
            notes.clear();
            notes.addAll(data.notes);
            notifyDataSetChanged();
            folder = data.folder;
        } else {
            notes = data.notes;
            folder = data.folder;
        }
        this.totalNotes = new ArrayList<>(notes);
        ((NoteListActivity)context).setTitle(notes.size() + " - Notes");
    }
    public void sortASC(){
        Collections.sort(notes, (o1, o2) -> o1.getNoteTitle().compareTo(o2.getNoteTitle()));
        notifyDataSetChanged();
    }
    public void sortDESC(){
        sortASC();
        Collections.reverse(notes);
        notifyDataSetChanged();
    }
    public void sortDateASC(){
        Collections.sort(notes, new Comparator<Note>() {
            DateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            @Override
            public int compare(Note o1, Note o2) {
                try {
                    return f.parse(o1.getNoteDate()).compareTo(f.parse(o2.getNoteDate()));
                } catch (ParseException e) {
                    e.printStackTrace();
                    throw new IllegalArgumentException(e);
                }
            }
        });
        notifyDataSetChanged();
    }
    public void sortDateDESC(){
        sortDateASC();
        Collections.reverse(notes);
        notifyDataSetChanged();
    }
}
