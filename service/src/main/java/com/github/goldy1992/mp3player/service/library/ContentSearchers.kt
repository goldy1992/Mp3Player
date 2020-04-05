package com.github.goldy1992.mp3player.service.library

import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.dagger.scopes.ComponentScope
import com.github.goldy1992.mp3player.service.library.content.searcher.ContentSearcher
import com.github.goldy1992.mp3player.service.library.content.searcher.FolderSearcher
import com.github.goldy1992.mp3player.service.library.content.searcher.SongSearcher
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@ComponentScope
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