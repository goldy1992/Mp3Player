package com.github.goldy1992.mp3player.client.ui.viewmodel.actions

import com.github.goldy1992.mp3player.client.data.repositories.media.MediaRepository
import com.github.goldy1992.mp3player.commons.LogTagger
import kotlinx.coroutines.CoroutineScope

interface MediaViewModelBase : LogTagger {

    val scope : CoroutineScope
    val mediaRepository : MediaRepository
}