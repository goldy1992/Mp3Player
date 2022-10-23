package com.github.goldy1992.mp3player.service.library

import android.net.Uri
import androidx.media3.common.MediaItem
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.Normaliser.normalise
import com.github.goldy1992.mp3player.service.library.content.ContentRetrievers
import com.github.goldy1992.mp3player.service.library.content.request.ContentRequestParser
import com.github.goldy1992.mp3player.service.library.content.retriever.MediaItemFromIdRetriever
import com.github.goldy1992.mp3player.service.library.content.retriever.RootRetriever
import com.github.goldy1992.mp3player.service.library.content.retriever.SongFromUriRetriever
import dagger.hilt.android.scopes.ServiceScoped
import org.apache.commons.collections4.CollectionUtils
import java.util.*
import javax.inject.Inject

@ServiceScoped
class ContentManager @Inject constructor(private val contentRetrievers: ContentRetrievers,
                                         private val contentSearchers: ContentSearchers,
                                         private val contentRequestParser: ContentRequestParser,
                                         private val songFromUriRetriever: SongFromUriRetriever,
                                         private val mediaItemFromIdRetriever: MediaItemFromIdRetriever) {
    private val rootRetriever: RootRetriever = contentRetrievers.root

    /**
     * The id is in the following format
     * CATEGORY_ID | CHILD_ID where CHILD_ID is optional.
     * e.g. ROOT_CATEGORY_ID
     * FOLDER_CATEGORY_ID | FOLDER_ID
     * where X_CATEGORY_ID is a unique id defined by the service to ensure that the subscriber has
     * gained authority to access the parent category and also tells the method which category to
     * look in for the data.
     * @param parentId the id of the children to get
     * @return all the children of the id specified by the parentId parameter
     */
    fun getChildren(parentId: String): List<MediaItem> {
        val cachedItems = getCachedMediaItems(parentId)
        if (cachedItems != null) {
            return cachedItems
        }
        val request = contentRequestParser.parse(parentId)
        val result = contentRetrievers[request!!.contentRetrieverKey]?.getChildren(request) ?: emptyList()
        val itemType = result.firstOrNull()?.mediaMetadata?.extras?.get(Constants.MEDIA_ITEM_TYPE) ?: MediaItemType.NONE
        if (itemType != MediaItemType.NONE) {
            for (item in result) {
                itemMap[itemType]?.set(item.mediaId, result)
            }
        }

        return result
    }

    private val cachedSearchResults : MutableMap<String, List<MediaItem>> = HashMap()
    /**
     * @param query the search query
     * @return a list of media items which match the search query
     */
    suspend fun search(query: String, checkCache : Boolean = false): List<MediaItem> {
        val normalisedQuery = normalise(query)


        if (checkCache) {
            val cachedResult : List<MediaItem>? = cachedSearchResults[query]
            if (cachedResult != null) {
                return cachedResult
            }
        }
        val results: MutableList<MediaItem> = ArrayList()
        for (contentSearcher in contentSearchers.all) {
            val searchResults : List<MediaItem>? = contentSearcher.search(normalisedQuery)
            if (CollectionUtils.isNotEmpty(searchResults)) {
                val searchCategory = contentSearcher.searchCategory
                results.add(rootRetriever.getRootItem(searchCategory ?: MediaItemType.NONE))
                results.addAll(searchResults as List<MediaItem>)
            }
        }
        cachedSearchResults[query] = results
        return results
    }

    /**
     *
     */
    fun getItem(uri: Uri): MediaItem? {
        return songFromUriRetriever.getSong(uri)
    }

    /**
     *
     */
    fun getItem(id: Long): MediaItem? { /* TODO: in the future the content manager will need to know which type of URI will need
        *   to be parsed, e.g. song, album, artist etc. */
        return mediaItemFromIdRetriever.getItem(id)
    }

    /**
     * This method assumes that each song is playable
     */
    fun getMediaItems(ids : Collection<String>) : MutableList<MediaItem> {
        val toReturn = mutableListOf<MediaItem>()
        for (id in ids) {
            toReturn.add((itemMap[MediaItemType.SONG]?.get(id) ?: MediaItem.EMPTY) as MediaItem)
        }
        return toReturn
    }

    /**
     *
     * @param id the id of the playlist
     * @return the playlist
     */
    fun getPlaylist(id: String): List<MediaItem> {
        return getChildren(id)
    }

    val itemMap : EnumMap<MediaItemType, HashMap<String, List<MediaItem>>> = EnumMap(MediaItemType::class.java)
    init {
        MediaItemType.values().forEach { itemMap[it] = HashMap() }
    }

    fun getCachedMediaItems(id : String) : List<MediaItem>? {
        for (mediaItemType in itemMap.keys) {
            if (itemMap[mediaItemType]?.containsKey(id) == true) {
                return itemMap[mediaItemType]!![id] ?: emptyList()
            }
        }
        return null
    }

    companion object {
        const val CONTENT_SCHEME = "content"
        const val FILE_SCHEME = "file"
    }

}