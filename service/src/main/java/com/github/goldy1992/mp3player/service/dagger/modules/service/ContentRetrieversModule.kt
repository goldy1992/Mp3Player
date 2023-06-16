package com.github.goldy1992.mp3player.service.dagger.modules.service

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
import com.github.goldy1992.mp3player.service.library.content.retrievers.RootRetriever
import com.github.goldy1992.mp3player.service.library.content.retrievers.SongsFromAlbumRetriever
import com.github.goldy1992.mp3player.service.library.content.retrievers.SongsFromFolderRetriever
import com.github.goldy1992.mp3player.service.library.content.retrievers.SongsRetriever
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.scopes.ServiceScoped

@InstallIn(ServiceComponent::class)
@Module
class ContentRetrieversModule {


    @Provides
    @ServiceScoped
    fun providesContentRetrieversDefaultImpl(
        mediaItemTypeIds: MediaItemTypeIds,
        rootRetriever: RootRetriever,
        songsRetriever: SongsRetriever,
        songsFromFolderRetriever: SongsFromFolderRetriever,
        songsFromAlbumRetriever: SongsFromAlbumRetriever,
        folderRetriever: FoldersRetriever,
        albumsRetriever: AlbumsRetriever
    ): ContentRetrieversDefaultImpl {
        return ContentRetrieversDefaultImpl(
            mediaItemTypeIds,
            rootRetriever,
            songsRetriever,
            folderRetriever,
            songsFromFolderRetriever,
            albumsRetriever,
            songsFromAlbumRetriever
        )
    }
}
