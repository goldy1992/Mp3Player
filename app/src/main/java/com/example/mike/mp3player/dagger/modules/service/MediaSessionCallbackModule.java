package com.example.mike.mp3player.dagger.modules.service;

import android.content.Context;
import android.os.Handler;

import com.example.mike.mp3player.dagger.components.ApplicationComponent;
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

@Module(subcomponents = ApplicationComponent.class)
public class MediaSessionCallbackModule {

    @Singleton
    @Provides
    public MediaSessionCallback provideMediaSessionCallback(Context context,
                                                            MediaLibrary mediaLibrary,
                                                            PlaybackManager playbackManager,
                                                            MediaPlayerAdapterBase mediaPlayerAdapterBase,
                                                            MediaSessionAdapter mediaSessionAdapter,
                                                            ServiceManager serviceManager,
                                                            AudioBecomingNoisyBroadcastReceiver broadcastReceiver,
                                                            Handler handler) {
        return new MediaSessionCallback(context,
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
        return new PlaybackManager();
    }


}
