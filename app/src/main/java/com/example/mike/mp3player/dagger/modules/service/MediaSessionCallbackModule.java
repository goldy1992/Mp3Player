package com.example.mike.mp3player.dagger.modules.service;

import android.os.Handler;
import android.os.Looper;

import com.example.mike.mp3player.service.MediaPlaybackService;
import com.example.mike.mp3player.service.PlaybackManager;
import com.example.mike.mp3player.service.ServiceManager;
import com.example.mike.mp3player.service.library.MediaLibrary;
import com.example.mike.mp3player.service.player.MediaPlayerAdapterBase;
import com.example.mike.mp3player.service.session.AudioBecomingNoisyBroadcastReceiver;
import com.example.mike.mp3player.service.session.MediaSessionAdapter;
import com.example.mike.mp3player.service.session.MediaSessionCallback;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MediaSessionCallbackModule {

    @Singleton
    @Provides
    public MediaSessionCallback provideMediaSessionCallback(MediaPlaybackService service,
                                                            MediaLibrary mediaLibrary,
                                                            PlaybackManager playbackManager,
                                                            MediaPlayerAdapterBase mediaPlayerAdapterBase,
                                                            MediaSessionAdapter mediaSessionAdapter,
                                                            ServiceManager serviceManager,
                                                            AudioBecomingNoisyBroadcastReceiver broadcastReceiver,
                                                            Handler handler) {
        return new MediaSessionCallback(service,
                mediaLibrary,
                playbackManager,
                mediaPlayerAdapterBase,
                mediaSessionAdapter,
                serviceManager,
                broadcastReceiver,
                handler);
    }

    @Singleton
    @Provides
    public PlaybackManager providePlaybackManager() {
        return null;
    }


}
