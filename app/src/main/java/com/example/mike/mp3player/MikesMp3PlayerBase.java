package com.example.mike.mp3player;

import android.app.Application;

import com.example.mike.mp3player.service.library.DaggerMediaLibraryComponent;
import com.example.mike.mp3player.service.library.MediaLibraryComponent;
import com.example.mike.mp3player.service.library.MediaLibraryModule;
import com.example.mike.mp3player.service.library.mediaretriever.MediaRetriever;
import com.example.mike.mp3player.service.library.mediaretriever.MediaRetrieverModule;

public class MikesMp3PlayerBase extends Application {

    private MediaLibraryComponent mediaLibraryComponent;

    public MediaLibraryComponent getMediaLibraryComponent() {
        return mediaLibraryComponent;
    }

    protected void setupMediaLibrary(MediaRetriever mediaRetriever) {
        MediaLibraryModule mediaLibraryModule = new MediaLibraryModule(getApplicationContext());
        MediaRetrieverModule mediaRetrieverModule = new MediaRetrieverModule(mediaRetriever);
        this.mediaLibraryComponent = DaggerMediaLibraryComponent.builder()
                .mediaRetrieverModule(mediaRetrieverModule)
                .mediaLibraryModule(mediaLibraryModule).build();

    }
}
