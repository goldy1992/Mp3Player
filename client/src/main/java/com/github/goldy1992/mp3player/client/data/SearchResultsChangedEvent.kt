package com.github.goldy1992.mp3player.client.data

import android.os.Bundle

data class SearchResultsChangedEvent constructor(
    val query: String,
    val itemCount: Int,
    val params: Bundle? = Bundle()
) {
    companion object {
        val DEFAULT = SearchResultsChangedEvent(
            query = "",
            itemCount = 1,
        )
    }
}