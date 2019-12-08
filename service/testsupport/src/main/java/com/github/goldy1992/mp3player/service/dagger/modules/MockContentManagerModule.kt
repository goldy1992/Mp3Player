package com.github.goldy1992.mp3player.service.dagger.modules

import android.support.v4.media.MediaBrowserCompat
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.service.library.ContentManager
import dagger.Module
import dagger.Provides
import org.mockito.Mockito
import javax.inject.Named
import javax.inject.Singleton

@Module
class MockContentManagerModule {
    @Provides
    @Singleton
    fun provideContentManager(): ContentManager {
        return Mockito.mock(ContentManager::class.java)
    }

    @Provides
    @Singleton
    @Named("starting_playlist")
    fun providesInitialPlaylist(contentManager: ContentManager, ids: Map<MediaItemType?, String?>): List<MediaBrowserCompat.MediaItem>? {
        return contentManager.getPlaylist(ids[MediaItemType.SONGS])
    }
}