package com.github.goldy1992.mp3player.service.dagger.modules.service

import android.content.ContentResolver
import com.github.goldy1992.mp3player.commons.dagger.scopes.ComponentScope
import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds
import com.github.goldy1992.mp3player.service.library.content.filter.FolderSearchResultsFilter
import com.github.goldy1992.mp3player.service.library.content.parser.FolderResultsParser
import com.github.goldy1992.mp3player.service.library.content.parser.SongResultsParser
import com.github.goldy1992.mp3player.service.library.content.searcher.FolderSearcher
import com.github.goldy1992.mp3player.service.library.content.searcher.SongSearcher
import com.github.goldy1992.mp3player.service.library.search.FolderDao
import com.github.goldy1992.mp3player.service.library.search.SongDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.scopes.ServiceScoped

@InstallIn(ServiceComponent::class)
@Module
class ContentSearchersModule {

    @Provides
    @ServiceScoped
    fun providesSongSearcher(contentResolver: ContentResolver,
                             resultsParser: SongResultsParser,
                             mediaItemTypeIds: MediaItemTypeIds,
                             songDao: SongDao
                             ) : SongSearcher {
        return SongSearcher(contentResolver, resultsParser, mediaItemTypeIds, songDao)
    }

    @Provides
    @ServiceScoped
    fun providesFolderSearcher(contentResolver: ContentResolver,
                               resultsParser: FolderResultsParser,
                               folderResultsFilter : FolderSearchResultsFilter,
                               mediaItemTypeIds: MediaItemTypeIds,
                               folderDao: FolderDao
    ) : FolderSearcher {
        return FolderSearcher(contentResolver,
                resultsParser,
                folderResultsFilter,
                mediaItemTypeIds,
                folderDao)
    }

}