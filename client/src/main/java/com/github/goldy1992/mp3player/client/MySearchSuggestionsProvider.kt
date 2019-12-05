package com.github.goldy1992.mp3player.client

import android.content.SearchRecentSuggestionsProvider
import android.database.Cursor
import android.net.Uri

class MySearchSuggestionsProvider : SearchRecentSuggestionsProvider() {

    override fun query(uri: Uri,
                       projection: Array<String>,
                       selection: String,
                       selectionArgs: Array<String>,
                       sortOrder: String): Cursor? { // gets recent suggestions
        val recentSuggestions = super.query(uri, projection, selection, selectionArgs, sortOrder)
        // get string query
        val query = uri.lastPathSegment.toLowerCase()
        return null
    }

    companion object {
        const val AUTHORITY = "com.github.goldy1992.mp3player.client.MySearchSuggestionsProvider"
        const val MODE = DATABASE_MODE_QUERIES
    }

    init {
        setupSuggestions(AUTHORITY, MODE)
    }
}