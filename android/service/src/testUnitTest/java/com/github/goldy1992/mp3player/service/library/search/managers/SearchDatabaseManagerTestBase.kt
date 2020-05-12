package com.github.goldy1992.mp3player.service.library.search.managers

import android.os.Handler
import com.github.goldy1992.mp3player.service.library.ContentManager
import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds
import com.github.goldy1992.mp3player.service.library.search.SearchDatabase
import com.nhaarman.mockitokotlin2.mock

abstract class SearchDatabaseManagerTestBase {

    var contentManager: ContentManager = mock<ContentManager>()
    lateinit var handler: Handler
    var searchDatabase: SearchDatabase = mock<SearchDatabase>()
    var mediaItemTypeIds: MediaItemTypeIds = MediaItemTypeIds()

    open fun setup() {
        handler = Handler()
    }

    abstract fun testInsert()
}