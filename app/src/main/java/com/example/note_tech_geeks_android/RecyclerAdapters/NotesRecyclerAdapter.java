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
import android.widget.Button;
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
import com.example.note_tech_geeks_android.EditNoteActivity;
import com.example.note_tech_geeks_android.MoveNoteActivity;
import com.example.note_tech_geeks_android.NoteListActivity;
import com.example.note_tech_geeks_android.R;
import com.example.note_tech_geeks_android.models.Folder;
import com.example.note_tech_geeks_android.models.FolderWithNotes;
import com.example.note_tech_geeks_android.models.Note;
import com.example.note_tech_geeks_android.viewmodel.NoteViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
    private final OnNoteClickListener onNoteClickListener;

    public NotesRecyclerAdapter(Context context, OnNoteClickListener onNoteClickListener) {
        this.notes = new ArrayList<>();
        this.context = context;
        this.onNoteClickListener = onNoteClickListener;

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
        Glide.with(context).load(R.drawable.note_icon)
                .apply(RequestOptions.circleCropTransform()).thumbnail(0.3f).into(holder.noteImageView);
        holder.noteName.setText(notes.get(position).getNoteTitle());
        holder.noteDate.setText("Date: " + notes.get(position).getNoteDate());
        holder.editButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, MoveNoteActivity.class);
            intent.putExtra("note",notes.get(position));
            intent.putExtra("folder", folder);
            context.startActivity(intent);
        });

//        holder.deleteButton.setOnClickListener(v -> {this.deleteNoteDialog(notes.get(position));});
        // Delete Part 3
        holder.deleteButton.setOnClickListener(v -> {this.deleteNoteDialog(notes.get(position));});
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
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView noteName, noteDate;
        private final ImageView noteImageView;
        private final FloatingActionButton editButton, deleteButton;

        public ViewHolder(@NonNull CardView cardView) {
            super(cardView);
            noteName = cardView.findViewById(R.id.note_name_card);
            noteDate = cardView.findViewById(R.id.note_date_card);
            noteImageView = cardView.findViewById(R.id.note_image_card);
            editButton = cardView.findViewById(R.id.edit_bt_note_card);
            deleteButton = cardView.findViewById(R.id.delete_bt_note_card);
            cardView.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            onNoteClickListener.onNoteClick(notes.get(getAdapterPosition()));

        }
    }
    public interface OnNoteClickListener {
        void onNoteClick(Note note);
    }
        // Delete part 4
        private void deleteNoteDialog(Note note){
            // create a dialog box from layout using layout inflater.
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View view = layoutInflater.inflate(R.layout.dialog_delete_note, null);
            builder.setView(view);
            final AlertDialog alertDialog = builder.create();
            //following is to disable dismiss if user touches outside the dialog box area
            alertDialog.setCanceledOnTouchOutside(false);
            //following is to add transparent background for roundedges other wise white corner will be shown
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.show();
            view.findViewById(R.id.cancel_note_delete_dialog_bt).setOnClickListener(v -> {alertDialog.dismiss();});
            view.findViewById(R.id.delete_note_dialog_bt).setOnClickListener(v -> {
                noteViewModel.delete(note);
                notes.remove(note);
                notifyDataSetChanged();
                alertDialog.dismiss();
            });
        }


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
