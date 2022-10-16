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
class FolderSearchResultsFilterTest {
    private var folderSearchResultsFilter: FolderSearchResultsFilter? = null
    @Before
    fun setup() {
        folderSearchResultsFilter = FolderSearchResultsFilter()
    }

    @Test
    fun testFilterTwoItems() {
        val filterQuery = "abc"
        val expectedResultsSize = 2
        val file1ToKeep = File("/a/b/abc101")
        val item1Keep = MediaItemBuilder("id")
                .setDirectoryFile(file1ToKeep)
                .build()
        val file2ToThrow = File("/a/b/1ac101")
        val item2Throw = MediaItemBuilder("id")
                .setDirectoryFile(file2ToThrow)
                .build()
        val file3ToThrow = File("/a/abc/101")
        val item3Throw = MediaItemBuilder("id")
                .setDirectoryFile(file3ToThrow)
                .build()
        val file4ToKeep = File("/a/abc/abc10abc1")
        val item4Keep = MediaItemBuilder("id")
                .setDirectoryFile(file4ToKeep)
                .build()
        val resultsToProcess: MutableList<MediaItem> = ArrayList()
        resultsToProcess.add(item1Keep)
        resultsToProcess.add(item2Throw)
        resultsToProcess.add(item3Throw)
        resultsToProcess.add(item4Keep)
        val results = folderSearchResultsFilter!!.filter(filterQuery, resultsToProcess)
        Assert.assertEquals(expectedResultsSize.toLong(), results!!.size.toLong())
        Assert.assertTrue(results.contains(item1Keep))
        Assert.assertTrue(results.contains(item4Keep))
    }
}