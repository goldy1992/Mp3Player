package com.github.goldy1992.mp3player.client.ui.screens;

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.commons.AudioSample
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VisualizerViewModel

        @Inject
        constructor(
               val mediaControllerAdapter: MediaControllerAdapter) : ViewModel() {

        val audioData : MutableLiveData<AudioSample> = mediaControllerAdapter.audioStream
}
