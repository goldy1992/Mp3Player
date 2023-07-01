package com.github.goldy1992.mp3player.commons

import java.util.EnumSet

enum class MediaItemType(
        val rank: Int) {

    ROOT(0),
    SONGS(1),
    SONG(2),
    FOLDERS(3),
    FOLDER(4),
    ALBUMS(5),
    ALBUM(6),
    NONE(7);

    // Add playlists

    val value = ordinal

    companion object {
        @JvmField
        var PARENT_TO_CHILD_MAP = mapOf(
                ROOT    to EnumSet.of(SONGS, ALBUMS, FOLDERS),
                ALBUMS  to EnumSet.of(ALBUM),
                ALBUM   to EnumSet.of(SONG),
                SONGS   to EnumSet.of(SONG),
                SONG    to EnumSet.noneOf(MediaItemType::class.java),
                FOLDERS to EnumSet.of(FOLDER),
                FOLDER  to EnumSet.of(SONG))
    }
}