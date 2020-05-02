package com.github.goldy1992.mp3player.service.library

import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.dagger.scopes.ComponentScope
import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import org.apache.commons.lang3.RandomStringUtils
import java.util.*
import javax.inject.Inject

@ComponentScope
class MediaItemTypeIds @Inject constructor() {
    private var idToMediaItemTypeMap: Map<String, MediaItemType>? = null
    private var mediaItemTypeToIdMap: EnumMap<MediaItemType, String>? = null
    /**
     * creates the maps
     */
    private fun create() {
        val biMap: BiMap<MediaItemType, String> = HashBiMap.create()
        // ensure unique ids
        val idSet = HashSet<String?>()
        for (mediaItemType in MediaItemType.values()) {
            var added = false
            var id: String? = null
            while (!added) {
                id = generateRootId(mediaItemType.name)
                added = idSet.add(id)
            }
            biMap[mediaItemType] = id
        }
        idToMediaItemTypeMap = HashMap(biMap.inverse())
        val enumMap = EnumMap<MediaItemType, String>(MediaItemType::class.java)
        enumMap.putAll(biMap)
        mediaItemTypeToIdMap = enumMap
    }

    fun getId(mediaItemType: MediaItemType?): String? {
        return mediaItemTypeToIdMap!![mediaItemType]
    }

    fun getMediaItemType(id: String?): MediaItemType? {
        return idToMediaItemTypeMap!![id]
    }

    companion object {
        fun generateRootId(prefix: String): String {
            return prefix + RandomStringUtils.randomAlphanumeric(15)
        }
    }

    init {
        create()
    }
}