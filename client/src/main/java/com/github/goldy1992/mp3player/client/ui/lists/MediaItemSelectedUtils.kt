package com.github.goldy1992.mp3player.client.ui.lists

import android.net.Uri
import android.util.Log
import androidx.navigation.NavController
import com.github.goldy1992.mp3player.client.models.Folder
import com.github.goldy1992.mp3player.client.models.Playlist
import com.github.goldy1992.mp3player.client.models.Song
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.Screen
import java.util.EnumMap

private const val LOG_TAG = "MediaItemSelectedUtils"


fun onFolderSelected(navController : NavController) : (Folder) -> Unit {
    return {
        Log.v(LOG_TAG, "onFolderSelected() invoked.")
        val folderId = it.id
        val encodedFolderLibraryId = Uri.encode(folderId)
        val encodedFolderPath = it.uri
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
    onFolderSelected : (Folder) -> Unit,
    onSongsSelected : (Int, Playlist) -> Unit,
    onSongSelected : (Song) -> Unit

) : EnumMap<MediaItemType, Any> {
    val toReturn : EnumMap<MediaItemType, Any> = EnumMap(MediaItemType::class.java)
    MediaItemType.values().forEach {
        when(it) {
            MediaItemType.FOLDERS,
            MediaItemType.FOLDER -> toReturn[it] = onFolderSelected
            MediaItemType.SONGS -> toReturn[it] = onSongsSelected
            MediaItemType.SONG -> toReturn[it] = onSongSelected
            else -> toReturn[it] = { Log.w(LOG_TAG, "onSelectedMap() $it selected, do nothing")}
        }
    }
    return toReturn
}