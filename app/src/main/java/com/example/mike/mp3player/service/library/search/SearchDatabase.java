package com.example.mike.mp3player.service.library.search;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Song.class, Folder.class}, version = 2)
public abstract class SearchDatabase extends RoomDatabase {
    public abstract SongDao songDao();
    public abstract FolderDao folderDao();

    public static final String DATABASE_NAME = "normalised_search_db";

    public static volatile SearchDatabase INSTANCE;

    public static SearchDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (SearchDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            SearchDatabase.class, DATABASE_NAME)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
