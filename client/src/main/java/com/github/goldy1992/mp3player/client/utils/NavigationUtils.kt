package com.github.goldy1992.mp3player.client.utils

import android.util.Base64
import android.util.Log
import androidx.navigation.NavController
import com.github.goldy1992.mp3player.client.models.media.Album
import com.github.goldy1992.mp3player.client.models.media.Folder
import com.github.goldy1992.mp3player.client.models.media.MediaEntity
import com.github.goldy1992.mp3player.client.ui.components.equalizer.VisualizerType
import com.github.goldy1992.mp3player.commons.LogTagger
import com.github.goldy1992.mp3player.commons.Screen

/**
 * Util object for navigating to different screens given a [MediaEntity].
 */
object NavigationUtils : LogTagger{

    fun navigate(navController: NavController, album: Album) {
        val albumId = album.id
        val albumTitle = album.title
        val albumArtist = album.artist
        val albumArtUriBase64 = Base64.encodeToString(album.artworkUri.toString().encodeToByteArray(), Base64.DEFAULT)
        Log.d(logTag(), "onAlbumSelected() Album $albumTitle uri: ${album.artworkUri}")
        navController.navigate(
            Screen.ALBUM.name
                    + "/" + albumId
                    + "/" + albumTitle
                    + "/" + albumArtist
                    + "/" + albumArtUriBase64)
    }

    fun navigate(navController: NavController, folder : Folder) {
        val encodedFolderLibraryId = folder.encodedLibraryId
        val encodedFolderPath = folder.encodedPath
        val folderName = folder.name
        Log.d(logTag(), "navigate() invoked with folder: $folder")
        navController.navigate(
            Screen.FOLDER.name
                    + "/" + encodedFolderLibraryId
                    + "/" + folderName
                    + "/" + encodedFolderPath)

    }

    fun navigate(navController: NavController, visualizerType: VisualizerType) {
        val visualizerTypeString = visualizerType.name
        Log.d(logTag(), "navigate() invoked with visualizerType: $visualizerType")
        navController.navigate(
            Screen.SINGLE_VISUALIZER.name
                    + "/" + visualizerTypeString)

    }
    override fun logTag(): String {
        return "NavigationUtils"
    }
}