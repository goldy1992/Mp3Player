package com.example.mike.mp3player.dagger.modules.service;

import android.content.ContentResolver;
import android.content.Context;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserCompat.MediaItem;

import com.example.mike.mp3player.commons.MediaItemType;
import com.example.mike.mp3player.service.library.ContentManager;

import java.util.List;
import java.util.Map;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ContentManagerModule {

    @Provides
    @Singleton
    public ContentResolver provideContentResolver(Context context) {
        return context.getContentResolver();
    }

    @Provides
    @Singleton
    @Named("starting_playlist")
    public List<MediaItem> providesInitialPlaylist(ContentManager contentManager, Map<MediaItemType, String> ids) {
        return contentManager.getPlaylist(ids.get(MediaItemType.SONGS));
    }


}
