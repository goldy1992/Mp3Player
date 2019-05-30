package com.example.mike.mp3player.service.library;

import com.example.mike.mp3player.service.library.mediaretriever.MediaRetriever;
import com.example.mike.mp3player.service.library.mediaretriever.MediaRetrieverModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = MediaRetrieverModule.class)
public interface MediaLibraryComponent {

    MediaRetriever provideMediaRetriever();

    void inject(MediaLibrary mediaLibrary);
}
