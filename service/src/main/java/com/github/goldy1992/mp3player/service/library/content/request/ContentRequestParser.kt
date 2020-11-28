package com.github.goldy1992.mp3player.service.library.content.request

import com.github.goldy1992.mp3player.commons.Constants.ID_SEPARATOR
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds
import javax.inject.Inject

class ContentRequestParser @Inject constructor(private val mediaItemTypeIds: MediaItemTypeIds) {
    fun parse(id: String): ContentRequest? {
        val splitId = id.split(ID_SEPARATOR)
        val mediaIdPrefix = calculateMediaIdPrefix(splitId)
        val splitIdSize = splitId.size
        if (splitIdSize == 1) {
            return ContentRequest(splitId[0], splitId[0], mediaIdPrefix)
        } else if (splitIdSize >= 2) {
            return ContentRequest(splitId[1], splitId[0], mediaIdPrefix)
        }
        return null
    }

    private fun calculateMediaIdPrefix(splitId: List<String>): String? {
        val splitIdSize = splitId.size
        if (splitIdSize == 1) {
            val mediaItemType = getMediaItemTypeFromId(splitId[0])
            if (mediaItemType != null) {
                val typeToReturn = MediaItemType.SINGLETON_PARENT_TO_CHILD_MAP[mediaItemType]
                if (null != typeToReturn) {
                    return mediaItemTypeIds.getId(typeToReturn)
                }
            }
        } else if (splitIdSize >= 2) {
            return splitId[0] + ID_SEPARATOR + splitId[1]
        }
        return null
    }

    private fun getMediaItemTypeFromId(id: String): MediaItemType? {
        return mediaItemTypeIds.getMediaItemType(id)
    }

}