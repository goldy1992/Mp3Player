package com.github.goldy1992.mp3player.commons

import android.net.Uri
import android.support.v4.media.session.PlaybackStateCompat
import android.util.SparseArray

object Constants {
    const val MEDIA_SESSION = "mediaSession"
    const val MEDIA_ID = "mediaId"
    const val MEDIA_ITEM = "MEDIA_ITEM"
    const val LIBRARY_ID = "LIBRARY_ID"
    const val PLAYLIST = "PLAYLiST"
    const val PLAY_ALL = "PLAY_ALL"
    const val MEDIA_SERVICE_DATA = "MEDIA_SERVICE_DATA"
    const val FIRST_ITEM = 0
    const val ONE_SECOND = 1000L
    const val UNKNOWN = "Unknown"
    const val INCREASE_PLAYBACK_SPEED = "INCREASE_PLAYBACK_SPEED"
    const val DECREASE_PLAYBACK_SPEED = "DECREASE_PLAYBACK_SPEED"
    const val REPEAT_MODE = "REPEAT_MODE"
    const val SHUFFLE_MODE = "SHUFFLE_MODE"
    const val DEFAULT_SPEED = 1.0f
    const val DEFAULT_PITCH = 1.0f
    const val DEFAULT_POSITION = 0
    const val THEME = "THEME"
    const val ACTION_PLAYBACK_SPEED_CHANGED = (1 shl 22.toLong().toInt()).toLong()
    val playbackStateDebugMap = SparseArray<String?>()
    val repeatModeDebugMap = SparseArray<String>()
    /* LIBRARY CONSTANTS */
    val ARTWORK_URI_PATH = Uri.parse("content://media/external/audio/albumart")
    const val CATEGORY_SONGS_DESCRIPTION = "A list of all songs in the library"
    const val CATEGORY_FOLDERS_DESCRIPTION = "A list of all folders with music inside them"
    const val REQUEST_OBJECT = "REQUEST_OBJECT"
    const val RESPONSE_OBJECT = "RESPONSE_OBJECT"
    const val PARENT_ID = "PARENT_ID"
    const val PARENT_MEDIA_ITEM = "PARENT_MEDIA_ITEM"
    const val NO_ACTION = 0
    // connection extras
    const val REJECTION = "REJECTION"
    const val PACKAGE_NAME = "com.github.goldy1992.mp3player"
    const val MEDIA_LIBRARY_INFO = "MEDIA_LIBRARY_INFO"
    const val MEDIA_ITEM_TYPE = "MEDIA_ITEM_TYPE"
    const val MEDIA_ITEM_TYPE_ID = "MEDIA_ITEM_TYPE_ID"
    const val PARENT_MEDIA_ITEM_TYPE = "PARENT_MEDIA_ITEM_TYPE"
    const val PARENT_MEDIA_ITEM_TYPE_ID = "PARENT_MEDIA_ITEM_TYPE_ID"
    const val EXTRA = "EXTRA"
    const val FIRST = 0
    const val OPAQUE = 255
    const val TRANSLUCENT = 100
    const val ROOT_ITEM_TYPE = "ROOT_ITEM_TYPE"
    const val ID_SEPARATOR = "|"
    const val ID_DELIMITER = "\\|"

    init {
        playbackStateDebugMap.put(PlaybackStateCompat.STATE_NONE, "STATE_NONE") // 0
        playbackStateDebugMap.put(PlaybackStateCompat.STATE_STOPPED, "STATE_STOPPED") // 1
        playbackStateDebugMap.put(PlaybackStateCompat.STATE_PAUSED, "STATE_PAUSED") // 2
        playbackStateDebugMap.put(PlaybackStateCompat.STATE_PLAYING, "STATE_PLAYING") // 3
        playbackStateDebugMap.put(PlaybackStateCompat.STATE_FAST_FORWARDING, "STATE_FAST_FORWARDING") // 4
        playbackStateDebugMap.put(PlaybackStateCompat.STATE_REWINDING, "STATE_REWINDING") // 5
        playbackStateDebugMap.put(PlaybackStateCompat.STATE_BUFFERING, "STATE_BUFFERING") // 6
        playbackStateDebugMap.put(PlaybackStateCompat.STATE_ERROR, "STATE_ERROR") // 7
        playbackStateDebugMap.put(PlaybackStateCompat.STATE_CONNECTING, "STATE_CONNECTING") // 8
        playbackStateDebugMap.put(PlaybackStateCompat.STATE_SKIPPING_TO_PREVIOUS, "STATE_SKIPPING_TO_PREVIOUS") // 9
        playbackStateDebugMap.put(PlaybackStateCompat.STATE_SKIPPING_TO_NEXT, "STATE_SKIPPING_TO_NEXT") // 10
        playbackStateDebugMap.put(PlaybackStateCompat.STATE_SKIPPING_TO_QUEUE_ITEM, "STATE_SKIPPING_TO_QUEUE_ITEM") // 11
    }

    init {
        repeatModeDebugMap.put(PlaybackStateCompat.REPEAT_MODE_ALL, "REPEAT_MODE_ALL") // 2
        repeatModeDebugMap.put(PlaybackStateCompat.REPEAT_MODE_NONE, "REPEAT_MODE_NONE") // 0
        repeatModeDebugMap.put(PlaybackStateCompat.REPEAT_MODE_ONE, "REPEAT_MODE_ONE") // 1
    }
}