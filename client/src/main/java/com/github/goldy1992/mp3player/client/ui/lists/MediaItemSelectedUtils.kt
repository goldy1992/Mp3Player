package com.github.goldy1992.mp3player.client.ui.lists

import android.net.Uri
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.navigation.NavController
import com.github.goldy1992.mp3player.client.ui.screens.search.SearchScreenViewModel
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import com.github.goldy1992.mp3player.commons.Screen
import java.util.EnumMap

private const val logTag = "MediaItemSelectedUtils"

fun onSongSelected(viewModel: SearchScreenViewModel) : (MediaItem) -> Unit  {
    return  { mediaItem : MediaItem ->
        viewModel.play(mediaItem)
    }
}

// TODO: abstract this method
fun onSongSelectedFromList(viewModel : SearchScreenViewModel) : (Int, List<MediaItem>) -> Unit  {
    return  { itemIndex : Int, mediaItemList : List<MediaItem> ->
        viewModel.playFromList(itemIndex, mediaItemList)
    }
}

fun onFolderSelected(navController : NavController) : (MediaItem) -> Unit {
    return {
        Log.i("folderSelected", "folder selected")
        val folderId = it.mediaId
        val encodedFolderLibraryId = Uri.encode(folderId)
        val directoryPath = MediaItemUtils.getDirectoryPath(it)
        val encodedFolderPath = Uri.encode(directoryPath)
        val folderName = MediaItemUtils.getDirectoryName(it)

        val navRoute = Screen.FOLDER.name +
            "/" + encodedFolderLibraryId +
            "/" + folderName +
            "/" + encodedFolderPath

        Log.i(logTag, "navigating to $navRoute")
        navController.navigate(navRoute)
    }
}

fun onSelectedMap(
    navController: NavController,
    viewModel: SearchScreenViewModel) : EnumMap<MediaItemType, Any> {
    val toReturn : EnumMap<MediaItemType, Any> = EnumMap(MediaItemType::class.java)
    MediaItemType.values().forEach {
        when(it) {
            MediaItemType.FOLDERS,
            MediaItemType.FOLDER -> toReturn[it] = onFolderSelected(navController)
            MediaItemType.SONGS -> toReturn[it] = onSongSelectedFromList(viewModel)
            MediaItemType.SONG -> toReturn[it] = onSongSelected(viewModel)
            else -> toReturn[it] = { Log.i(logTag, "$it selected, do nothing")}
        }
    }
    return toReturn
}

fun buildOnSelectedMap(
    onFolderSelected : (MediaItem) -> Unit,
    onSongsSelected : (Int, List<MediaItem>) -> Unit,
    onSongSelected : (MediaItem) -> Unit

) : EnumMap<MediaItemType, Any> {
    val toReturn : EnumMap<MediaItemType, Any> = EnumMap(MediaItemType::class.java)
    MediaItemType.values().forEach {
        when(it) {
            MediaItemType.FOLDERS,
            MediaItemType.FOLDER -> toReturn[it] = onFolderSelected
            MediaItemType.SONGS -> toReturn[it] = onSongsSelected
            MediaItemType.SONG -> toReturn[it] = onSongSelected
            else -> toReturn[it] = { Log.i(logTag, "$it selected, do nothing")}
        }
    }
    return toReturn
}