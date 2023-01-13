package com.github.goldy1992.mp3player.commons

import java.util.*

enum class MediaItemType(
        val rank: Int,
        val title: String,
        val description: String?) {

    ROOT(0, "Root", null),
    SONGS(1, "Songs", null),
    SONG(2, "Song", null),
    FOLDERS(3, "Folders", null),
    FOLDER(4, "Folder", null),
    ALBUMS(5, "Albums", null),
    ALBUM(6, "Album", null),
    NONE(7, "None", null);

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
        @JvmField
        var SINGLETON_PARENT_TO_CHILD_MAP = mapOf(
                SONGS to SONG,
                SONG to null,
                FOLDERS to FOLDER,
                FOLDER to SONG
        )
    }
}