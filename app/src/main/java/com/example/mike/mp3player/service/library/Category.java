package com.example.mike.mp3player.service.library;

import com.example.mike.mp3player.client.activities.FolderActivityInjector;
import com.example.mike.mp3player.client.activities.MediaActivityCompat;
import com.example.mike.mp3player.client.activities.MediaPlayerActivityInjector;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Comparator;

import static com.example.mike.mp3player.service.library.MediaItemType.FOLDER;
import static com.example.mike.mp3player.service.library.MediaItemType.SONG;

public enum Category implements Comparator<Category> {


    SONGS   (RandomStringUtils.randomAlphanumeric(15),
            1,
            "Songs",
            null,
            SONG),

    FOLDERS (RandomStringUtils.randomAlphanumeric(15),
            2,
            "Folders",
            null,
            FOLDER),

    ALBUMS  (RandomStringUtils.randomAlphanumeric(15),
            3,
            "Albums",
            null,
            null),

    ARTISTS (RandomStringUtils.randomAlphanumeric(15),
            4,
            "Artists",
            null,
            null),

    GENRES  (RandomStringUtils.randomAlphanumeric(15),
            5,
            "Genres",
            null,
            null);


    public static boolean isCategory(String s) {
        for (Category c : Category.values()) {
            if (c.name().equals(s)) {
                return true;
            }
        }
        return false;
    }
    private final String id;
    private final Integer rank;
    private final String title;
    private final String description;

    Category(String id, int rank, String title, String description, MediaItemType childType) {
        this.id = id;
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

    public static Category getCategoryById(String id) {
        for (Category c : Category.values()) {
            if (c.id.equals(id)) {
                return c;
            }
        }
        return null;
    }


    public String getId() { return id; }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Integer getRank() {
        return rank;
    }
}