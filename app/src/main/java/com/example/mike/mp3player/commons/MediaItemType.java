package com.example.mike.mp3player.commons;

import androidx.annotation.Nullable;

import java.util.Collections;
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

    public static final Map<MediaItemType, EnumSet<MediaItemType>> PARENT_TO_CHILD_MAP;

    public static final Map<MediaItemType, MediaItemType> SINGLETON_PARENT_TO_CHILD_MAP;

    static {
        // TODO: move these maps to somewhere more appropriate
        Map<MediaItemType, EnumSet<MediaItemType>> parentChildMap = new EnumMap<>(MediaItemType.class);
        parentChildMap.put(ROOT, EnumSet.of(SONGS, FOLDERS));
        parentChildMap.put(SONGS, EnumSet.of(SONG));
        parentChildMap.put(SONG, EnumSet.noneOf(MediaItemType.class));
        parentChildMap.put(FOLDERS, EnumSet.of(FOLDER));
        parentChildMap.put(FOLDER, EnumSet.of(SONG));
        PARENT_TO_CHILD_MAP = Collections.unmodifiableMap(parentChildMap);

        Map<MediaItemType, MediaItemType> singletonParentChildMap = new EnumMap<>(MediaItemType.class);
        singletonParentChildMap.put(SONGS, SONG);
        singletonParentChildMap.put(SONG, null);
        singletonParentChildMap.put(FOLDERS, FOLDER);
        singletonParentChildMap.put(FOLDER, SONG);
        SINGLETON_PARENT_TO_CHILD_MAP = Collections.unmodifiableMap(singletonParentChildMap);
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