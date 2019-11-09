package com.github.goldy1992.mp3player.dagger.modules.service;

import android.support.v4.media.MediaBrowserCompat;

import com.github.goldy1992.mp3player.service.PlaybackManager;
import com.github.goldy1992.mp3player.service.library.ContentManager;

import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class PlaybackManagerModule {
    @Singleton
    @Provides
    public PlaybackManager providePlaybackManager(ContentManager contentManager,
                                                  @Named("starting_playlist")List<MediaBrowserCompat.MediaItem> items) {
        return new PlaybackManager(items, 0);
    }
}
