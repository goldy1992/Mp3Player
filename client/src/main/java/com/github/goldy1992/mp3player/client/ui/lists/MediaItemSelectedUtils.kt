package com.github.goldy1992.mp3player.client.ui.lists

import android.net.Uri
import android.util.Log
import androidx.navigation.NavController
import com.github.goldy1992.mp3player.client.data.Folder
import com.github.goldy1992.mp3player.client.data.Song
import com.github.goldy1992.mp3player.client.data.Songs
import com.github.goldy1992.mp3player.client.ui.screens.search.SearchScreenViewModel
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.Screen
import java.util.EnumMap

private const val logTag = "MediaItemSelectedUtils"

fun onSongSelected(viewModel: SearchScreenViewModel) : (Song) -> Unit  {
    return  { mediaItem : Song ->
        viewModel.play(mediaItem)
    }
}

// TODO: abstract this method
fun onSongSelectedFromList(viewModel : SearchScreenViewModel) : (Int, Songs) -> Unit  {
    return  { itemIndex : Int, mediaItemList : Songs ->
        viewModel.playFromList(itemIndex, mediaItemList)
    }
}

fun onFolderSelected(navController : NavController) : (Folder) -> Unit {
    return {
        Log.i("folderSelected", "folder selected")
        val folderId = it.id
        val encodedFolderLibraryId = Uri.encode(folderId)
        val encodedFolderPath = it.uri
        val folderName = it.name

        val navRoute = Screen.FOLDER.name +
            "/" + encodedFolderLibraryId +
            "/" + folderName +
            "/" + encodedFolderPath

        Log.i(logTag, "navigating to $navRoute")
        navController.navigate(navRoute)
    }
}

fun buildOnSelectedMap(
    onFolderSelected : (Folder) -> Unit,
    onSongsSelected : (Int, Songs) -> Unit,
    onSongSelected : (Song) -> Unit

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