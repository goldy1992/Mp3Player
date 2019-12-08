package com.github.goldy1992.mp3player.service.library.search.managers

import android.os.Handler
import com.github.goldy1992.mp3player.service.library.ContentManager
import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds
import com.github.goldy1992.mp3player.service.library.search.SearchDatabase
import org.mockito.Mock

abstract class SearchDatabaseManagerTestBase {
    @Mock
    var contentManager: ContentManager? = null
    var handler: Handler? = null
    @Mock
    var searchDatabase: SearchDatabase? = null
    var mediaItemTypeIds: MediaItemTypeIds? = null
    open fun setup() {
        handler = Handler()
    }

    abstract fun testInsert()
    abstract fun testReindex()
}