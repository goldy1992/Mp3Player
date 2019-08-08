package com.example.mike.mp3player.commons;

import java.util.EnumMap;
import java.util.EnumSet;

import static com.example.mike.mp3player.service.library.utils.IdGenerator.generateRootId;

public enum MediaItemType {

    ROOT(generateRootId("root"), 0),
    SONGS(generateRootId("songs"), 1, "Songs", null),
    SONG(generateRootId("song"), 2),
    FOLDERS(generateRootId("folders"), 3, "Folders", null),
    FOLDER(generateRootId("folder"), 4);

    public static final EnumMap<MediaItemType, EnumSet<MediaItemType>> PARENT_TO_CHILD_MAP
            = new EnumMap<>(MediaItemType.class);

    static {
        PARENT_TO_CHILD_MAP.put(ROOT, EnumSet.of(SONGS, FOLDERS));
        PARENT_TO_CHILD_MAP.put(SONGS, EnumSet.of(SONG));
        PARENT_TO_CHILD_MAP.put(SONG, EnumSet.noneOf(MediaItemType.class));
        PARENT_TO_CHILD_MAP.put(FOLDERS, EnumSet.of(FOLDER));
        PARENT_TO_CHILD_MAP.put(FOLDER, EnumSet.of(SONG));
    }

    private final String id;
    private final Integer rank;
    private String title;
    private String description;

    MediaItemType(String id, int rank, String title, String description) {
        this.id = id;
        this.rank = rank;
        this.title = title;
        this.description = description;
    }

    MediaItemType(String id, int rank) {
        this.id = id;
        this.rank = rank;
    }

    public static MediaItemType getMediaItemTypeById(String id) {
        for (MediaItemType mediaItemType : MediaItemType.values()) {
            if (mediaItemType.id.equals(id)) {
                return mediaItemType;
            }
        }
        return null;
    }
}