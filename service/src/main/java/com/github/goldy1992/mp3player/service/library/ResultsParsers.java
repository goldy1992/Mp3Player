package com.github.goldy1992.mp3player.service.library;

import com.github.goldy1992.mp3player.commons.MediaItemType;
import com.github.goldy1992.mp3player.service.library.content.parser.FolderResultsParser;
import com.github.goldy1992.mp3player.service.library.content.parser.ResultsParser;
import com.github.goldy1992.mp3player.service.library.content.parser.SongResultsParser;

import java.util.EnumMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ResultsParsers {

    private EnumMap<MediaItemType, ResultsParser> resultsParserEnumMap;

    @Inject
    public ResultsParsers(SongResultsParser songResultsParser,
                          FolderResultsParser folderResultsParser) {
        EnumMap<MediaItemType, ResultsParser> enumMap = new EnumMap<>(MediaItemType.class);
        Map<MediaItemType, ResultsParser> map = new EnumMap<>(MediaItemType.class);
        map.put(MediaItemType.SONG, songResultsParser);
        map.put(MediaItemType.FOLDER, folderResultsParser);
        this.resultsParserEnumMap = enumMap;
    }

    public ResultsParser get(MediaItemType mediaItemType) {
        return this.resultsParserEnumMap.get(mediaItemType);
    }
}
