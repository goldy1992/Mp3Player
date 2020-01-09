package com.github.goldy1992.mp3player.service.library.content.retriever

import android.os.Looper
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.service.library.content.filter.SongsFromFolderResultsFilter
import com.github.goldy1992.mp3player.service.library.content.parser.SongResultsParser
import com.github.goldy1992.mp3player.service.library.content.request.ContentRequest
import com.github.goldy1992.mp3player.service.library.search.SongDao
import com.nhaarman.mockitokotlin2.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows

@RunWith(RobolectricTestRunner::class)
class SongsFromFolderRetrieverTest : ContentResolverRetrieverTestBase<SongsFromFolderRetriever?>() {

    var resultsParser: SongResultsParser = mock<SongResultsParser>()

    var songDao: SongDao = mock<SongDao>()

    @Before
    fun setup() {
        retriever = spy(SongsFromFolderRetriever(contentResolver!!, resultsParser!!, handler, SongsFromFolderResultsFilter()))
    }

    @Test
    fun testGetChildren() {
        contentRequest = ContentRequest("x", "y", "z")
        val id = "xyz"
        val title = "title"
        val mediaItem = MediaItemBuilder(id)
                .setTitle(title)
                .build()
        expectedResult.add(mediaItem)
        whenever(contentResolver!!.query(any(), any(), any(), any(), eq(null))).thenReturn(cursor)
        whenever(resultsParser!!.create(cursor!!, contentRequest!!.mediaIdPrefix!!)).thenReturn(expectedResult)
        val result = retriever!!.getChildren(contentRequest!!)
        // call remaining looper messages
        Shadows.shadowOf(Looper.getMainLooper()).idle()
        // assert results are the expected ones
        Assert.assertEquals(expectedResult, result)
    }

    @Test
    override fun testGetMediaType() {
        Assert.assertEquals(MediaItemType.SONG, retriever!!.type)
    }
}