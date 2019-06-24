package com.example.mike.mp3player.dagger.components;

import android.content.Context;

import com.example.mike.mp3player.MikesMp3PlayerBase;
import com.example.mike.mp3player.dagger.modules.ApplicationContextModule;
import com.example.mike.mp3player.dagger.modules.service.MediaPlayerAdapterModule;
import com.example.mike.mp3player.dagger.modules.service.MediaRetrieverModule;
import com.example.mike.mp3player.service.library.MediaLibrary;
import com.example.mike.mp3player.service.library.mediaretriever.MediaRetriever;
import com.example.mike.mp3player.service.player.MediaPlayerAdapterBase;
import com.example.mike.mp3player.service.session.MediaSessionCallback;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

/**
 * Provide objects that should have instances calculated on initialisation
 */
@Singleton
@Component (modules = {
        ApplicationContextModule.class,
        MediaPlayerAdapterModule.class,
        MediaRetrieverModule.class})
public interface ApplicationComponent {

    MediaPlayerAdapterBase provideMediaPlayerAdapter();
    MediaRetriever provideMediaRetriever();
    Context context();

    void inject(MikesMp3PlayerBase mp3PlayerBase);


}
