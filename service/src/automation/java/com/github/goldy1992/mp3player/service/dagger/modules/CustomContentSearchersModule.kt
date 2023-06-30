package com.github.goldy1992.mp3player.service.dagger.modules

import android.content.ContentResolver
import com.github.goldy1992.mp3player.commons.ServiceCoroutineScope
import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds
import com.github.goldy1992.mp3player.service.library.content.filter.FolderSearchResultsFilter
import com.github.goldy1992.mp3player.service.library.content.parser.AlbumsResultsParser
import com.github.goldy1992.mp3player.service.library.content.parser.FolderResultsParser
import com.github.goldy1992.mp3player.service.library.content.parser.SongResultsParser
import com.github.goldy1992.mp3player.service.library.content.searcher.AlbumSearcher
import com.github.goldy1992.mp3player.service.library.content.searcher.FolderSearcher
import com.github.goldy1992.mp3player.service.library.content.searcher.SongSearcher
import com.github.goldy1992.mp3player.service.library.data.search.AlbumDao
import com.github.goldy1992.mp3player.service.library.data.search.FolderDao
import com.github.goldy1992.mp3player.service.library.data.search.SongDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.scopes.ServiceScoped
import kotlinx.coroutines.CoroutineScope

@InstallIn(ServiceComponent::class)
@Module
class CustomContentSearchersModule {

    @Provides
    @ServiceScoped
    fun providesSongSearcher(contentResolver: ContentResolver,
                             resultsParser: SongResultsParser,
                             mediaItemTypeIds: MediaItemTypeIds,
                             songDao: SongDao,
                             @ServiceCoroutineScope scope: CoroutineScope
                             ) : SongSearcher {
        return SongSearcher(contentResolver, resultsParser, mediaItemTypeIds, songDao, scope)
    }

    @Provides
    @ServiceScoped
    fun providesFolderSearcher(contentResolver: ContentResolver,
                               resultsParser: FolderResultsParser,
                               folderResultsFilter : FolderSearchResultsFilter,
                               mediaItemTypeIds: MediaItemTypeIds,
                               folderDao: FolderDao,
                               @ServiceCoroutineScope scope: CoroutineScope
    ) : FolderSearcher {
        return FolderSearcher(contentResolver,
                resultsParser,
                folderResultsFilter,
                mediaItemTypeIds,
                folderDao,
                scope)
    }

    @Provides
    @ServiceScoped
    fun providesAlbumSearcher(contentResolver: ContentResolver,
                              resultsParser: AlbumsResultsParser,
                              mediaItemTypeIds: MediaItemTypeIds,
                              albumDao: AlbumDao,
                              @ServiceCoroutineScope scope: CoroutineScope
    ) : AlbumSearcher {
        return AlbumSearcher(contentResolver,
            resultsParser,
            mediaItemTypeIds,
            albumDao,
            scope)
    }

}