package com.example.mike.mp3player.dagger.modules.service;

import android.content.Context;

import com.example.mike.mp3player.commons.MediaItemType;
import com.example.mike.mp3player.service.library.content.parser.FolderResultsParser;
import com.example.mike.mp3player.service.library.content.parser.ResultsParser;
import com.example.mike.mp3player.service.library.content.parser.SongResultsParser;

import java.util.EnumMap;
import java.util.Map;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MediaItemBuilderModule {

    @Provides
    @Singleton
    public Map<MediaItemType, ResultsParser> providesMediaItemBuilderMap(Context context) {
        Map<MediaItemType, ResultsParser> map = new EnumMap<>(MediaItemType.class);
        map.put(MediaItemType.SONG, new SongResultsParser(context));
        map.put(MediaItemType.FOLDER, new FolderResultsParser(context));
        return map;
    }
}
