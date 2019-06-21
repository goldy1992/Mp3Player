package com.example.mike.mp3player.dagger.modules.service;

import android.content.Context;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.annotation.NonNull;

import com.example.mike.mp3player.service.library.MediaLibrary;
import com.example.mike.mp3player.service.library.mediaretriever.MediaRetriever;
import com.example.mike.mp3player.service.library.utils.MediaLibraryUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MediaLibraryModule {
    private final Context context;

    public MediaLibraryModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    public MediaLibrary provideMediaLibrary(MediaRetriever mediaRetriever) {
        return new MediaLibrary(mediaRetriever);
    }

    @Provides
    @Singleton
    List<MediaSessionCompat.QueueItem> provdeInitialQueueItems(@NonNull MediaLibrary mediaLibrary) {
        List<MediaBrowserCompat.MediaItem> songList = new ArrayList<>(mediaLibrary.getSongList());
        return MediaLibraryUtils.convertMediaItemsToQueueItem(songList);
    }


    @Provides
    public Context provideContext() {
        return context;
    }



}
