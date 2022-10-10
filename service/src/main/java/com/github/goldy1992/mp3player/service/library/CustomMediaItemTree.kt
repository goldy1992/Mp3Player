package com.github.goldy1992.mp3player.service.library

import android.util.Log
import androidx.media3.common.MediaItem
import com.github.goldy1992.mp3player.commons.LogTagger
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import com.github.goldy1992.mp3player.service.library.content.ContentRetrievers
import com.google.common.collect.ImmutableList
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Inject

@ServiceScoped
class CustomMediaItemTree
    @Inject
    constructor(val contentRetrievers: ContentRetrievers) : LogTagger {

    class MediaItemNode(item: MediaItem) {
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

    var rootNode : MediaItemNode? = null

    var nodeMap : Map<String, MediaItemNode>? = null

    fun initialise(rootItem: MediaItem) {
        val rootNode = MediaItemNode(rootItem)
        build(rootNode)
        Log.i(logTag(), "built tree")
        val nodeMap : MutableMap<String, MediaItemNode> = HashMap()
        buildMediaNodeMap(rootNode, nodeMap)
        this.nodeMap = nodeMap
        Log.i(logTag(), "MediaItemNode map built")
    }


    private fun buildMediaNodeMap(node : MediaItemNode, nodeMap: MutableMap<String, MediaItemNode>) {
        if (node.hasChildren()) {
            node.getChildren().forEach{buildMediaNodeMap(it, nodeMap)}
        }
        nodeMap[node.id] = node
    }

    fun build(node : MediaItemNode) {
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

    override fun logTag(): String {
        return "CustMdiaItemTree"
    }

}