package com.github.goldy1992.mp3player.service.library.content.retriever

import android.content.ContentResolver
import android.database.Cursor
import android.os.Handler
import android.provider.MediaStore
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.service.library.content.Projections.SONG_PROJECTION
import com.github.goldy1992.mp3player.service.library.content.parser.SongResultsParser
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
open class SongsRetriever @Inject constructor(contentResolver: ContentResolver,
                                              resultsParser: SongResultsParser,
                                              @Named("worker") handler: Handler) : ContentResolverRetriever(contentResolver, resultsParser, handler, null) {
    override val type: MediaItemType?
        get() = MediaItemType.SONG

    public override fun performGetChildrenQuery(id: String?): Cursor? {
        return contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection,
                null, null, null)
    }

    override val projection: Array<String?>?
        get() = SONG_PROJECTION.toTypedArray()
}