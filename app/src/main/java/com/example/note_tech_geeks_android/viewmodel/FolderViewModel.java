package com.example.note_tech_geeks_android.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.note_tech_geeks_android.dao.FolderRepository;
import com.example.note_tech_geeks_android.models.Folder;
import com.example.note_tech_geeks_android.models.FolderWithNotes;

import java.util.List;

public class FolderViewModel extends AndroidViewModel {
    private final LiveData<List<FolderWithNotes>> allFolders;
    private final FolderRepository folderRepository;

    public FolderViewModel(Application application) {
        super(application);
        folderRepository = new FolderRepository(application);
        allFolders = folderRepository.getAllFolderWithNotes();
    }

    public LiveData<List<FolderWithNotes>> getAllFolders() {
        return allFolders;
    }

    public int getFolderNotesCount(int id) {
        return folderRepository.getFolderNotesCount(id);
    }

    public LiveData<FolderWithNotes> getFolderWithNotesById(int id) {return folderRepository.getFolderWithNotesById(id);}



    public void insert(Folder folder) {
        folderRepository.insert(folder);
    }

    public void update(Folder folder) {
        folderRepository.update(folder);
    }

    public void delete(Folder folder) {
        folderRepository.delete(folder);
    }
}
