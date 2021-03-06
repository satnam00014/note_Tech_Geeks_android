package com.example.note_tech_geeks_android.RecyclerAdapters;
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

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.note_tech_geeks_android.MainActivity;
import com.example.note_tech_geeks_android.NoteListActivity;
import com.example.note_tech_geeks_android.R;
import com.example.note_tech_geeks_android.models.Folder;
import com.example.note_tech_geeks_android.models.FolderWithNotes;
import com.example.note_tech_geeks_android.models.Note;
import com.example.note_tech_geeks_android.viewmodel.FolderViewModel;
import com.example.note_tech_geeks_android.viewmodel.NoteViewModel;

import java.util.ArrayList;
import java.util.List;

public class FolderRecyclerAdapter extends RecyclerView.Adapter<FolderRecyclerAdapter.ViewHolder> implements Filterable {
    FolderViewModel folderViewModel;
    List<FolderWithNotes> folders;
    List<FolderWithNotes> totalFolders;
    List<Integer> folderNoteCounts;
    NoteViewModel noteViewModel;

    Context context;

    public FolderRecyclerAdapter(Context context) {
        this.folderViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(FolderViewModel.class);
        this.folders = new ArrayList<>();
        this.context = context;
        this.folderNoteCounts = new ArrayList<>();
        this.noteViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(NoteViewModel.class);
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private final Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<FolderWithNotes> filteredNotes = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredNotes.addAll(totalFolders);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (FolderWithNotes folderWithNotes : totalFolders) {
                    if (folderWithNotes.folder.getTitle().toLowerCase().contains(filterPattern)) {
                        filteredNotes.add(folderWithNotes);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredNotes;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            folders.clear();
            folders.addAll((List<FolderWithNotes>) results.values);
            notifyDataSetChanged();
        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //this is where where view in inflated and will return view holder with view(that means card)
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.folder_card, parent, false);
        return new ViewHolder((CardView) view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //instance of card on which we are performing operations.
        CardView localCardView = holder.currentCardView;
        TextView folderName = localCardView.findViewById(R.id.folder_name_card);
        TextView numberOfNotes = localCardView.findViewById(R.id.note_numbers_card);
        ImageView folderImageView = localCardView.findViewById(R.id.folder_image_card);
        Glide.with(context).load(R.drawable.folder_icon)
                .apply(RequestOptions.circleCropTransform()).thumbnail(0.3f).into(folderImageView);
        folderName.setText(folders.get(position).folder.getTitle());
        numberOfNotes.setText(folderNoteCounts.get(position).toString());

        // Delete Part 1
        localCardView.findViewById(R.id.delete_bt_folder_card).setOnClickListener(v -> {
            this.deleteFolderDialog(folders.get(position));
        });

        localCardView.setOnClickListener(v -> {
            Intent i = new Intent(context, NoteListActivity.class);
            i.putExtra("data", folders.get(position));
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return folders.size();
    }
            // Delete Part 2
            private void deleteFolderDialog(FolderWithNotes folderWithNotes) {
                // create a dialog box from layout using layout inflater.
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                View view = layoutInflater.inflate(R.layout.dialog_delete_folder, null);
                builder.setView(view);
                final AlertDialog alertDialog = builder.create();
                //following is to disable dismiss if user touches outside the dialog box area
                alertDialog.setCanceledOnTouchOutside(false);
                //following is to add transparent background for roundedges other wise white corner will be shown
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
                view.findViewById(R.id.cancel_folder_delete_dialog_bt).setOnClickListener(v -> {
                    alertDialog.dismiss();
                });
                view.findViewById(R.id.delete_folder_dialog_bt).setOnClickListener(v -> {
                    folderViewModel.delete(folderWithNotes.folder);
                    for (Note note: folderWithNotes.notes){
                        noteViewModel.delete(note);
                    }
                    alertDialog.dismiss();
                });
            }
    public void setData(List<FolderWithNotes> data) {
        if (data != null) {
            folders.clear();
            folders.addAll(data);
            this.folderNoteCounts.clear();
            getFolderNotesCount(data);
            notifyDataSetChanged();
        } else {
            folders = data;
        }
        this.totalFolders = new ArrayList<>(folders);
        ((MainActivity)context).setTitle(folders.size() + " - Folders");

    }

    // this is the view holder which holds the view
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final CardView currentCardView;

        public ViewHolder(@NonNull CardView cardView) {
            super(cardView);
            this.currentCardView = cardView;
        }
    }
    public void getFolderNotesCount(List<FolderWithNotes> data){
        for (FolderWithNotes folderWithNotes: data){
            this.folderNoteCounts.add(folderViewModel.getFolderNotesCount(folderWithNotes.folder.getId()));
        }
    }
}
