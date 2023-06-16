package com.github.goldy1992.mp3player.service.library.data.search.managers

import android.util.Log
import androidx.media3.common.MediaItem
import com.github.goldy1992.mp3player.commons.LogTagger
import com.github.goldy1992.mp3player.service.library.ContentManager
import com.github.goldy1992.mp3player.service.library.data.search.SearchDao

abstract class SearchDatabaseManager<T>(private val contentManager: ContentManager,
                                                       private val dao: SearchDao<T>,
                                                       private val rootCategoryId: String) : LogTagger {
    abstract fun createFromMediaItem(item: MediaItem): T?
    fun insert(item: MediaItem) {
        val t = createFromMediaItem(item)
        dao.insert(t!!)
    }

    init {
        Log.d(logTag(), "init() with rootCategoryId")
    }

    @Suppress("UNCHECKED_CAST")
    suspend fun reindex() {
        Log.v(logTag(), "reindex() invoked")
        val results = contentManager.getChildren(rootCategoryId).children
        Log.d(logTag(), "reindex() number of children: ${results.size}")
        val entries = buildResults(results)
        deleteOld(entries)
        // replace any new entries
        dao.insertAll(entries as List<T>)
    }

    //abstract fun generateIdList(entries: List<T?>) : List<String>

    abstract fun getEntryId(entry : T) : String

    /**
     *  Delete old entries i.e. files that have been deleted.
     */
    private fun deleteOld(entries: List<T?>) {
        Log.v(logTag(), "deleteOld() invoked")
        val ids: MutableList<String> = ArrayList()
        for (entry in entries) {
            if (entry != null) {
                val entryId = getEntryId(entry)
                ids.add(entryId)
            }
        }
        dao.deleteOld(ids)
    }

    private fun buildResults(mediaItems: List<MediaItem>?): List<T?> {
        val entries: MutableList<T?> = ArrayList()
        for (mediaItem in mediaItems!!) {
            val entry = createFromMediaItem(mediaItem)
            if (null != entry) {
                entries.add(entry)
            }
        }
        return entries
    }


}