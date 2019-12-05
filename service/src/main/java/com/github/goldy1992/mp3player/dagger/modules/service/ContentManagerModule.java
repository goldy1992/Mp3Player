package com.github.goldy1992.mp3player.dagger.modules.service;

import android.content.ContentResolver;
import android.content.Context;
import android.support.v4.media.MediaBrowserCompat.MediaItem;

import com.github.goldy1992.mp3player.commons.MediaItemType;
import com.github.goldy1992.mp3player.service.library.ContentManager;
import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds;

import java.util.List;

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
    public List<MediaItem> providesInitialPlaylist(ContentManager contentManager, MediaItemTypeIds ids) {
        return contentManager.getPlaylist(ids.getId(MediaItemType.SONGS));
    }


}
