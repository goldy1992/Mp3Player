package com.github.goldy1992.mp3player.service.library

import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.service.library.content.searcher.ContentSearcher
import com.github.goldy1992.mp3player.service.library.content.searcher.FolderSearcher
import com.github.goldy1992.mp3player.service.library.content.searcher.SongSearcher
import dagger.hilt.android.scopes.ServiceScoped
import java.util.*
import javax.inject.Inject

@ServiceScoped
class ContentSearchers

    @Inject
    constructor(songSearcher: SongSearcher,
               folderSearcher: FolderSearcher) {
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
        contentSearcherMap = map
    }
}