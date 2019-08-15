package com.example.mike.mp3player.dagger.modules.service;

import com.example.mike.mp3player.commons.MediaItemType;
import com.example.mike.mp3player.service.library.utils.IdGenerator;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MediaItemTypeIdModule {

    @Singleton
    @Provides
    public BiMap<MediaItemType, String> provideIdBiMap() {
        BiMap<MediaItemType, String> map = HashBiMap.create();

        for (MediaItemType mediaItemType : MediaItemType.values()) {
            map.put(mediaItemType, IdGenerator.generateRootId(mediaItemType.name()));
        }
        return map;
    }

    @Singleton
    @Provides
    @Named("all_media_item_types")
    public Map<String, MediaItemType> provideInverseIdMap(BiMap<MediaItemType, String> biMap) {
        Map<String, MediaItemType> map = new HashMap<>();
        map.putAll(biMap.inverse());
        return map;
    }

    @Singleton
    @Provides
    @Named("playable_media_item_types")
    public Map<String, MediaItemType> provideInversePlayableIdMap(BiMap<MediaItemType, String> biMap) {

        Map<String, MediaItemType> mapToReturn = new HashMap<>();
        mapToReturn.put(biMap.get(MediaItemType.SONGS), MediaItemType.SONGS);
        mapToReturn.put(biMap.get(MediaItemType.FOLDERS), MediaItemType.FOLDERS);
        return mapToReturn;
    }

    @Singleton
    @Provides
    public EnumMap<MediaItemType, String> provideIdMap(BiMap<MediaItemType, String> biMap) {
        EnumMap<MediaItemType, String> map = new EnumMap<>(MediaItemType.class);
        map.putAll(biMap);
        return map;
    }

    @Singleton
    @Provides
    public @Named("rootId") String provideRootId(EnumMap<MediaItemType, String> enumMap) {
        return enumMap.get(MediaItemType.ROOT);
    }
}
