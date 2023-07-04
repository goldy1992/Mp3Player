package com.github.goldy1992.mp3player.client.models

import android.net.Uri
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.MediaItemType

class Folder constructor(
    override val id : String = Constants.UNKNOWN,
    override val type: MediaItemType = MediaItemType.FOLDER,
    val encodedLibraryId : String = Constants.UNKNOWN,
    val name : String = Constants.UNKNOWN,
    val path : String = Constants.UNKNOWN,
    val encodedPath : String = Constants.UNKNOWN,
    val uri: Uri = Uri.EMPTY,
    val playlist: Playlist = Playlist(),
    val totalDuration : Long = 0L,
    val state: State = State.NOT_LOADED,
    val isRecursive : Boolean = false
) : MediaEntity