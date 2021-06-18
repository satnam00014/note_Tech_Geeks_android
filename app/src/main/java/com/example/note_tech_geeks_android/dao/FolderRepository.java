package com.example.note_tech_geeks_android.dao;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.note_tech_geeks_android.db.NoteAppDatabase;
import com.example.note_tech_geeks_android.models.Folder;
import com.example.note_tech_geeks_android.models.FolderWithNotes;

import java.util.List;

public class FolderRepository {
    private final FolderDao mFolderDao;
    private final LiveData<List<FolderWithNotes>> allFolderWithNotes;

    public FolderRepository(Application application) {
        NoteAppDatabase db = NoteAppDatabase.getInstance(application);
        mFolderDao = db.folderDao();
        allFolderWithNotes = mFolderDao.loadFolderList();
    }

    public int getFolderNotesCount(int id) {
        return mFolderDao.getFolderNotesCount(id);
    }

    public LiveData<FolderWithNotes> getFolderWithNotesById(int id) {return mFolderDao.getFolderWithNotesById(id);}

    public LiveData<List<FolderWithNotes>> getAllFolderWithNotes() {
        return allFolderWithNotes;
    }

    public void insert(Folder folder) {
        NoteAppDatabase.databaseWriteExecutor.execute(() -> mFolderDao.insertFolder(folder));
    }

    public void update(Folder folder) {
        NoteAppDatabase.databaseWriteExecutor.execute(() -> mFolderDao.updateFolder(folder));
    }

    public void delete(Folder folder) {
        NoteAppDatabase.databaseWriteExecutor.execute(() -> mFolderDao.deleteFolder(folder));
    }
}
