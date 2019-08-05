package com.example.mike.mp3player.service.library.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(version = 1, entities = {Root.class, Song.class, Folder.class})
@TypeConverters(MediaItemConverter.class)
public abstract class AppDatabase extends RoomDatabase {
    public abstract SongDao songDao();
    public abstract FolderDao folderDao();
    public abstract RootDao rootDao();
}
