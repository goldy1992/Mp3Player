package com.example.mike.mp3player.service.library.search;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Song.class, Folder.class}, version = 2)
public abstract class SearchDatabase extends RoomDatabase {
    public abstract SongDao songDao();
    public abstract FolderDao folderDao();

    private static final String DATABASE_NAME = "normalised_search_db";

    private static SearchDatabase instance;

    public synchronized static SearchDatabase getDatabase(final Context context) {
        if (instance == null) {
                     instance = Room.databaseBuilder(context.getApplicationContext(),
                            SearchDatabase.class, DATABASE_NAME)
                            .build();
                }
        return instance;
    }
}
