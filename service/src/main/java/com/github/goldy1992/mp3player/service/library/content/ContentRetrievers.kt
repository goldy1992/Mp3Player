package com.github.goldy1992.mp3player.service.library.content

import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds
import com.github.goldy1992.mp3player.service.library.content.retriever.*
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Inject

@ServiceScoped
class ContentRetrievers

    @Inject
    constructor(mediaItemTypeIds: MediaItemTypeIds,
                private val rootRetriever: RootRetriever,
                private val songsRetriever: SongsRetriever,
                private val foldersRetriever: FoldersRetriever,
                private val songsFromFolderRetriever: SongsFromFolderRetriever) {
    /**  */
    var contentRetrieverMap: Map<Class<out ContentRetriever>, ContentRetriever>
    /**  */
    var idToContentRetrieverMap: MutableMap<String, ContentRetriever>? = null
    val root: RootRetriever
    operator fun get(id: String?): ContentRetriever? {
        return idToContentRetrieverMap!![id]
    }

    operator fun get(clazz: Class<out ContentRetriever?>?): ContentRetriever? {
        return contentRetrieverMap[clazz]
    }

    private fun createIdMap(mediaItemTypeIds: MediaItemTypeIds) {
        idToContentRetrieverMap = HashMap()
        for (mediaItemTypeInfo in MediaItemType.values()) {
            val key = mediaItemTypeIds.getId(mediaItemTypeInfo)
            if (null != key) {
                when (mediaItemTypeInfo) {
                    MediaItemType.SONGS, MediaItemType.SONG -> addToIdToContentRetrieverMap(key, SongsRetriever::class.java)
                    MediaItemType.FOLDER -> addToIdToContentRetrieverMap(key, SongsFromFolderRetriever::class.java)
                    MediaItemType.FOLDERS -> addToIdToContentRetrieverMap(key, FoldersRetriever::class.java)
                    MediaItemType.ROOT -> addToIdToContentRetrieverMap(key, RootRetriever::class.java)
                    else -> {
                    }
                }
            }
        }
    }

    fun getContentRetriever(mediaItemType: MediaItemType) : ContentRetriever? {
        return when (mediaItemType) {
            MediaItemType.ROOT -> root
            MediaItemType.SONGS -> songsRetriever
            MediaItemType.FOLDER -> songsFromFolderRetriever
            MediaItemType.FOLDERS -> foldersRetriever
            else -> null

        }
    }

    private fun addToIdToContentRetrieverMap(key: String, clazz: Class<out ContentRetriever>) {
        val contentRetriever = contentRetrieverMap[clazz]
        if (null != contentRetriever) {
            idToContentRetrieverMap!![key] = contentRetriever
        }
    }

    init {
        val mapToReturn: MutableMap<Class<out ContentRetriever>, ContentRetriever> = HashMap()
        mapToReturn[RootRetriever::class.java] = rootRetriever
        mapToReturn[SongsRetriever::class.java] = songsRetriever
        mapToReturn[FoldersRetriever::class.java] = foldersRetriever
        mapToReturn[SongsFromFolderRetriever::class.java] = songsFromFolderRetriever
        contentRetrieverMap = mapToReturn
        createIdMap(mediaItemTypeIds)
        this.root = rootRetriever
    }
}