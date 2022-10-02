package com.github.goldy1992.mp3player.commons

import android.net.Uri
import androidx.media3.common.Player
import android.util.SparseArray

object Constants {
    const val FILE_COUNT = "FILE_COUNT"
    const val MEDIA_SESSION = "mediaSession"
    const val NAVIGATION_ROUTE = "NavigationRoute"
    const val LIBRARY_ID = "LIBRARY_ID"
    const val UNKNOWN = "Unknown"
    const val CHANGE_PLAYBACK_SPEED = "CHANGE_PLAYBACK_SPEED"

    val playbackStateDebugMap = SparseArray<String?>()
    val repeatModeDebugMap = SparseArray<String>()

    /* LIBRARY CONSTANTS */
    val ARTWORK_URI_PATH = Uri.parse("content://media/external/audio/albumart")
    const val PACKAGE_NAME = "com.github.goldy1992.mp3player"
    const val MEDIA_ITEM_TYPE = "MEDIA_ITEM_TYPE"
    const val ROOT_ITEM_TYPE = "ROOT_ITEM_TYPE"
    const val ID_SEPARATOR = "|"
    const val EMPTY_MEDIA_ITEM_ID = "-1"

    init {
        playbackStateDebugMap.put(Player.STATE_IDLE, "STATE_IDLE") // 1
        playbackStateDebugMap.put(Player.STATE_BUFFERING, "STATE_BUFFERING") // 2
        playbackStateDebugMap.put(Player.STATE_READY, "STATE_READY") // 3
        playbackStateDebugMap.put(Player.STATE_ENDED, "STATE_ENDED") // 4

        repeatModeDebugMap.put(Player.REPEAT_MODE_OFF, "REPEAT_MODE_OFF") // 0
        repeatModeDebugMap.put(Player.REPEAT_MODE_ONE, "REPEAT_MODE_ONE") // 1
        repeatModeDebugMap.put(Player.REPEAT_MODE_ALL, "REPEAT_MODE_ALL") // 2
    }
}