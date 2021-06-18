package com.example.note_tech_geeks_android.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.note_tech_geeks_android.models.Folder;
import com.example.note_tech_geeks_android.models.FolderWithNotes;

import java.util.List;

@Dao
public interface FolderDao {
    @Query("SELECT * FROM folders")
    LiveData<List<FolderWithNotes>> loadFolderList();

    @Query("SELECT * FROM folders WHERE id=:id")
    LiveData<FolderWithNotes> getFolderWithNotesById(int id);

    @Query("SELECT COUNT(folderId) FROM notes WHERE folderId=:id")
    int getFolderNotesCount(int id);

    @Insert
    void insertFolder(Folder folder);

    @Update
    void updateFolder(Folder folder);

    @Delete
    void deleteFolder(Folder folder);
}

