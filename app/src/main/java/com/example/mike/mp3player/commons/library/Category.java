package com.example.mike.mp3player.commons.library;

import com.example.mike.mp3player.client.activities.FolderActivity;
import com.example.mike.mp3player.client.activities.FolderActivityInjector;
import com.example.mike.mp3player.client.activities.MediaActivityCompat;
import com.example.mike.mp3player.client.activities.MediaPlayerActivity;
import com.example.mike.mp3player.client.activities.MediaPlayerActivityInjector;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.Comparator;

public enum Category implements Comparator<Category> {
    ROOT    (0, "Root", "The root item of the library"),
    SONGS   (1, "Songs", null),
    FOLDERS (2, "Folders", null),
    ALBUMS  (3, "Albums", null),
    ARTISTS (4, "Artists", null),
    GENRES  (5, "Genres", null);

    public static boolean isCategory(String s) {
        for (Category c : Category.values()) {
            if (c.name().equals(s)) {
                return true;
            }
        }
        return false;
    }
    private final Integer rank;
    private final String title;
    private final String description;

    Category(int rank, String title, String description) {
        this.rank = rank;
        this.title = title;
        this.description = description;
    }

    @Override
    public int compare(Category o1, Category o2) {
        return o1.rank.compareTo(o2.rank);
    }

    private static final BiMap<Category, Class<? extends MediaActivityCompat>> CATEGORY_TO_ACTIVITY_MAP = HashBiMap.create();
    private static final BiMap<Class<? extends MediaActivityCompat>, Category> ACTIVITY_TO_CATEGORY_MAP;
    static {
        // TODO: change this code to accommodate test implementations. Intents should be made in Dagger
        CATEGORY_TO_ACTIVITY_MAP.put(Category.SONGS, MediaPlayerActivityInjector.class);
        CATEGORY_TO_ACTIVITY_MAP.put(Category.FOLDERS, FolderActivityInjector.class);
        ACTIVITY_TO_CATEGORY_MAP = CATEGORY_TO_ACTIVITY_MAP.inverse();
    }

    public static Class<? extends MediaActivityCompat> getActivityClassForCategory(Category category) {
        return CATEGORY_TO_ACTIVITY_MAP.get(category);
    }

    public static Category getCategoryForActivityClass(Class<? extends MediaActivityCompat> classKey) {
        return ACTIVITY_TO_CATEGORY_MAP.get(classKey);
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}