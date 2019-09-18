package com.example.mike.mp3player.dagger.modules.service;

import android.support.v4.media.MediaBrowserCompat;

import com.example.mike.mp3player.commons.MediaItemType;
import com.example.mike.mp3player.service.PlaybackManager;
import com.example.mike.mp3player.service.library.ContentManager;

import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class PlaybackManagerModule {
    @Singleton
    @Provides
    public PlaybackManager providePlaybackManager(ContentManager contentManager, Map<MediaItemType, String> ids) {
        List<MediaBrowserCompat.MediaItem> items = contentManager.getPlaylist(ids.get(MediaItemType.SONGS));
        return new PlaybackManager(items, 0);
    }
}
