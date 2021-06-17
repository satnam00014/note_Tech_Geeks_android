package com.example.note_tech_geeks_android.db;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.note_tech_geeks_android.dao.FolderDao;
import com.example.note_tech_geeks_android.dao.NoteDao;
import com.example.note_tech_geeks_android.models.Folder;
import com.example.note_tech_geeks_android.models.Note;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {
        Folder.class,
        Note.class
}, version = 1, exportSchema = false)
public abstract class NoteAppDatabase extends RoomDatabase {
    private static final String DB_NAME = "note_app_db";
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    private static NoteAppDatabase noteAppDatabase;
    private static final RoomDatabase.Callback rdc = new RoomDatabase.Callback() {
        public void onCreate(SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                FolderDao folderDao = noteAppDatabase.folderDao();
                NoteDao noteDao = noteAppDatabase.noteDao();
                Folder folder = new Folder("Business");
                folderDao.insertFolder(folder);
                folder = new Folder("School");
                folderDao.insertFolder(folder);
                Note note = new Note(1,"Don't forget send mail to Carolina.", "Mail", new Date().toString());
                noteDao.insertNote(note);
                note = new Note(2,"Math exam tomorrow.", "Exam", new Date().toString());
                noteDao.insertNote(note);
            });

        }
    };

    public static synchronized NoteAppDatabase getInstance(Context context) {
        if (noteAppDatabase == null) {
            synchronized (NoteAppDatabase.class) {
                noteAppDatabase = Room.databaseBuilder(context.getApplicationContext(), NoteAppDatabase.class, DB_NAME)
                        .addCallback(rdc)
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        return noteAppDatabase;
    }

    public abstract FolderDao folderDao();

    public abstract NoteDao noteDao();
}

