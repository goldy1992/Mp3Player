package com.github.goldy1992.mp3player.service.library.content.searcher

import android.content.ContentResolver
import android.database.Cursor
import android.provider.BaseColumns
import android.provider.MediaStore
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds
import com.github.goldy1992.mp3player.service.library.content.Projections
import com.github.goldy1992.mp3player.service.library.content.parser.AlbumsResultsParser
import com.github.goldy1992.mp3player.service.library.data.search.Album
import com.github.goldy1992.mp3player.service.library.data.search.AlbumDao
import kotlinx.coroutines.CoroutineScope
import org.apache.commons.lang3.StringUtils

open class AlbumSearcher

    constructor(contentResolver: ContentResolver,
                resultsParser: AlbumsResultsParser,
                private val mediaItemTypeIds: MediaItemTypeIds,
                albumDao: AlbumDao,
                scope: CoroutineScope)
    : ContentResolverSearcher<Album>(
        contentResolver,
        resultsParser,
        null,
        albumDao,
        scope) {
    override val idPrefix: String
        get() = mediaItemTypeIds.getId(MediaItemType.SONG)

    override val projection: Array<String?>
        get() = Projections.ALBUM_PROJECTION.toTypedArray()

    override val searchCategory: MediaItemType?
        get() = MediaItemType.ALBUMS

    override suspend fun performSearchQuery(query: String?): Cursor? {
        val results: List<Album>? = searchDatabase.query(query)
        val ids: MutableList<String> = ArrayList()
        val parameters: MutableList<String?> = ArrayList()
        if (results != null && !results.isEmpty()) {
            for (album in results) {
                ids.add(album.id)
                parameters.add(PARAMETER)
            }
        }
        val whereClause = BaseColumns._ID + " IN(" + StringUtils.join(parameters, ", ") + ") COLLATE NOCASE"
        val whereArgs = ids.toTypedArray()
        return contentResolver.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                projection,
                whereClause, whereArgs, null)
    }

    companion object {
        const val PARAMETER = "?"
    }

}