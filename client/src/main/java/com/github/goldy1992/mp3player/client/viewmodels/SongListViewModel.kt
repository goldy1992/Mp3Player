package com.github.goldy1992.mp3player.client.viewmodels

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import com.github.goldy1992.mp3player.commons.LogTagger

class SongListViewModel

    @ViewModelInject
    constructor() : MediaListViewModel(), LogTagger {

    init {
        Log.i(logTag(), "creating SongViewModel")
    }

    override fun logTag(): String {
        return "SONG_VIEW_MODEL"
    }
}
