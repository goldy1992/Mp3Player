package com.example.mike.mp3player.client.activities;

import com.example.mike.mp3player.commons.MediaItemType;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public final class MediaItemTypeToActivityMap {

    private static final BiMap<MediaItemType, Class<? extends MediaActivityCompat>> CATEGORY_TO_ACTIVITY_MAP = HashBiMap.create();
    private static final BiMap<Class<? extends MediaActivityCompat>, MediaItemType> ACTIVITY_TO_CATEGORY_MAP;
    static {
        // TODO: change this code to accommodate test implementations. Intents should be made in Dagger
        CATEGORY_TO_ACTIVITY_MAP.put(MediaItemType.SONGS, MediaPlayerActivityInjector.class);
        CATEGORY_TO_ACTIVITY_MAP.put(MediaItemType.FOLDERS, FolderActivityInjector.class);
        ACTIVITY_TO_CATEGORY_MAP = CATEGORY_TO_ACTIVITY_MAP.inverse();
    }

    public static Class<? extends MediaActivityCompat> getActivityClassForCategory(MediaItemType category) {
        return CATEGORY_TO_ACTIVITY_MAP.get(category);
    }

    public static MediaItemType getCategoryForActivityClass(Class<? extends MediaActivityCompat> classKey) {
        return ACTIVITY_TO_CATEGORY_MAP.get(classKey);
    }
}
