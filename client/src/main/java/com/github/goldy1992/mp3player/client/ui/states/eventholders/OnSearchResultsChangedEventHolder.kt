package com.github.goldy1992.mp3player.client.ui.states.eventholders

import androidx.media3.session.MediaLibraryService
import com.github.goldy1992.mp3player.client.utils.MediaLibraryParamUtils.getDefaultLibraryParams

data class OnSearchResultsChangedEventHolder constructor(
    val query: String,
    val itemCount: Int,
    val params: MediaLibraryService.LibraryParams? = getDefaultLibraryParams()
) {
    companion object {
        val DEFAULT = OnSearchResultsChangedEventHolder(
            query = "",
            itemCount = 1,
            params = getDefaultLibraryParams()
        )
    }
}