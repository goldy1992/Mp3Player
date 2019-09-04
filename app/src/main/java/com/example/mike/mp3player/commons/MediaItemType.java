package com.example.mike.mp3player.commons;

import androidx.annotation.Nullable;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;

import static com.example.mike.mp3player.service.library.utils.IdGenerator.generateRootId;

public enum MediaItemType {

    ROOT(0, "Root", null),
    SONGS( 1, "Songs", null),
    SONG(2, "Song", null),
    FOLDERS( 3, "Folders", null),
    FOLDER(4, "Folder", null);

    public static final Map<MediaItemType, EnumSet<MediaItemType>> PARENT_TO_CHILD_MAP
            = new EnumMap<>(MediaItemType.class);

    public static final Map<MediaItemType, MediaItemType> SINGLETON_PARENT_TO_CHILD_MAP
            = new EnumMap<>(MediaItemType.class);

    static {
        // TODO: move these maps to somewhere more appropriate
        PARENT_TO_CHILD_MAP.put(ROOT, EnumSet.of(SONGS, FOLDERS));
        PARENT_TO_CHILD_MAP.put(SONGS, EnumSet.of(SONG));
        PARENT_TO_CHILD_MAP.put(SONG, EnumSet.noneOf(MediaItemType.class));
        PARENT_TO_CHILD_MAP.put(FOLDERS, EnumSet.of(FOLDER));
        PARENT_TO_CHILD_MAP.put(FOLDER, EnumSet.of(SONG));


        SINGLETON_PARENT_TO_CHILD_MAP.put(SONGS, SONG);
        SINGLETON_PARENT_TO_CHILD_MAP.put(SONG, null);
        SINGLETON_PARENT_TO_CHILD_MAP.put(FOLDERS, FOLDER);
        SINGLETON_PARENT_TO_CHILD_MAP.put(FOLDER, SONG);
    }

    private final Integer rank;
    private final String title;
    private String description;

    MediaItemType(int rank, String title, @Nullable String description) {
        this.rank = rank;
        this.title = title;
        this.description = description;
    }

    public Integer getRank() {
        return rank;
    }

    public int getValue() {
        return this.ordinal();
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}