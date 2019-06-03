package com.example.mike.mp3player.service.library.mediaretriever;

import android.content.Context;

import com.example.mike.mp3player.service.library.MediaLibrary;
import com.example.mike.mp3player.service.library.MediaLibraryComponent;

import javax.inject.Singleton;

import dagger.Component;
@Singleton
@Component(modules = MediaRetrieverModule.class)
public interface MediaRetrieverComponent {

    void inject(MediaLibrary mediaLibrary);
}
