package com.example.mike.mp3player.dagger.components;

import com.example.mike.mp3player.dagger.modules.MediaLibraryModule;
import com.example.mike.mp3player.service.MediaPlaybackService;
import com.example.mike.mp3player.service.library.MediaLibrary;
import com.example.mike.mp3player.service.library.mediaretriever.MediaRetriever;
import com.example.mike.mp3player.dagger.modules.MediaRetrieverModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {MediaLibraryModule.class, MediaRetrieverModule.class})
public interface MediaLibraryComponent {

    MediaRetriever mediaRetriever();
    MediaLibrary provideMediaLibrary();

    void inject(MediaPlaybackService mediaPlaybackService);
    void injectMediaRetriever(MediaLibrary mediaLibrary);

}
