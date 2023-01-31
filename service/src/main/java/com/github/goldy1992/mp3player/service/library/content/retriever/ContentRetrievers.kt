package com.github.goldy1992.mp3player.service.library.content.retriever

import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Inject

@ServiceScoped
class ContentRetrievers

    @Inject
    constructor(mediaItemTypeIds: MediaItemTypeIds,
                private val rootRetriever: RootRetriever,
                private val songsRetriever: SongsRetriever,
                private val foldersRetriever: FoldersRetriever,
                private val songsFromFolderRetriever: SongsFromFolderRetriever,
                private val albumsRetriever: AlbumsRetriever,
                private val songsFromAlbumRetriever: SongsFromAlbumRetriever,
                ) {
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
            if (MediaItemType.NONE.name != key) {
                when (mediaItemTypeInfo) {
                    MediaItemType.SONGS, MediaItemType.SONG -> addToIdToContentRetrieverMap(key, SongsRetriever::class.java)
                    MediaItemType.FOLDER -> addToIdToContentRetrieverMap(key, SongsFromFolderRetriever::class.java)
                    MediaItemType.FOLDERS -> addToIdToContentRetrieverMap(key, FoldersRetriever::class.java)
                    MediaItemType.ALBUMS -> addToIdToContentRetrieverMap(key, AlbumsRetriever::class.java)
                    MediaItemType.ALBUM -> addToIdToContentRetrieverMap(key, SongsFromAlbumRetriever::class.java)
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
            MediaItemType.ALBUMS -> albumsRetriever
            MediaItemType.ALBUM -> songsFromAlbumRetriever
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
        mapToReturn[AlbumsRetriever::class.java] = albumsRetriever
        mapToReturn[SongsFromFolderRetriever::class.java] = songsFromFolderRetriever
        contentRetrieverMap = mapToReturn
        createIdMap(mediaItemTypeIds)
        this.root = rootRetriever
    }
}