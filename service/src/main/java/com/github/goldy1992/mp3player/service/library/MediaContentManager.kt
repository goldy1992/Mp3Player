package com.github.goldy1992.mp3player.service.library

import android.net.Uri
import android.util.Log
import androidx.media3.common.MediaItem
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import com.github.goldy1992.mp3player.commons.Normaliser.normalise
import com.github.goldy1992.mp3player.service.library.content.retriever.ContentRetrievers
import com.github.goldy1992.mp3player.service.library.content.retriever.MediaItemFromIdRetriever
import com.github.goldy1992.mp3player.service.library.content.retriever.RootRetriever
import com.github.goldy1992.mp3player.service.library.content.retriever.SongFromUriRetriever
import com.github.goldy1992.mp3player.service.library.content.searcher.ContentSearchers
import com.google.common.collect.ImmutableList
import dagger.hilt.android.scopes.ServiceScoped
import org.apache.commons.collections4.CollectionUtils
import java.util.*
import javax.inject.Inject

@ServiceScoped
class MediaContentManager @Inject constructor(private val contentRetrievers: ContentRetrievers,
                                              private val contentSearchers: ContentSearchers,
                                              private val songFromUriRetriever: SongFromUriRetriever,
                                              private val mediaItemFromIdRetriever: MediaItemFromIdRetriever) :
    ContentManager {
    private val rootRetriever: RootRetriever = contentRetrievers.root
    override suspend fun initialise(rootMediaItem: MediaItem) {
        val rootNode = MediaItemNode(rootMediaItem)
        build(rootNode)
        this.rootNode = rootNode
        Log.i(logTag(), "built tree")
        val nodeMap : MutableMap<String, MediaItemNode> = HashMap()
        buildMediaNodeMap(rootNode, nodeMap)
        this.nodeMap = nodeMap
        Log.i(logTag(), "MediaItemNode map built")
    }

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
    override suspend fun getChildren(parentId: String): List<MediaItem> {

        if (this.nodeMap == null) {
            return emptyList()
        }

        val parentNode: MediaItemNode? = nodeMap!![parentId]
        val children = parentNode?.getChildren()
        Log.i(logTag(), "parentId: ${parentId}, children count: ${children?.count() ?: 0}")
        return children?.map(MediaItemNode::item) ?: emptyList()
    }

    override suspend fun getChildren(mediaItemType: MediaItemType): List<MediaItem> {
        val mediaItemId : String? = rootNode?.getChildren()?.filter { x -> x.mediaItemType == mediaItemType }?.first()?.id
        return if (mediaItemId != null) getChildren(mediaItemId) else emptyList()
    }

    override suspend fun getContentById(id: String): MediaItem {
        return nodeMap?.get(id)?.item ?: MediaItem.EMPTY
    }

    override suspend fun getContentByIds(ids: List<String>): List<MediaItem> {
        val toReturn = mutableListOf<MediaItem>()

        for (id in ids) {
            toReturn.add(getContentById(id))
        }
        return toReturn.toList()
    }

    override suspend fun search(query: String): List<MediaItem> {
        val normalisedQuery = normalise(query)

        val cachedResult : List<MediaItem>? = cachedSearchResults[query]
        if (cachedResult != null) {
            return cachedResult
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

    override fun logTag(): String {
        return "MediaContentManager"
    }

    private val cachedSearchResults : MutableMap<String, List<MediaItem>> = HashMap()



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

    private val itemMap : EnumMap<MediaItemType, HashMap<String, List<MediaItem>>> = EnumMap(MediaItemType::class.java)
    init {
        MediaItemType.values().forEach { itemMap[it] = HashMap() }
    }

    companion object {
        const val CONTENT_SCHEME = "content"
        const val FILE_SCHEME = "file"
    }

    private class MediaItemNode(val item: MediaItem) {
        val id = item.mediaId

        val mediaItemType = MediaItemUtils.getMediaItemType(item)
        private val children: MutableList<MediaItemNode> = ArrayList()

        fun addChild(childNode : MediaItemNode) {
            this.children.add(childNode)
        }

        fun addChildren(childNodes : Collection<MediaItemNode>) {
            this.children.addAll(childNodes)
        }
        fun getChildren(): List<MediaItemNode> {
            return ImmutableList.copyOf(children)
        }

        fun hasChildren() : Boolean {
            return children.isNotEmpty()
        }
    }

    private var rootNode : MediaItemNode? = null

    private var nodeMap : Map<String, MediaItemNode>? = null

    private fun buildMediaNodeMap(node : MediaItemNode, nodeMap: MutableMap<String, MediaItemNode>) {
        if (node.hasChildren()) {
            node.getChildren().forEach{buildMediaNodeMap(it, nodeMap)}
        }
        nodeMap[node.id] = node
    }

    private fun build(node : MediaItemNode) {
        val children = contentRetrievers.getContentRetriever(node.mediaItemType ?: MediaItemType.NONE)?.getChildren(node.id)
        if (children != null) {
            for (child in children) {
                val childNode = MediaItemNode(child)
                node.addChild(childNode)

                if (childNode.mediaItemType != MediaItemType.SONG) {
                    build(childNode)
                }
            }
        }
    }

}