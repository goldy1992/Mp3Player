package com.github.goldy1992.mp3player.service.library.content.retriever

import android.content.ContentResolver
import android.database.Cursor
import android.os.Handler
import android.provider.MediaStore
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.service.library.content.Projections.FOLDER_PROJECTION
import com.github.goldy1992.mp3player.service.library.content.parser.FolderResultsParser
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
open class FoldersRetriever

    @Inject
    constructor(contentResolver: ContentResolver,
                resultsParser: FolderResultsParser)

    : ContentResolverRetriever(contentResolver, resultsParser, null) {
    override val type: MediaItemType
        get() = MediaItemType.FOLDER

    override fun performGetChildrenQuery(id: String?): Cursor? {
        return contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection,
                null, null, null)
    }

    override val projection: Array<String?>?
        get() = FOLDER_PROJECTION.toTypedArray()
}