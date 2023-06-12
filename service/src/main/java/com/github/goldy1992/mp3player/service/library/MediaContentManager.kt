package com.github.goldy1992.mp3player.service.library

import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaLibraryService.LibraryParams
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import com.github.goldy1992.mp3player.commons.Normaliser.normalise
import com.github.goldy1992.mp3player.commons.data.repositories.permissions.IPermissionsRepository
import com.github.goldy1992.mp3player.service.RootAuthenticator
import com.github.goldy1992.mp3player.service.library.content.ContentManagerResult
import com.github.goldy1992.mp3player.service.library.content.retriever.ContentRetrievers
import com.github.goldy1992.mp3player.service.library.content.retriever.RootRetriever
import com.github.goldy1992.mp3player.service.library.content.searcher.ContentSearchers
import com.google.common.collect.ImmutableList
import dagger.hilt.android.scopes.ServiceScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.apache.commons.collections4.CollectionUtils
import java.util.*
import javax.inject.Inject

@ServiceScoped
class MediaContentManager @Inject constructor(private val permissionRepository: IPermissionsRepository,
                                              private val contentRetrievers: ContentRetrievers,
                                              private val contentSearchers: ContentSearchers,
                                              rootAuthenticator: RootAuthenticator,
                                              scope : CoroutineScope,
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
            permissionRepository.permissionsFlow().collect {
                Log.v(logTag(), "  permissionRepository.permissionsFlow().collect invoked")
                if (permissionRepository.hasStorageReadPermissions()) {
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
     * @param parentId the id of the children to get
     * @return all the children of the id specified by the parentId parameter
     */
    override suspend fun getChildren(parentId: String): ContentManagerResult {
        Log.v(logTag(), "getChildren() invoked with parentId $parentId")
        val parentNode: MediaItemNode? = nodeMap[parentId]
        val children = parentNode?.getChildren()?.map(MediaItemNode::item) ?: emptyList()

        val toReturn = ContentManagerResult( children,
            children.size,
            permissionRepository.hasStorageReadPermissions())
        Log.d(logTag(), "getChildren() parentId: ${parentId}, children count: ${toReturn.children.size}, hasPermissions: ${toReturn.hasPermissions}")
        return toReturn
    }

    override suspend fun getChildren(mediaItemType: MediaItemType): ContentManagerResult{
        val mediaItemId : String = rootNode.getChildren()
            .first { x -> x.mediaItemType == mediaItemType }.id
        return if (mediaItemId != null) getChildren(mediaItemId) else buildEmptyResult()
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
        Log.v(logTag(),"onPermissionsGranted() invoked")
        if (!_isInitialised.value) {
            Log.d(logTag(), "onPermissionsGranted() Not yet initialised, building tree")
            for (rootChild in rootNode.getChildren()) {
                Log.v(logTag(), "onPermissionsGranted() building node: ${rootChild.mediaItemType}")
                build(rootChild)
            }
            Log.v(logTag(), "onPermissionsGranted() built tree")
            _isInitialised.value = true
        }
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

        fun getChildren(): List<MediaItemNode> {
            return ImmutableList.copyOf(children)
        }
        fun numberOfChildren() : Int {
            return children.size
        }
    }

    private fun build(node : MediaItemNode) {
        val children = contentRetrievers.getContentRetriever(node.mediaItemType)?.getChildren(node.id)
        if (children != null) {
            for (child in children) {
                val childNode = MediaItemNode(child)
                nodeMap[childNode.id] = childNode
                node.addChild(childNode)

                if (childNode.mediaItemType != MediaItemType.SONG) {
                    build(childNode)
                }
            }
            Log.d(logTag(), "build() calling notifyChildrenChange(parentId:${node.id}, itemCount: ${node.numberOfChildren()}")
            mediaSession?.notifyChildrenChanged(node.id, node.numberOfChildren(), LibraryParams.Builder().build())
        }
        nodeMap[node.id] = node
    }

    private fun buildEmptyResult() : ContentManagerResult {
        return ContentManagerResult(children = emptyList(), numberOfResults = 0, hasPermissions = permissionRepository.hasStorageReadPermissions())
    }

}