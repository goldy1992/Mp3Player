package com.example.mike.mp3player.dagger.components;

import android.support.v4.media.session.MediaSessionCompat;

import com.example.mike.mp3player.dagger.modules.service.MediaLibraryModule;
import com.example.mike.mp3player.dagger.modules.service.MediaPlayerAdapterModule;
import com.example.mike.mp3player.dagger.modules.service.MediaRetrieverModule;
import com.example.mike.mp3player.dagger.modules.service.MediaSessionCallbackModule;
import com.example.mike.mp3player.dagger.modules.service.ServiceContextModule;
import com.example.mike.mp3player.dagger.modules.service.ServiceModule;
import com.example.mike.mp3player.service.MediaPlaybackService;
import com.example.mike.mp3player.service.MyNotificationManager;
import com.example.mike.mp3player.service.ServiceManager;
import com.example.mike.mp3player.service.library.MediaLibrary;
import com.example.mike.mp3player.service.library.mediaretriever.MediaRetriever;
import com.example.mike.mp3player.service.session.MediaSessionAdapter;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        MediaLibraryModule.class,
        MediaRetrieverModule.class,
        MediaPlayerAdapterModule.class,
        MediaSessionCallbackModule.class,
        ServiceModule.class,
        ServiceContextModule.class})
public interface ServiceComponent {

    MediaPlaybackService provideMediaPlaybackService();
    MediaRetriever mediaRetriever();
    MediaLibrary provideMediaLibrary();
    ServiceManager provideServiceManager();
    MyNotificationManager provideMyNotificationManager();
    MediaSessionCompat provideMediaSessiomCompat();
    MediaSessionCompat.Token provideMediaSessionToken();
    MediaSessionAdapter mediaSessionAdapter();

    void inject(MediaPlaybackService mediaPlaybackService);
    void injectMediaRetriever(MediaLibrary mediaLibrary);

}
