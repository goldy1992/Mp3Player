package com.example.mike.mp3player.dagger.components;

import android.os.Handler;
import android.support.v4.media.session.MediaSessionCompat;

import com.example.mike.mp3player.dagger.modules.service.HandlerThreadModule;
import com.example.mike.mp3player.dagger.modules.service.MediaLibraryModule;
import com.example.mike.mp3player.dagger.modules.service.MediaPlayerAdapterModule;
import com.example.mike.mp3player.dagger.modules.service.MediaRetrieverModule;
import com.example.mike.mp3player.dagger.modules.service.MediaSessionCallbackModule;
import com.example.mike.mp3player.dagger.modules.service.MediaSessionCompatModule;
import com.example.mike.mp3player.dagger.modules.service.ServiceContextModule;
import com.example.mike.mp3player.dagger.modules.service.ServiceModule;
import com.example.mike.mp3player.service.MediaPlaybackService;
import com.example.mike.mp3player.service.MyNotificationManager;
import com.example.mike.mp3player.service.ServiceManager;
import com.example.mike.mp3player.service.library.MediaLibrary;
import com.example.mike.mp3player.service.library.mediaretriever.MediaRetriever;
import com.example.mike.mp3player.service.player.MediaPlayerAdapterBase;
import com.example.mike.mp3player.service.session.AudioBecomingNoisyBroadcastReceiver;
import com.example.mike.mp3player.service.session.MediaSessionAdapter;
import com.example.mike.mp3player.service.session.MediaSessionCallback;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
    HandlerThreadModule.class,
    MediaLibraryModule.class,
    MediaPlayerAdapterModule.class,
    MediaRetrieverModule.class,
    MediaSessionCallbackModule.class,
    MediaSessionCompatModule.class,
    ServiceContextModule.class,
    ServiceModule.class
})
public interface ServiceComponent {

    MediaPlayerAdapterBase provideMediaPlayerAdapter();
    MediaPlaybackService provideMediaPlaybackService();
    MediaRetriever mediaRetriever();
    MediaLibrary provideMediaLibrary();
    ServiceManager provideServiceManager();
    MyNotificationManager provideMyNotificationManager();
    MediaSessionCompat provideMediaSessionCompat();
    MediaSessionCompat.Token provideMediaSessionToken();
    MediaSessionAdapter provideMediaSessionAdapter();
    AudioBecomingNoisyBroadcastReceiver provideAudioBecomingNoisyBroadcastReceiver();
    Handler provideHandler();
    ApplicationComponent

    void inject(MediaPlaybackService mediaPlaybackService);
    void injectMediaRetriever(MediaLibrary mediaLibrary);
    void inject(MediaPlayerAdapterBase mediaPlayerAdapterBase);
    void inject(MediaSessionCallback mediaSessionCallback);

}
