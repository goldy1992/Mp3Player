package com.example.mike.mp3player.dagger.modules;

import android.os.Handler;

import com.example.mike.mp3player.service.MockMediaSessionCallback;
import com.example.mike.mp3player.service.PlaybackManager;
import com.example.mike.mp3player.service.ServiceManager;
import com.example.mike.mp3player.service.library.ContentManager;
import com.example.mike.mp3player.service.library.MediaLibrary;
import com.example.mike.mp3player.service.player.MediaPlayerAdapter;
import com.example.mike.mp3player.service.session.AudioBecomingNoisyBroadcastReceiver;
import com.example.mike.mp3player.service.session.MediaSessionAdapter;
import com.example.mike.mp3player.service.session.MediaSessionCallback;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MockMediaSessionCallbackModule {
    @Singleton
    @Provides
    public MediaSessionCallback provideMediaSessionCallback(ContentManager mediaLibrary,
                                                            PlaybackManager playbackManager,
                                                            MediaPlayerAdapter mediaPlayerAdapter,
                                                            MediaSessionAdapter mediaSessionAdapter,
                                                            ServiceManager serviceManager,
                                                            AudioBecomingNoisyBroadcastReceiver broadcastReceiver,
                                                            Handler handler) {
        return new MockMediaSessionCallback(mediaLibrary,
                playbackManager,
                mediaPlayerAdapter,
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
