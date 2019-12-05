package com.github.goldy1992.mp3player.service.library;

import com.github.goldy1992.mp3player.commons.MediaItemType;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class MediaItemTypeIds {

    private Map<String, MediaItemType> idToMediaItemTypeMap;

    private EnumMap<MediaItemType, String> mediaItemTypeToIdMap;

    @Inject
    public MediaItemTypeIds() {
        create();
    }

    /**
     * creates the maps
     */
    private void create() {
        BiMap<MediaItemType, String> biMap = HashBiMap.create();

        // ensure unique ids
        HashSet<String> idSet = new HashSet<>();

        for (MediaItemType mediaItemType : MediaItemType.values()) {
            boolean added = false;
            String id = null;
            while (!added) {
                id = generateRootId(mediaItemType.name());
                added = idSet.add(id);
            }
            biMap.put(mediaItemType, id);
        }

        this.idToMediaItemTypeMap = new HashMap<>(biMap.inverse());

        EnumMap<MediaItemType, String> enumMap = new EnumMap<>(MediaItemType.class);
        enumMap.putAll(biMap);
        this.mediaItemTypeToIdMap = enumMap;
    }

    public String getId(MediaItemType mediaItemType) {
        return mediaItemTypeToIdMap.get(mediaItemType);
    }

    public MediaItemType getMediaItemType(String id) {
        return idToMediaItemTypeMap.get(id);
    }


    public static final String generateRootId(String prefix) {
        return prefix + RandomStringUtils.randomAlphanumeric(15);
    }
}
