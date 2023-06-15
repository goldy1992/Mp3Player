package com.github.goldy1992.mp3player.service.dagger.modules

import android.content.ContentResolver
import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds
import com.github.goldy1992.mp3player.service.library.content.filter.SongsFromFolderResultsFilter
import com.github.goldy1992.mp3player.service.library.content.parser.AlbumsResultsParser
import com.github.goldy1992.mp3player.service.library.content.parser.FolderResultsParser
import com.github.goldy1992.mp3player.service.library.content.parser.SongResultsParser
import com.github.goldy1992.mp3player.service.library.content.retrievers.AlbumsRetriever
import com.github.goldy1992.mp3player.service.library.content.retrievers.ContentRetrievers
import com.github.goldy1992.mp3player.service.library.content.retrievers.ContentRetrieversDefaultImpl
import com.github.goldy1992.mp3player.service.library.content.retrievers.FoldersRetriever
import com.github.goldy1992.mp3player.service.library.content.retrievers.FoldersRetrieverAutomationImpl
import com.github.goldy1992.mp3player.service.library.content.retrievers.RootRetriever
import com.github.goldy1992.mp3player.service.library.content.retrievers.SongsFromAlbumRetriever
import com.github.goldy1992.mp3player.service.library.content.retrievers.SongsFromFolderRetriever
import com.github.goldy1992.mp3player.service.library.content.retrievers.SongsRetriever
import com.github.goldy1992.mp3player.service.library.content.retrievers.SongsRetrieverAutomationImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.scopes.ServiceScoped

@InstallIn(ServiceComponent::class)
@Module
class CustomRetrieversModule {

    @Provides
    @ServiceScoped
    fun providesSongsRetrieverAutomationImpl(
        contentResolver: ContentResolver,
        resultsParser: SongResultsParser
    ): SongsRetriever {
        return SongsRetrieverAutomationImpl(contentResolver, resultsParser)
    }

    @Provides
    @ServiceScoped
    fun providesSongsFromFolderRetriever(
        contentResolver: ContentResolver,
        resultsParser: SongResultsParser,
        songsFromFolderResultsFilter: SongsFromFolderResultsFilter
    ): SongsFromFolderRetriever {
        return SongsFromFolderRetriever(
            contentResolver,
            resultsParser,
            songsFromFolderResultsFilter
        )
    }

    @Provides
    @ServiceScoped
    fun providesSongsFromAlbumRetriever(
        contentResolver: ContentResolver,
        resultsParser: SongResultsParser
    ): SongsFromAlbumRetriever {
        return SongsFromAlbumRetriever(contentResolver, resultsParser)
    }

    @Provides
    @ServiceScoped
    fun providesFoldersRetrieverAutomationImpl(
        contentResolver: ContentResolver,
        resultsParser: FolderResultsParser
    ): FoldersRetriever {
        return FoldersRetrieverAutomationImpl(contentResolver, resultsParser)
    }

    @Provides
    @ServiceScoped
    fun providesAlbumsRetriever(
        contentResolver: ContentResolver,
        resultsParser: AlbumsResultsParser
    ): AlbumsRetriever {
        return AlbumsRetriever(contentResolver, resultsParser)
    }

    @Provides
    @ServiceScoped
    fun providesRootRetriever(mediaItemTypeIds: MediaItemTypeIds): RootRetriever {
        return RootRetriever(mediaItemTypeIds)
    }
}
