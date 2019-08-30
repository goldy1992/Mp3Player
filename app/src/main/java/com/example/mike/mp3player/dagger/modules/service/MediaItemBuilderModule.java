package com.example.mike.mp3player.dagger.modules.service;

import com.example.mike.mp3player.commons.MediaItemType;
import com.example.mike.mp3player.service.library.content.builder.FolderItemBuilder;
import com.example.mike.mp3player.service.library.content.builder.MediaItemBuilder;
import com.example.mike.mp3player.service.library.content.builder.SongItemBuilder;

import java.util.EnumMap;
import java.util.Map;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MediaItemBuilderModule {

    @Provides
    @Singleton
    public Map<MediaItemType, MediaItemBuilder> providesMediaItemBuilderMap() {
        Map<MediaItemType, MediaItemBuilder> map = new EnumMap<>(MediaItemType.class);
        map.put(MediaItemType.SONG, new SongItemBuilder());
        map.put(MediaItemType.FOLDER, new FolderItemBuilder());
        return map;
    }
}
