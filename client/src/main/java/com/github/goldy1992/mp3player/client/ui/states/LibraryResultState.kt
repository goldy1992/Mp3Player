package com.github.goldy1992.mp3player.client.ui.states

import androidx.media3.common.MediaItem
import com.github.goldy1992.mp3player.commons.MediaItemType

enum class State {
    NOT_LOADED,
    LOADING,
    NO_RESULTS,
    LOADED
}

data class LibraryResultState(
    val state: State,
    val mediaItemType: MediaItemType = MediaItemType.NONE,
    val results : List<MediaItem> = emptyList()
) {
    companion object {
        fun notLoaded(mediaItemType: MediaItemType) : LibraryResultState {
            return LibraryResultState(
                state = State.NOT_LOADED,
                mediaItemType = mediaItemType
            )
        }

        fun loading(mediaItemType: MediaItemType) : LibraryResultState {
            return LibraryResultState(
                state = State.LOADING,
                mediaItemType = mediaItemType
            )
        }

        fun noResults(mediaItemType: MediaItemType) : LibraryResultState {
            return LibraryResultState(
                state = State.NO_RESULTS,
                mediaItemType = mediaItemType
            )
        }

        fun loaded(mediaItemType: MediaItemType,
                    results: List<MediaItem>) : LibraryResultState {
            return LibraryResultState(
                state = State.LOADED,
                results = results,
                mediaItemType = mediaItemType
            )
        }

    }
}
