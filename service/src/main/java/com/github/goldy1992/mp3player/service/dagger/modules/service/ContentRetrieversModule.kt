package com.github.goldy1992.mp3player.service.dagger.modules.service

import android.content.ContentResolver
import com.github.goldy1992.mp3player.commons.dagger.scopes.ComponentScope
import com.github.goldy1992.mp3player.service.library.content.parser.FolderResultsParser
import com.github.goldy1992.mp3player.service.library.content.parser.SongResultsParser
import com.github.goldy1992.mp3player.service.library.content.retriever.FoldersRetriever
import com.github.goldy1992.mp3player.service.library.content.retriever.SongsRetriever
import dagger.Module
import dagger.Provides

@Module
open class ContentRetrieversModule {

    @Provides
    @ComponentScope
    fun providesSongsRetriever(contentResolver: ContentResolver,
                               resultsParser: SongResultsParser) : SongsRetriever {
        return SongsRetriever(contentResolver, resultsParser)
    }

    @Provides
    @ComponentScope
    fun providesFoldersRetriever(contentResolver: ContentResolver,
                                 resultsParser: FolderResultsParser) : FoldersRetriever {
        return FoldersRetriever(contentResolver, resultsParser)
    }
}