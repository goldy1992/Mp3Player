package com.github.goldy1992.mp3player.service.dagger.modules

import android.content.ContentResolver
import com.github.goldy1992.mp3player.commons.dagger.scopes.ComponentScope
import com.github.goldy1992.mp3player.service.dagger.modules.service.ContentRetrieversModule
import com.github.goldy1992.mp3player.service.library.content.parser.FolderResultsParser
import com.github.goldy1992.mp3player.service.library.content.parser.SongResultsParser
import com.github.goldy1992.mp3player.service.library.content.retriever.FoldersRetriever
import com.github.goldy1992.mp3player.service.library.content.retriever.FoldersRetrieverTestImpl
import com.github.goldy1992.mp3player.service.library.content.retriever.SongsRetriever
import com.github.goldy1992.mp3player.service.library.content.retriever.SongsRetrieverTestImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AndroidTestContentRetrieversModule  {
    @Provides
    @ComponentScope
    fun providesSongsRetriever(contentResolver: ContentResolver,
                                    resultsParser: SongResultsParser) : SongsRetriever {
        return SongsRetrieverTestImpl(contentResolver, resultsParser)
    }

    @Provides
    @ComponentScope
    fun providesFoldersRetriever(contentResolver: ContentResolver,
                                      resultsParser: FolderResultsParser) : FoldersRetriever {
        return FoldersRetrieverTestImpl(contentResolver, resultsParser)
    }
}