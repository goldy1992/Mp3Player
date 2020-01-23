package com.github.goldy1992.mp3player.service.library.search.managers

import android.os.Handler
import android.support.v4.media.MediaBrowserCompat
import com.github.goldy1992.mp3player.service.library.ContentManager
import com.github.goldy1992.mp3player.service.library.search.SearchDao
import com.github.goldy1992.mp3player.service.library.search.SearchEntity
import java.util.*
import javax.inject.Named

abstract class SearchDatabaseManager<T : SearchEntity>(private val contentManager: ContentManager,
                                                        private val dao: SearchDao<T>,
                                                        private val rootCategoryId: String?) {
    abstract fun createFromMediaItem(item: MediaBrowserCompat.MediaItem): T?
    fun insert(item: MediaBrowserCompat.MediaItem) {
        val t = createFromMediaItem(item)
        dao!!.insert(t!!)
    }

    suspend fun reindex() {
        val results = contentManager.getChildren(rootCategoryId)
        val entries = buildResults(results)
        deleteOld(entries)
        // replace any new entries
        dao.insertAll(entries as List<T>)
    }

    private fun deleteOld(entries: List<T?>) { // Delete old entries i.e. files that have been deleted.
        val ids: MutableList<String?> = ArrayList()
        for (entry in entries) {
            ids.add(entry!!.id)
        }
        // remove old entries
        dao.deleteOld(ids)
    }

    private fun buildResults(mediaItems: List<MediaBrowserCompat.MediaItem>?): List<T?> {
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