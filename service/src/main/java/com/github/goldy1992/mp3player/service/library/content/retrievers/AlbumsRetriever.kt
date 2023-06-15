package com.github.goldy1992.mp3player.service.library.content.retrievers

import android.content.ContentResolver
import android.database.Cursor
import android.provider.MediaStore
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.service.library.content.Projections.ALBUM_PROJECTION
import com.github.goldy1992.mp3player.service.library.content.parser.AlbumsResultsParser


class AlbumsRetriever

    constructor(contentResolver: ContentResolver,
                resultsParser : AlbumsResultsParser) : ContentResolverRetriever(contentResolver, resultsParser, null) {
    override fun performGetChildrenQuery(id: String?): Cursor? {
        return contentResolver.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, projection, null, null, null)
    }

    override val projection: Array<String?>
        get() = ALBUM_PROJECTION.toTypedArray()
    override val type: MediaItemType
        get() = MediaItemType.ALBUM


}