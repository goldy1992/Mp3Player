package com.github.goldy1992.mp3player.service.library

import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaLibraryService.LibraryParams
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import com.github.goldy1992.mp3player.commons.Normaliser.normalise
import com.github.goldy1992.mp3player.commons.PermissionsNotifier
import com.github.goldy1992.mp3player.service.RootAuthenticator
import com.github.goldy1992.mp3player.service.library.content.retriever.ContentRetrievers
import com.github.goldy1992.mp3player.service.library.content.retriever.MediaItemFromIdRetriever
import com.github.goldy1992.mp3player.service.library.content.retriever.RootRetriever
import com.github.goldy1992.mp3player.service.library.content.retriever.SongFromUriRetriever
import com.github.goldy1992.mp3player.service.library.content.searcher.ContentSearchers
import com.google.common.collect.ImmutableList
import dagger.hilt.android.scopes.ServiceScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.apache.commons.collections4.CollectionUtils
import java.util.*
import javax.inject.Inject

@ServiceScoped
class MediaContentManager @Inject constructor(permissionsNotifier : PermissionsNotifier,
                                              private val contentRetrievers: ContentRetrievers,
                                              private val contentSearchers: ContentSearchers,
                                              rootAuthenticator: RootAuthenticator,
                                              private val scope : CoroutineScope,
) : ContentManager {

    private val rootNode = MediaItemNode(rootAuthenticator.getRootItem())
    private val nodeMap : MutableMap<String, MediaItemNode> = mutableMapOf()
    init {
        nodeMap[rootNode.id] = rootNode
        // initialise the root items
        val children = contentRetrievers.getContentRetriever(MediaItemType.ROOT)?.getChildren(rootNode.id)
        if (children != null) {
            for (child in children) {
                val childNode = MediaItemNode(child)
                rootNode.addChild(childNode)
                nodeMap[childNode.id] = childNode
            }
        }

        scope.launch {
            permissionsNotifier.permissionsGranted.collect {
                if (it) {
                    onPermissionsGranted()
                }
            }
        }
    }

    override var mediaSession: MediaLibraryService.MediaLibrarySession? = null

    private val _isInitialised = MutableStateFlow(false)
    override val isInitialised: StateFlow<Boolean> = _isInitialised

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
        val mediaItemId : String? = rootNode.getChildren().filter { x -> x.mediaItemType == mediaItemType }?.first()?.id
        return if (mediaItemId != null) getChildren(mediaItemId) else emptyList()
    }

    override suspend fun getContentById(id: String): MediaItem {
        return nodeMap[id]?.item ?: MediaItem.EMPTY
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

    private fun onPermissionsGranted() {
        Log.i(logTag(),"onPermissionsGranted invoked")
        Log.i(logTag(), "building tree")
        for (rootChild in rootNode.getChildren()) {
            build(rootChild)
        }
        Log.i(logTag(), "built tree")
       _isInitialised.value = true
    }

    private val cachedSearchResults : MutableMap<String, List<MediaItem>> = HashMap()

    private val itemMap : EnumMap<MediaItemType, HashMap<String, List<MediaItem>>> = EnumMap(MediaItemType::class.java)
    init {
        MediaItemType.values().forEach { itemMap[it] = HashMap() }
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
        fun numberOfChildren() : Int {
            return children.size
        }
    }

    private fun build(node : MediaItemNode) {
        val children = contentRetrievers.getContentRetriever(node.mediaItemType ?: MediaItemType.NONE)?.getChildren(node.id)
        if (children != null) {
            for (child in children) {
                val childNode = MediaItemNode(child)
                nodeMap[childNode.id] = childNode
                node.addChild(childNode)

                if (childNode.mediaItemType != MediaItemType.SONG) {
                    build(childNode)
                }
                mediaSession?.notifyChildrenChanged(childNode.id, childNode.numberOfChildren(), LibraryParams.Builder().build())
            }
        }
        nodeMap[node.id] = node
    }

}