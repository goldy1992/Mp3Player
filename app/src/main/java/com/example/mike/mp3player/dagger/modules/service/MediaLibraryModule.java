package com.example.mike.mp3player.dagger.modules.service;

import android.content.Context;

import androidx.room.Room;

import com.example.mike.mp3player.service.library.MediaLibrary;
import com.example.mike.mp3player.service.library.db.AppDatabase;
import com.example.mike.mp3player.service.library.mediaretriever.MediaRetriever;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MediaLibraryModule {

    @Provides
    @Singleton
    public MediaLibrary provideMediaLibrary(MediaRetriever mediaRetriever, AppDatabase database) {
        return new MediaLibrary(mediaRetriever, database);
    }

    @Provides
    @Singleton
    public AppDatabase provideDatabase(Context context) {
        return Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
    }

}
