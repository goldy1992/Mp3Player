package com.github.goldy1992.mp3player.service.library.data.search.managers

import androidx.media3.common.MediaItem
import com.github.goldy1992.mp3player.service.library.ContentManager
import com.github.goldy1992.mp3player.service.library.data.search.SearchDao

abstract class SearchDatabaseManager<T>(private val contentManager: ContentManager,
                                                       private val dao: SearchDao<T>,
                                                       private val rootCategoryId: String) {
    abstract fun createFromMediaItem(item: MediaItem): T?
    fun insert(item: MediaItem) {
        val t = createFromMediaItem(item)
        dao.insert(t!!)
    }

    @Suppress("UNCHECKED_CAST")
    suspend fun reindex() {
        val results = contentManager.getChildren(rootCategoryId).children
        val entries = buildResults(results)
        deleteOld(entries)
        // replace any new entries
        dao.insertAll(entries as List<T>)
    }

    private fun deleteOld(entries: List<T?>) { // Delete old entries i.e. files that have been deleted.
        val ids: MutableList<String?> = ArrayList()
        for (entry in entries) {
            //ids.add(entry!!.id)
        }
        // remove old entries
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