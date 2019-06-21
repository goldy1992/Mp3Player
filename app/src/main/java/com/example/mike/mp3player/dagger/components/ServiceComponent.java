package com.example.mike.mp3player.dagger.components;

import com.example.mike.mp3player.dagger.modules.service.MediaLibraryModule;
import com.example.mike.mp3player.dagger.modules.service.MediaPlayerAdapterModule;
import com.example.mike.mp3player.dagger.modules.service.MediaRetrieverModule;
import com.example.mike.mp3player.dagger.modules.service.MediaSessionCallbackModule;
import com.example.mike.mp3player.service.MediaPlaybackService;
import com.example.mike.mp3player.service.library.MediaLibrary;
import com.example.mike.mp3player.service.library.mediaretriever.MediaRetriever;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {MediaLibraryModule.class, MediaRetrieverModule.class,
        MediaPlayerAdapterModule.class, MediaSessionCallbackModule.class})
public interface ServiceComponent {

    MediaRetriever mediaRetriever();
    MediaLibrary provideMediaLibrary();

    void inject(MediaPlaybackService mediaPlaybackService);
    void injectMediaRetriever(MediaLibrary mediaLibrary);

}
