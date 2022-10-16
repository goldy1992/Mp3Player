package com.github.goldy1992.mp3player.service.library.content.filter

import androidx.media3.common.MediaItem
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.File
import java.util.*

@RunWith(RobolectricTestRunner::class)
class SongsFromFolderResultsFilterTest {
    private var songsFromFolderResultsFilter: SongsFromFolderResultsFilter? = null

    @Before
    fun setup() {
        songsFromFolderResultsFilter = SongsFromFolderResultsFilter()
    }

    @Test
    fun testFilterValidQuery() {
        val query = "/a/b/c"
        val expectedDirectory = File(query)
        val dontFilter = MediaItemBuilder("fds")
                .setDirectoryFile(expectedDirectory)
                .build()
        val toFilter = MediaItemBuilder("fds")
                .setDirectoryFile(File("/a/otherDir"))
                .build()
        val items: MutableList<MediaItem> = ArrayList()
        items.add(dontFilter)
        items.add(toFilter)
        val results = songsFromFolderResultsFilter!!.filter(query, items)
        Assert.assertEquals(1, results!!.size.toLong())
        Assert.assertTrue(results.contains(dontFilter))
        Assert.assertFalse(results.contains(toFilter))
    }
}