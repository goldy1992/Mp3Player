package com.github.goldy1992.mp3player.service.library.content.retriever

import android.os.Bundle
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.MediaMetadata.FOLDER_TYPE_NONE
import com.github.goldy1992.mp3player.commons.ComparatorUtils.Companion.compareRootMediaItemsByMediaItemType
import com.github.goldy1992.mp3player.commons.Constants.MEDIA_ITEM_TYPE
import com.github.goldy1992.mp3player.commons.Constants.ROOT_ITEM_TYPE
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds
import com.github.goldy1992.mp3player.service.library.content.request.ContentRequest
import java.util.*
import javax.inject.Inject

@Suppress("UNUSED_PARAMETER")
class RootRetriever @Inject constructor(private val mediaItemTypeIds: MediaItemTypeIds) : ContentRetriever(), Comparator<MediaItem?> {

    private val CHILDREN: List<MediaItem>
    private val typeToMediaItemMap: MutableMap<MediaItemType, MediaItem>

    override fun getChildren(request: ContentRequest): List<MediaItem> {
        return CHILDREN
    }

    override fun getChildren(parentId: String): List<MediaItem> {
        // TODO: Add check to ensure the correct parent id
        return CHILDREN
    }

    override fun getItems(): List<MediaItem> {
        return CHILDREN
    }

    fun getRootItem(mediaItemType: MediaItemType): MediaItem {
        return typeToMediaItemMap[mediaItemType] ?: MediaItem.EMPTY
    }

    override val type: MediaItemType
        get() = MediaItemType.ROOT

    /**
     * @return a root category item
     */
    private fun createRootItem(category: MediaItemType): MediaItem {
        val extras = Bundle()
        extras.putSerializable(MEDIA_ITEM_TYPE, category)
        extras.putSerializable(ROOT_ITEM_TYPE, category)

        val mediaMetadata = MediaMetadata.Builder()
            .setDescription(category.description)
            .setTitle(category.title)
            .setFolderType(FOLDER_TYPE_NONE)
            .setIsPlayable(false)
            .setExtras(extras)
            .build()
        return MediaItem.Builder()
            .setMediaId(mediaItemTypeIds.getId(category))
            .setMediaMetadata(mediaMetadata)
            .build()
    }

    override fun compare(o1: MediaItem?, o2: MediaItem?): Int {
        return compareRootMediaItemsByMediaItemType.compare(o1, o2)
    }

    init {
        val categorySet = TreeSet(this)
        typeToMediaItemMap = EnumMap(MediaItemType::class.java)
        for (category in MediaItemType.PARENT_TO_CHILD_MAP[MediaItemType.ROOT]!!) {
            val mediaItem = createRootItem(category)
            categorySet.add(mediaItem)
            typeToMediaItemMap[category] = mediaItem
        }
        @Suppress("UNCHECKED_CAST")
        CHILDREN = ArrayList(categorySet) as List<MediaItem>
    }
}