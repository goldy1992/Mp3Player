package com.github.goldy1992.mp3player.service.library.search.managers

import com.github.goldy1992.mp3player.service.library.ContentManager
import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds
import com.github.goldy1992.mp3player.service.library.data.search.SearchDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.mockito.kotlin.mock

@OptIn(ExperimentalCoroutinesApi::class)
abstract class SearchDatabaseManagerTestBase {

    var contentManager: ContentManager = mock<ContentManager>()
    var searchDatabase: SearchDatabase = mock<SearchDatabase>()
    var mediaItemTypeIds: MediaItemTypeIds = MediaItemTypeIds()
    protected val testScheduler : TestCoroutineScheduler = TestCoroutineScheduler()
    protected val dispatcher : TestDispatcher = UnconfinedTestDispatcher(testScheduler)
    protected val testScope : TestScope = TestScope(dispatcher)
    open fun setup() {
    }

    abstract fun testInsert()
}