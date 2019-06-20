package com.example.mike.mp3player;

import android.app.Application;

import com.example.mike.mp3player.dagger.components.MainActivityComponent;
import com.example.mike.mp3player.dagger.modules.MediaBrowserAdapterModule;
import com.example.mike.mp3player.dagger.components.DaggerMediaLibraryComponent;
import com.example.mike.mp3player.dagger.components.MediaLibraryComponent;
import com.example.mike.mp3player.dagger.modules.MediaLibraryModule;
import com.example.mike.mp3player.service.library.mediaretriever.MediaRetriever;
import com.example.mike.mp3player.dagger.modules.MediaRetrieverModule;

public class MikesMp3PlayerBase extends Application {

    private MediaLibraryComponent mediaLibraryComponent;
    private MainActivityComponent mainActivityComponent;

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

    protected void setupMainActivity() {
        MediaBrowserAdapterModule mediaBrowserAdapterModule = new MediaBrowserAdapterModule();
        //this.mainActivityComponent = DaggerMainActivityComponent.builder()
    }
}
