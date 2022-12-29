package com.github.goldy1992.mp3player.client.ui.states.eventholders

import androidx.media3.session.MediaLibraryService
import com.github.goldy1992.mp3player.client.utils.MediaLibraryParamUtils.getDefaultLibraryParams

data class OnChildrenChangedEventHolder(
    val parentId: String,
    val itemCount: Int,
    val params: MediaLibraryService.LibraryParams?) {
    companion object {
        val DEFAULT = OnChildrenChangedEventHolder("", 1, getDefaultLibraryParams())
    }
}
