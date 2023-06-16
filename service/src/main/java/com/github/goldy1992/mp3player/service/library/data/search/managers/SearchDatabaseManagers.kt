package com.github.goldy1992.mp3player.service.library.data.search.managers

import com.github.goldy1992.mp3player.service.library.data.search.Album
import com.github.goldy1992.mp3player.service.library.data.search.Folder
import com.github.goldy1992.mp3player.service.library.data.search.Song

interface SearchDatabaseManagers {

    fun getSongDatabaseManager() : SearchDatabaseManager<Song>

    fun getFolderDatabaseManager() : SearchDatabaseManager<Folder>

    fun getAlbumDatabaseManager() : SearchDatabaseManager<Album>
}