package com.example.mike.mp3player.service.library.db;

import android.content.Context;

import androidx.room.Room;

public class DatabaseCreator {

    private Context context;
    private AppDatabase appDatabase;

    public DatabaseCreator(Context context) {
        this.context = context;
        this.appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
    }
}
