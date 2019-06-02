package com.example.mike.mp3player;

import android.app.Application;

import com.example.mike.mp3player.service.library.ContentResolverMediaRetrieverModule;
import com.example.mike.mp3player.service.library.MediaLibrary;
import com.example.mike.mp3player.service.library.MediaLibraryComponent;
import com.example.mike.mp3player.service.library.MediaLibraryModule;
import com.example.mike.mp3player.service.library.mediaretriever.ContentResolverMediaRetriever;
import com.example.mike.mp3player.service.library.mediaretriever.MediaRetrieverModule;

public class MikesMp3Player extends Application {

    private MediaLibraryComponent mediaLibraryComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        MediaLibraryModule mediaLibraryModule = new MediaLibraryModule(getApplicationContext());
        MediaRetrieverModule mediaRetrieverModule = new ContentResolverMediaRetrieverModule(getApplicationContext());

            }

    public MediaLibraryComponent getMediaLibraryComponent() {
        return mediaLibraryComponent;
    }
}
