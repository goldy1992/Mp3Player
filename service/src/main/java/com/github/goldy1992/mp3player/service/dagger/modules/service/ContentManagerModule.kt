package com.github.goldy1992.mp3player.service.dagger.modules.service

import android.content.ContentResolver
import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.service.library.ContentManager
import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class ContentManagerModule {
    @Provides
    @Singleton
    fun provideContentResolver(context: Context): ContentResolver {
        return context.contentResolver
    }

    @Provides
    @Singleton
    @Named("starting_playlist")
    fun providesInitialPlaylist(contentManager: ContentManager, ids: MediaItemTypeIds): List<MediaBrowserCompat.MediaItem>? {
        return contentManager.getPlaylist(ids.getId(MediaItemType.SONGS))
    }
}