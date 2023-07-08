package com.github.goldy1992.mp3player.client.ui.lists

import android.util.Log
import androidx.navigation.NavController
import com.github.goldy1992.mp3player.client.models.media.Album
import com.github.goldy1992.mp3player.client.models.media.Albums
import com.github.goldy1992.mp3player.client.models.media.Folder
import com.github.goldy1992.mp3player.client.models.media.Folders
import com.github.goldy1992.mp3player.client.models.media.Playlist
import com.github.goldy1992.mp3player.client.models.media.Song
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.Screen
import java.util.EnumMap

private const val LOG_TAG = "MediaItemSelectedUtils"


fun onFolderSelected(navController : NavController) : (Folder) -> Unit {
    return {
        Log.v(LOG_TAG, "onFolderSelected() invoked.")
        val encodedFolderLibraryId = it.encodedLibraryId
        val encodedFolderPath = it.encodedPath
        val folderName = it.name

        val navRoute = Screen.FOLDER.name +
            "/" + encodedFolderLibraryId +
            "/" + folderName +
            "/" + encodedFolderPath

        Log.d(LOG_TAG, "onFolderSelected() navigating to $navRoute")
        navController.navigate(navRoute)
    }
}

fun buildOnSelectedMap(
    onAlbumSelected : (Album) -> Unit = {_->},
    onAlbumsSelected : (Albums) -> Unit = {_->},
    onFolderSelected : (Folder) -> Unit = {_->},
    onFoldersSelected : (Folders) -> Unit = {_->},
    onSongsSelected : (Int, Playlist) -> Unit = {_,_->},
    onSongSelected : (Song) -> Unit = {_->}

) : EnumMap<MediaItemType, Any> {
    val toReturn : EnumMap<MediaItemType, Any> = EnumMap(MediaItemType::class.java)
    MediaItemType.values().forEach {
        when(it) {
            MediaItemType.ALBUMS -> toReturn[it] = onAlbumsSelected
            MediaItemType.ALBUM -> toReturn[it] = onAlbumSelected
            MediaItemType.FOLDERS -> toReturn[it] = onFoldersSelected
            MediaItemType.FOLDER -> toReturn[it] = onFolderSelected
            MediaItemType.SONGS -> toReturn[it] = onSongsSelected
            MediaItemType.SONG -> toReturn[it] = onSongSelected
            else -> toReturn[it] = { Log.w(LOG_TAG, "onSelectedMap() $it selected, do nothing")}
        }
    }
    return toReturn
}