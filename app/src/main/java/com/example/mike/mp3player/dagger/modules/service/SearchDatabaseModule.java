package com.example.mike.mp3player.dagger.modules.service;

import android.content.Context;

import androidx.room.Room;

import com.example.mike.mp3player.service.library.search.SearchDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class SearchDatabaseModule {

    private static final String DATABASE_NAME = "normalised-search-db";

    @Provides
    @Singleton
    public SearchDatabase providesSearchDb(Context context) {
        Room.databaseBuilder(context, SearchDatabase.class, DATABASE_NAME).build();
        return SearchDatabase.getDatabase(context);
    }
}
