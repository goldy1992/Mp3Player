package com.example.mike.mp3player.dagger.components;

import android.content.Context;
import android.os.Handler;
import android.support.v4.media.session.MediaSessionCompat;

import com.example.mike.mp3player.dagger.modules.service.HandlerThreadModule;
import com.example.mike.mp3player.dagger.modules.service.MediaLibraryModule;
import com.example.mike.mp3player.dagger.modules.service.MediaPlayerAdapterModule;
import com.example.mike.mp3player.dagger.modules.service.MediaRetrieverModule;
import com.example.mike.mp3player.dagger.modules.service.MediaSessionCallbackModule;
import com.example.mike.mp3player.dagger.modules.service.MediaSessionCompatModule;
import com.example.mike.mp3player.dagger.modules.service.ServiceModule;
import com.example.mike.mp3player.service.MediaPlaybackService;
import com.example.mike.mp3player.service.MyNotificationManager;
import com.example.mike.mp3player.service.ServiceManager;
import com.example.mike.mp3player.service.library.MediaLibrary;
import com.example.mike.mp3player.service.library.mediaretriever.MediaRetriever;
import com.example.mike.mp3player.service.player.MediaPlayerAdapter;
import com.example.mike.mp3player.service.session.AudioBecomingNoisyBroadcastReceiver;
import com.example.mike.mp3player.service.session.MediaSessionAdapter;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component(modules = {
    HandlerThreadModule.class,
    MediaLibraryModule.class,
    MediaPlayerAdapterModule.class,
    MediaRetrieverModule.class,
    MediaSessionCallbackModule.class,
    MediaSessionCompatModule.class,
    ServiceModule.class
})
public interface ServiceComponent {

    AudioBecomingNoisyBroadcastReceiver provideAudioBecomingNoisyBroadcastReceiver();
    Handler provideHandler();
    MediaLibrary provideMediaLibrary();
    MediaPlayerAdapter provideMediaPlayerAdapter();

    MediaRetriever mediaRetriever();
    MediaSessionAdapter provideMediaSessionAdapter();
    MediaSessionCompat provideMediaSessionCompat();
    MediaSessionCompat.Token provideMediaSessionToken();
    MyNotificationManager provideMyNotificationManager();
    ServiceManager provideServiceManager();
    String workerId();

    void inject(MediaPlaybackService mediaPlaybackService);

    @Component.Factory
    interface Factory {
        ServiceComponent create(@BindsInstance Context context,
                                @BindsInstance MediaPlaybackService mediaPlaybackService,
                                @BindsInstance String workerId);
    }
}
