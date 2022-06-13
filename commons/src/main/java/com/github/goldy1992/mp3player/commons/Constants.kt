package com.github.goldy1992.mp3player.commons

import android.net.Uri
import android.support.v4.media.session.PlaybackStateCompat
import android.util.SparseArray

object Constants {
    const val FILE_COUNT = "FILE_COUNT"
    const val MEDIA_SESSION = "mediaSession"
    const val NAVIGATION_ROUTE = "NavigationRoute"
    const val LIBRARY_ID = "LIBRARY_ID"
    const val UNKNOWN = "Unknown"
    const val CHANGE_PLAYBACK_SPEED = "CHANGE_PLAYBACK_SPEED"
    const val AUDIO_SESSION_ID = "AUDIO_SESSION_ID"
    const val AUDIO_DATA = "AUDIO_DATA"

    val playbackStateDebugMap = SparseArray<String?>()
    val repeatModeDebugMap = SparseArray<String>()
    /* LIBRARY CONSTANTS */
    val ARTWORK_URI_PATH = Uri.parse("content://media/external/audio/albumart")
    const val PACKAGE_NAME = "com.github.goldy1992.mp3player"
    const val MEDIA_ITEM_TYPE = "MEDIA_ITEM_TYPE"
    const val ROOT_ITEM_TYPE = "ROOT_ITEM_TYPE"
    const val ID_SEPARATOR = "|"
    const val EMPTY_MEDIA_ITEM_ID = "-1"

    val SUB_BASS = IntRange(20, 60)
    val BASS = IntRange(61, 250)
    val LOW_MIDRANGE = IntRange(251, 500)
    val MIDRANGE = IntRange(501, 2000)
    val UPPER_MIDRANGE = IntRange(2001, 4000)

    val frequencyBands = listOf(SUB_BASS, BASS, LOW_MIDRANGE, MIDRANGE, UPPER_MIDRANGE)


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