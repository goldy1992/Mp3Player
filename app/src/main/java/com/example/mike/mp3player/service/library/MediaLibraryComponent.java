package com.example.mike.mp3player.service.library;

import com.example.mike.mp3player.service.MediaPlaybackService;
import com.example.mike.mp3player.service.library.mediaretriever.MediaRetriever;
import com.example.mike.mp3player.service.library.mediaretriever.MediaRetrieverComponent;
import com.example.mike.mp3player.service.library.mediaretriever.MediaRetrieverModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {MediaLibraryModule.class}, dependencies = MediaRetrieverComponent.class)
public interface MediaLibraryComponent {

    MediaLibrary provideMediaLibrary();

    void inject(MediaPlaybackService mediaPlaybackService);


}
