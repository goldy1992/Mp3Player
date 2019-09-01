package com.example.mike.mp3player.dagger.modules.service;

import com.example.mike.mp3player.commons.MediaItemType;
import com.example.mike.mp3player.service.library.content.builder.FolderItemCreator;
import com.example.mike.mp3player.service.library.content.builder.MediaItemCreator;
import com.example.mike.mp3player.service.library.content.builder.SongItemCreator;

import java.util.EnumMap;
import java.util.Map;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MediaItemBuilderModule {

    @Provides
    @Singleton
    public Map<MediaItemType, MediaItemCreator> providesMediaItemBuilderMap() {
        Map<MediaItemType, MediaItemCreator> map = new EnumMap<>(MediaItemType.class);
        map.put(MediaItemType.SONG, new SongItemCreator());
        map.put(MediaItemType.FOLDER, new FolderItemCreator());
        return map;
    }
}
