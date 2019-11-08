package com.github.goldy1992.mp3player.commons;

import android.net.Uri;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.SparseArray;

import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector;

import static android.support.v4.media.session.PlaybackStateCompat.ACTION_PAUSE;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_PLAY;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_SEEK_TO;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_SET_REPEAT_MODE;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_SET_SHUFFLE_MODE;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_STOP;

public final class Constants {

    // ensure that an instance of Constants cannot be instantiated
    private Constants() { }
    public static final String MEDIA_SESSION = "mediaSession";
    public static final String MEDIA_ID = "mediaId";
    public static final String MEDIA_ITEM = "MEDIA_ITEM";
    public static final String LIBRARY_ID = "LIBRARY_ID";

    public static final String PLAYLIST = "PLAYLiST";
    public static final String PLAY_ALL = "PLAY_ALL";
    public static final String MEDIA_SERVICE_DATA = "MEDIA_SERVICE_DATA";
    public static final int FIRST_ITEM = 0;
    public static final long ONE_SECOND = 1000L;
    public static final String UNKNOWN = "Unknown";
    public static final String INCREASE_PLAYBACK_SPEED = "INCREASE_PLAYBACK_SPEED";
    public static final String DECREASE_PLAYBACK_SPEED = "DECREASE_PLAYBACK_SPEED";
    public static final String REPEAT_MODE = "REPEAT_MODE";
    public static final String SHUFFLE_MODE = "SHUFFLE_MODE";
    public static final float DEFAULT_SPEED = 1.0f;
    public static final float DEFAULT_PITCH = 1.0f;
    public static final int DEFAULT_POSITION = 0;
    public static final String THEME = "THEME";

    public static final long ACTION_PLAYBACK_SPEED_CHANGED = 1 << 22;
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
    }

    public static final SparseArray<String> repeatModeDebugMap = new SparseArray<>();
    static {
        repeatModeDebugMap.put(PlaybackStateCompat.REPEAT_MODE_ALL, "REPEAT_MODE_ALL"); // 2
        repeatModeDebugMap.put(PlaybackStateCompat.REPEAT_MODE_NONE, "REPEAT_MODE_NONE"); // 0
        repeatModeDebugMap.put(PlaybackStateCompat.REPEAT_MODE_ONE, "REPEAT_MODE_ONE"); // 1
    }

    /* LIBRARY CONSTANTS */
    public static final Uri ARTWORK_URI_PATH = Uri.parse("content://media/external/audio/albumart");

    public static final String CATEGORY_SONGS_DESCRIPTION = "A list of all songs in the library";
    public static final String CATEGORY_FOLDERS_DESCRIPTION = "A list of all folders with music inside them";

    public static final String REQUEST_OBJECT = "REQUEST_OBJECT";
    public static final String RESPONSE_OBJECT = "RESPONSE_OBJECT";
    public static final String PARENT_ID = "PARENT_ID";
    public static final String PARENT_MEDIA_ITEM = "PARENT_MEDIA_ITEM";
    public static final int NO_ACTION = 0;

    // connection extras
    public static final String REJECTION = "REJECTION";
    public static final String PACKAGE_NAME = "com.github.goldy1992.mp3player";

    public static final String MEDIA_LIBRARY_INFO = "MEDIA_LIBRARY_INFO";
    public static final String MEDIA_ITEM_TYPE = "MEDIA_ITEM_TYPE";
    public static final String MEDIA_ITEM_TYPE_ID = "MEDIA_ITEM_TYPE_ID";
    public static final String PARENT_MEDIA_ITEM_TYPE = "PARENT_MEDIA_ITEM_TYPE";
    public static final String PARENT_MEDIA_ITEM_TYPE_ID = "PARENT_MEDIA_ITEM_TYPE_ID";



    public static final String EXTRA = "EXTRA";
    public static final int FIRST = 0;

    public static final int OPAQUE = 255;
    public static final int TRANSLUCENT = 100;

    public static final String ROOT_ITEM_TYPE = "ROOT_ITEM_TYPE";
    public static final String ID_SEPARATOR = "|";
    public static final String ID_DELIMITER = "\\|";


    @MediaSessionConnector.PlaybackActions
    public static final long SUPPORTED_PLAYBACK_ACTIONS = ACTION_STOP | ACTION_PAUSE | ACTION_PLAY |
            ACTION_SET_REPEAT_MODE | ACTION_SET_SHUFFLE_MODE | ACTION_SEEK_TO;

}
