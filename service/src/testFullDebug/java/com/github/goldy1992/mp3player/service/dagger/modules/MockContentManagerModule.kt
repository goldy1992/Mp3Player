package com.github.goldy1992.mp3player.service.dagger.modules

import android.content.ContentResolver
import androidx.media3.common.MediaItem
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.service.library.ContentManager
import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.scopes.ServiceScoped
import org.mockito.kotlin.mock
import javax.inject.Named

@InstallIn(ServiceComponent::class)
@Module
class MockContentManagerModule {
    @Provides
    @ServiceScoped
    fun providesContentResolver(): ContentResolver {
        return mock<ContentResolver>()
    }

    @Provides
    @ServiceScoped
    @Named("starting_playlist")
    fun providesInitialPlaylist(contentManager: ContentManager, ids: MediaItemTypeIds): List<MediaItem>? {
        return contentManager.getPlaylist(ids.getId(MediaItemType.SONGS))
    }
}