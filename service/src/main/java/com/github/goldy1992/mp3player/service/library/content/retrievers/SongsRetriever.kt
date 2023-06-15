package com.github.goldy1992.mp3player.service.library.content.retrievers

import android.content.ContentResolver
import android.database.Cursor
import android.provider.MediaStore
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.service.library.content.Projections.SONG_PROJECTION
import com.github.goldy1992.mp3player.service.library.content.parser.SongResultsParser

open class SongsRetriever constructor(contentResolver: ContentResolver,
                                      resultsParser: SongResultsParser)
    : ContentResolverRetriever(contentResolver, resultsParser, null) {
    override val type: MediaItemType
        get() = MediaItemType.SONG

    override fun performGetChildrenQuery(id: String?): Cursor? {
        return contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection,
            MediaStore.Audio.Media.IS_MUSIC + " = 1", null, null)
    }


    override val projection: Array<String?>?
        get() = SONG_PROJECTION.toTypedArray()
}