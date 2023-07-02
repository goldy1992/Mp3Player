package com.github.goldy1992.mp3player.client.data

import android.net.Uri
import com.github.goldy1992.mp3player.client.ui.states.State
import com.github.goldy1992.mp3player.commons.Constants

class Folder constructor(
    val id : String = Constants.UNKNOWN,
    val encodedLibraryId : String = Constants.UNKNOWN,
    val name : String = Constants.UNKNOWN,
    val path : String = Constants.UNKNOWN,
    val encodedPath : String = Constants.UNKNOWN,
    val uri: Uri = Uri.EMPTY,
    val playlist: Playlist= Playlist(),
    val totalDuration : Long = 0L,
    val state: State = State.NOT_LOADED,
    val isRecursive : Boolean = false
) : MediaEntity