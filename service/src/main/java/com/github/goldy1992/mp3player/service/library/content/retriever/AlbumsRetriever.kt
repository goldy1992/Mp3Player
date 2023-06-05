package com.github.goldy1992.mp3player.service.library.content.retriever

import android.content.ContentResolver
import android.database.Cursor
import android.provider.MediaStore
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.service.library.content.Projections.ALBUM_PROJECTION
import com.github.goldy1992.mp3player.service.library.content.parser.AlbumsResultsParser
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Inject

@ServiceScoped
class AlbumsRetriever

    @Inject
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