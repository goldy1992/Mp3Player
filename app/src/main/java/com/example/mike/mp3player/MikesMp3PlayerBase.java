package com.example.mike.mp3player;

import android.app.Application;

import com.example.mike.mp3player.dagger.components.DaggerServiceComponent;
import com.example.mike.mp3player.dagger.components.MainActivityComponent;
import com.example.mike.mp3player.dagger.components.ServiceComponent;
import com.example.mike.mp3player.dagger.modules.MediaBrowserAdapterModule;
import com.example.mike.mp3player.dagger.modules.service.MediaLibraryModule;
import com.example.mike.mp3player.dagger.modules.service.MediaRetrieverModule;
import com.example.mike.mp3player.service.library.mediaretriever.MediaRetriever;

public class MikesMp3PlayerBase extends Application {

    private ServiceComponent serviceComponent;
    private MainActivityComponent mainActivityComponent;

    public ServiceComponent getServiceComponent() {
        return serviceComponent;
    }

    protected void setupMediaLibrary(MediaRetriever mediaRetriever) {
        MediaLibraryModule mediaLibraryModule = new MediaLibraryModule();
        MediaRetrieverModule mediaRetrieverModule = new MediaRetrieverModule(mediaRetriever);
        this.serviceComponent = DaggerServiceComponent.builder()
                .mediaRetrieverModule(mediaRetrieverModule)
                .mediaLibraryModule(mediaLibraryModule).build();

    }

    protected void setupMainActivity() {
        MediaBrowserAdapterModule mediaBrowserAdapterModule = new MediaBrowserAdapterModule();
        //this.mainActivityComponent = DaggerMainActivityComponent.builder()
    }
}
