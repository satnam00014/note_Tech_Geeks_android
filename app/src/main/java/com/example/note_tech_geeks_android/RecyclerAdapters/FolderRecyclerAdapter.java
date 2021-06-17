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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.note_tech_geeks_android.NoteListActivity;
import com.example.note_tech_geeks_android.R;

import java.util.List;

public class FolderRecyclerAdapter extends RecyclerView.Adapter<FolderRecyclerAdapter.ViewHolder> implements Filterable {

    List<String > folders;
    List<String> totalFolders;
    Context context;
    Activity folderActivity;

    public FolderRecyclerAdapter(List<String> folderList, Context context,Activity folderActivity) {
        this.folders = folderList;
        this.totalFolders = folders;
        this.context = context;
        //following is passed to access titlebar or similar properties from adapter.
        this.folderActivity = folderActivity;
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //this is where where view in inflated and will return view holder with view(that means card)
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.folder_card, parent, false);
        folderActivity.setTitle(" 20 - Folders");
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
        folderName.setText("Sample is to great to handle because this is too big");
        numberOfNotes.setText("20 - notes");
        localCardView.findViewById(R.id.delete_bt_folder_card).setOnClickListener(v -> {this.deleteFolderDialog(25);});
        localCardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, NoteListActivity.class);
            //give folder id in place of 25
            intent.putExtra("folderId",25);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return 20;
    }

    // this is the view holder which holds the view
    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView currentCardView;

        public ViewHolder(@NonNull CardView cardView) {
            super(cardView);
            this.currentCardView = cardView;
        }
    }

    private void deleteFolderDialog(int folderId){
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
        view.findViewById(R.id.cancel_folder_delete_dialog_bt).setOnClickListener(v -> {alertDialog.dismiss();});
        view.findViewById(R.id.delete_folder_dialog_bt).setOnClickListener(v -> {this.deleteFolder(alertDialog,folderId);});
    }

    //write logic to delete folder in this function.
    private void deleteFolder(AlertDialog alertDialog, int folderId){
        Toast.makeText(context,"delete button",Toast.LENGTH_SHORT).show();
        //write you logic here
        alertDialog.dismiss();
    }
}
