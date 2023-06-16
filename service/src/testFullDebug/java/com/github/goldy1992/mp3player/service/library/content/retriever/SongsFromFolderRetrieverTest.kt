package com.github.goldy1992.mp3player.service.library.content.retriever

import android.os.Looper
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.service.library.content.parser.SongResultsParser
import com.github.goldy1992.mp3player.service.library.content.retrievers.SongsFromFolderRetriever
import com.github.goldy1992.mp3player.service.library.data.search.SongDao
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows

@RunWith(RobolectricTestRunner::class)
class SongsFromFolderRetrieverTest : ContentResolverRetrieverTestBase<SongsFromFolderRetriever?>() {

    var resultsParser: SongResultsParser = mock<SongResultsParser>()

    var songDao: SongDao = mock<SongDao>()

    @Before
    fun setup() {
        retriever = SongsFromFolderRetriever(contentResolver, resultsParser, null)
    }

    @Test
    fun testGetChildren() {
        val id = "xyz"
        val title = "title"
        val mediaItem = MediaItemBuilder(id)
                .setTitle(title)
                .build()
        expectedResult.add(mediaItem)
        whenever(contentResolver.query(any(), any(), any(), any(), eq(null))).thenReturn(cursor)
        whenever(resultsParser.create(cursor)).thenReturn(expectedResult)
        val result = retriever!!.getChildren(id)
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