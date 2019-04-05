package com.example.mike.mp3player.commons.library;

import com.example.mike.mp3player.client.activities.FolderActivity;
import com.example.mike.mp3player.client.activities.MediaActivityCompat;
import com.example.mike.mp3player.client.activities.MediaPlayerActivity;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.Comparator;

public enum Category implements Comparator<Category> {
    ROOT    (0),
    SONGS   (1),
    FOLDERS (2),
    ALBUMS  (3),
    ARTISTS (4),
    GENRES  (5);

    public static boolean isCategory(String s) {
        for (Category c : Category.values()) {
            if (c.name().equals(s)) {
                return true;
            }
        }
        return false;
    }
    private final Integer rank;

    Category(int rank) {
        this.rank = rank;
    }

    @Override
    public int compare(Category o1, Category o2) {
        return o1.rank.compareTo(o2.rank);
    }

    public static final BiMap<Category, Class<? extends MediaActivityCompat>> CATEGORY_TO_ACTIVITY_MAP = HashBiMap.create();

    static {
        CATEGORY_TO_ACTIVITY_MAP.put(Category.SONGS, MediaPlayerActivity.class);
        CATEGORY_TO_ACTIVITY_MAP.put(Category.FOLDERS, FolderActivity.class);
    }
   // public static final Map<>
}

