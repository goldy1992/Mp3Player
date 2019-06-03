package com.example.mike.mp3player;

import android.app.Application;

import com.example.mike.mp3player.service.library.DaggerMediaLibraryComponent;
import com.example.mike.mp3player.service.library.MediaLibraryComponent;
import com.example.mike.mp3player.service.library.MediaLibraryModule;
import com.example.mike.mp3player.service.library.mediaretriever.ContentResolverMediaRetriever;
import com.example.mike.mp3player.service.library.mediaretriever.MediaRetriever;
import com.example.mike.mp3player.service.library.mediaretriever.MediaRetrieverModule;

public class MikesMp3Player extends Application {

    private MediaLibraryComponent mediaLibraryComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        MediaLibraryModule mediaLibraryModule = new MediaLibraryModule(getApplicationContext());
        MediaRetriever contentResolverMediaRetriever = new ContentResolverMediaRetriever(getApplicationContext());
        MediaRetrieverModule mediaRetrieverModule = new MediaRetrieverModule(contentResolverMediaRetriever);
        this.mediaLibraryComponent = DaggerMediaLibraryComponent.builder()
                .mediaRetrieverModule(mediaRetrieverModule)
                .mediaLibraryModule(mediaLibraryModule).build();
            }

    public MediaLibraryComponent getMediaLibraryComponent() {
        return mediaLibraryComponent;
    }
}
