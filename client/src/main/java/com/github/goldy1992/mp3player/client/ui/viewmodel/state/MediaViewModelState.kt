package com.github.goldy1992.mp3player.client.ui.viewmodel.state

import com.github.goldy1992.mp3player.client.data.repositories.media.MediaRepository
import com.github.goldy1992.mp3player.commons.LogTagger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

abstract class MediaViewModelState<T>(

    protected val mediaRepository: MediaRepository,
    protected val scope: CoroutineScope) : LogTagger {

    abstract fun state() : StateFlow<T>
}