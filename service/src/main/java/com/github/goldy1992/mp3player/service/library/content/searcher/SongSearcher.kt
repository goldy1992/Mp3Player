package com.github.goldy1992.mp3player.service.library.content.searcher

import android.content.ContentResolver
import android.database.Cursor
import android.provider.BaseColumns
import android.provider.MediaStore
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds
import com.github.goldy1992.mp3player.service.library.content.Projections
import com.github.goldy1992.mp3player.service.library.content.parser.SongResultsParser
import com.github.goldy1992.mp3player.service.library.search.Song
import com.github.goldy1992.mp3player.service.library.search.SongDao
import org.apache.commons.lang3.StringUtils
import java.util.*
import javax.inject.Inject

open class SongSearcher

    constructor(contentResolver: ContentResolver,
               resultsParser: SongResultsParser,
               private val mediaItemTypeIds: MediaItemTypeIds,
               songDao: SongDao)
    : ContentResolverSearcher<Song>(
        contentResolver,
        resultsParser,
        null,
        songDao) {
    override val idPrefix: String
        get() = mediaItemTypeIds.getId(MediaItemType.SONG)!!

    override val projection: Array<String?>
        get() = Projections.SONG_PROJECTION.toTypedArray()

    override val searchCategory: MediaItemType?
        get() = MediaItemType.SONGS

    override fun performSearchQuery(query: String?): Cursor? {
        val results: List<Song>? = searchDatabase.query(query)
        val ids: MutableList<String> = ArrayList()
        val parameters: MutableList<String?> = ArrayList()
        if (results != null && !results.isEmpty()) {
            for (song in results) {
                ids.add(song.id)
                parameters.add(PARAMETER)
            }
        }
        val WHERE_CLAUSE = BaseColumns._ID + " IN(" + StringUtils.join(parameters, ", ") + ") COLLATE NOCASE"
        val WHERE_ARGS = ids.toTypedArray()
        return contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                WHERE_CLAUSE, WHERE_ARGS, null)
    }

    companion object {
        const val PARAMETER = "?"
    }

}