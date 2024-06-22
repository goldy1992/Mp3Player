package com.github.goldy1992.mp3player.service.library.content.filter

import androidx.media3.common.MediaItem
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.File

@RunWith(RobolectricTestRunner::class)
class SongsFromFolderResultsFilterTest {
    private var songsFromFolderResultsFilter: SongsFromFolderResultsFilter? = null

    @Before
    fun setup() {
        songsFromFolderResultsFilter = SongsFromFolderResultsFilter()
    }

    @Test
    fun testFilterValidQuery() {
        val query = File.pathSeparator + "a" + File.pathSeparator + "b" + File.pathSeparator + "c"
        val expectedDirectory = File(query)
        val dontFilter = MediaItemBuilder(
            mediaId = "fds",
            file = expectedDirectory
        ).build()
        val toFilter = MediaItemBuilder(
            mediaId = "fds",
            file = File("/a/otherDir")
        ).build()
        val items: MutableList<MediaItem> = ArrayList()
        items.add(dontFilter)
        items.add(toFilter)
        val results = songsFromFolderResultsFilter!!.filter(query, items)

        Assert.assertEquals(1, results.size.toLong())
        val result : MediaItem = results.get(0)
        Assert.assertTrue(MediaItemUtils.getDirectoryPath(result).contains(query))
    }
}