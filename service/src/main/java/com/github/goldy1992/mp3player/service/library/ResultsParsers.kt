package com.github.goldy1992.mp3player.service.library

import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.service.library.content.parser.FolderResultsParser
import com.github.goldy1992.mp3player.service.library.content.parser.ResultsParser
import com.github.goldy1992.mp3player.service.library.content.parser.SongResultsParser
import dagger.hilt.android.scopes.ServiceScoped
import java.util.EnumMap
import javax.inject.Inject

@ServiceScoped
class ResultsParsers @Inject constructor(songResultsParser: SongResultsParser,
                                         folderResultsParser: FolderResultsParser) {
    private val resultsParserEnumMap: EnumMap<MediaItemType, ResultsParser>
    operator fun get(mediaItemType: MediaItemType?): ResultsParser? {
        return resultsParserEnumMap[mediaItemType]
    }

    init {
        val enumMap = EnumMap<MediaItemType, ResultsParser>(MediaItemType::class.java)
        val map: MutableMap<MediaItemType, ResultsParser> = EnumMap(MediaItemType::class.java)
        map[MediaItemType.SONG] = songResultsParser
        map[MediaItemType.FOLDER] = folderResultsParser
        resultsParserEnumMap = enumMap
    }
}