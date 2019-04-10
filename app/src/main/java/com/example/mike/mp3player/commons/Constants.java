package com.example.mike.mp3player.commons;

import android.support.v4.media.session.PlaybackStateCompat;
import android.util.SparseArray;

import com.example.mike.mp3player.commons.library.Category;

public final class Constants {

    public static final String MEDIA_SESSION = "mediaSession";
    public static final String MEDIA_ID = "mediaId";

    public static final String PLAYLIST = "PLAYLiST";
    public static final String PLAY_ALL = "PLAY_ALL";
    public static final String MEDIA_SERVICE_DATA = "MEDIA_SERVICE_DATA";
    public static final int FIRST_ITEM = 0;
    public static final long ONE_SECOND = 1000L;
    public static final String UNKNOWN = "Unknown";
    public static final String INCREASE_PLAYBACK_SPEED = "INCREASE_PLAYBACK_SPEED";
    public static final String DECREASE_PLAYBACK_SPEED = "DECREASE_PLAYBACK_SPEED";
    public static final String REPEAT_MODE = "REPEAT_MODE";
    public static final float DEFAULT_SPEED = 1.0f;
    public static final float DEFAULT_PITCH = 1.0f;
    public static final int DEFAULT_POSITION = 0;

    public static final SparseArray<String> playbackStateDebugMap = new SparseArray<>();
    static {
        playbackStateDebugMap.put(PlaybackStateCompat.STATE_NONE, "STATE_NONE"); // 0
        playbackStateDebugMap.put(PlaybackStateCompat.STATE_STOPPED, "STATE_STOPPED"); // 1
        playbackStateDebugMap.put(PlaybackStateCompat.STATE_PAUSED, "STATE_PAUSED"); // 2
        playbackStateDebugMap.put(PlaybackStateCompat.STATE_PLAYING, "STATE_PLAYING"); // 3
        playbackStateDebugMap.put(PlaybackStateCompat.STATE_FAST_FORWARDING, "STATE_FAST_FORWARDING"); // 4
        playbackStateDebugMap.put(PlaybackStateCompat.STATE_REWINDING, "STATE_REWINDING"); // 5
        playbackStateDebugMap.put(PlaybackStateCompat.STATE_BUFFERING, "STATE_BUFFERING"); // 6
        playbackStateDebugMap.put(PlaybackStateCompat.STATE_ERROR, "STATE_ERROR"); // 7
        playbackStateDebugMap.put(PlaybackStateCompat.STATE_CONNECTING, "STATE_CONNECTING"); // 8
        playbackStateDebugMap.put(PlaybackStateCompat.STATE_SKIPPING_TO_PREVIOUS, "STATE_SKIPPING_TO_PREVIOUS"); // 9
        playbackStateDebugMap.put(PlaybackStateCompat.STATE_SKIPPING_TO_NEXT, "STATE_SKIPPING_TO_NEXT"); // 10
        playbackStateDebugMap.put(PlaybackStateCompat.STATE_SKIPPING_TO_QUEUE_ITEM, "STATE_SKIPPING_TO_QUEUE_ITEM"); // 11
    };

    public static final SparseArray<String> repeatModeDebugMap = new SparseArray<>();
    static {
        playbackStateDebugMap.put(PlaybackStateCompat.REPEAT_MODE_ALL, "REPEAT_MODE_ALL"); // 2
        playbackStateDebugMap.put(PlaybackStateCompat.REPEAT_MODE_NONE, "REPEAT_MODE_NONE"); // 0
        playbackStateDebugMap.put(PlaybackStateCompat.REPEAT_MODE_ONE, "REPEAT_MODE_ONE"); // 1
    };


    /* LIBRARY CONSTANTS */
    public static final String CATEGORY_ROOT_ID = Category.ROOT.name();

    public static final String CATEGORY_SONGS_TITLE = "Songs";
    public static final String CATEGORY_SONGS_ID = Category.SONGS.name();
    public static final String CATEGORY_SONGS_DESCRIPTION = "A list of all songs in the library";

    public static final String CATEGORY_FOLDERS_TITLE = "Folders";
    public static final String CATEGORY_FOLDERS_ID = Category.FOLDERS.name();
    public static final String CATEGORY_FOLDERS_DESCRIPTION = "A list of all folders with music inside them";
    public static final String FOLDER_CHILDREN = "FOLDER_CHILDREN";
    public static final String FOLDER_NAME = "FOLDER_NAME";
    public static final String REQUEST_OBJECT = "REQUEST_OBJECT";
    public static final String RESPONSE_OBJECT = "RESPONSE_OBJECT";
    public static final String PARENT_OBJECT = "PARENT_OBJECT";
    public static final String PARENT_MEDIA_ITEM = "PARENT_MEDIA_ITEM";
    public static final int NO_ACTION = 0;

    // connection extras
    public static final String REJECTION = "REJECTION";
    public static final String PACKAGE_NAME = "com.example.mike.mp3player";
    public static final String ACCEPTED_MEDIA_ROOT_ID = Category.ROOT.name();
    public static final String REJECTED_MEDIA_ROOT_ID = "empty_root_id";

    // connection rejections
    public static final String INVALID_PACKAGE = "INVALID_PACKAGE";
    public static final String EMPTY_LIBRARY = "EMPTY_LIBRARY";

    public static final String EXTRA = "EXTRA";
    public static final int FIRST = 0;
}
