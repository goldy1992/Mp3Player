package com.example.mike.mp3player.dagger.modules.service;

import android.os.Handler;
import android.support.v4.media.MediaBrowserCompat;

import com.example.mike.mp3player.commons.MediaItemType;
import com.example.mike.mp3player.service.PlaybackManager;
import com.example.mike.mp3player.service.ServiceManager;
import com.example.mike.mp3player.service.library.ContentManager;
import com.example.mike.mp3player.service.library.MediaLibrary;
import com.example.mike.mp3player.service.player.MediaPlayerAdapter;
import com.example.mike.mp3player.service.session.AudioBecomingNoisyBroadcastReceiver;
import com.example.mike.mp3player.service.session.MediaSessionAdapter;
import com.example.mike.mp3player.service.session.MediaSessionCallback;

import java.util.EnumMap;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MediaSessionCallbackModule {

    @Singleton
    @Provides
    public MediaSessionCallback provideMediaSessionCallback(ContentManager mediaLibrary,
                                                            PlaybackManager playbackManager,
                                                            MediaPlayerAdapter mediaPlayerAdapter,
                                                            MediaSessionAdapter mediaSessionAdapter,
                                                            ServiceManager serviceManager,
                                                            AudioBecomingNoisyBroadcastReceiver broadcastReceiver,
                                                            Handler handler) {
        return new MediaSessionCallback(mediaLibrary,
                playbackManager,
                mediaPlayerAdapter,
                mediaSessionAdapter,
                serviceManager,
                broadcastReceiver,
                handler);
    }

    @Singleton
    @Provides
    public PlaybackManager providePlaybackManager(ContentManager contentManager, EnumMap<MediaItemType, String> ids) {
        List<MediaBrowserCompat.MediaItem> items = contentManager.getPlaylist(ids.get(MediaItemType.SONGS));
        return new PlaybackManager(items, 0);
    }


}
