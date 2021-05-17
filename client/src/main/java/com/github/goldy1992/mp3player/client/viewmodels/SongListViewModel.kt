package com.github.goldy1992.mp3player.client.viewmodels

import android.util.Log
import com.github.goldy1992.mp3player.commons.LogTagger
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SongListViewModel

    @Inject
    constructor() : MediaListViewModel(), LogTagger {

    init {
        Log.i(logTag(), "creating SongViewModel")
    }

    override fun logTag(): String {
        return "SONG_VIEW_MODEL"
    }
}
