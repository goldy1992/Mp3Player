package com.github.goldy1992.mp3player.client.ui.lists

import android.net.Uri
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.navigation.NavController
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import com.github.goldy1992.mp3player.commons.Screen
import java.util.EnumMap

private const val logTag = "MediaItemSelectedUtils"

fun onSongSelected(mediaControllerAdapter: MediaControllerAdapter) : (MediaItem) -> Unit  {
    return  { mediaItem : MediaItem ->
        mediaControllerAdapter.playFromMediaId(mediaItem)
    }
}

fun onSongSelectedFromList(mediaControllerAdapter: MediaControllerAdapter) : (Int, List<MediaItem>) -> Unit  {
    return  { itemIndex : Int, mediaItemList : List<MediaItem> ->
        mediaControllerAdapter.playFromSongList(itemIndex, mediaItemList)
    }
}

fun onFolderSelected(navController : NavController) : (MediaItem) -> Unit {
    return {
        val folderId = it.mediaId
        val encodedFolderLibraryId = Uri.encode(folderId)
        val directoryPath = MediaItemUtils.getDirectoryPath(it)
        val encodedFolderPath = Uri.encode(directoryPath)
        val folderName = MediaItemUtils.getDirectoryName(it)
        navController.navigate(
            Screen.FOLDER.name
                    + "/" + encodedFolderLibraryId
                    + "/" + folderName
                    + "/" + encodedFolderPath
        )
    }
}

fun onSelectedMap(
    navController: NavController,
    mediaControllerAdapter: MediaControllerAdapter) : EnumMap<MediaItemType, Any> {
    val toReturn : EnumMap<MediaItemType, Any> = EnumMap(MediaItemType::class.java)
    MediaItemType.values().forEach {
        when(it) {
            MediaItemType.FOLDERS,
            MediaItemType.FOLDER -> toReturn[it] = onFolderSelected(navController)
            MediaItemType.SONGS -> toReturn[it] = onSongSelectedFromList(mediaControllerAdapter)
            MediaItemType.SONG -> toReturn[it] = onSongSelected(mediaControllerAdapter)
            else -> toReturn[it] = { Log.i(logTag, "$it selected, do nothing")}
        }
    }
    return toReturn
}