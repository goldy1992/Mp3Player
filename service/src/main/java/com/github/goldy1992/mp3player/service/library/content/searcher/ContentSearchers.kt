package com.github.goldy1992.mp3player.service.library.content.searcher

import com.github.goldy1992.mp3player.commons.MediaItemType
import dagger.hilt.android.scopes.ServiceScoped
import java.util.EnumMap
import javax.inject.Inject

@ServiceScoped
class ContentSearchers

    @Inject
    constructor(songSearcher: SongSearcher,
               folderSearcher: FolderSearcher,
                albumSearcher: AlbumSearcher) {
    /**  */
    var contentSearcherMap: EnumMap<MediaItemType, ContentSearcher>

    operator fun get(mediaItemType: MediaItemType?): ContentSearcher? {
        return contentSearcherMap[mediaItemType]
    }

    val all: Collection<ContentSearcher>
        get() = contentSearcherMap.values

    init {
        val map = EnumMap<MediaItemType, ContentSearcher>(MediaItemType::class.java)
        map[MediaItemType.SONG] = songSearcher
        map[MediaItemType.FOLDER] = folderSearcher
        map[MediaItemType.ALBUMS] = albumSearcher
        contentSearcherMap = map
    }
}