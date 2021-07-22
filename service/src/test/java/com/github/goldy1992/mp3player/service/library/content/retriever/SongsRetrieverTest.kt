package com.github.goldy1992.mp3player.service.library.content.retriever

import android.os.Looper
import android.provider.MediaStore
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.service.library.content.parser.SongResultsParser
import com.github.goldy1992.mp3player.service.library.content.request.ContentRequest
import org.mockito.kotlin.mock
import org.mockito.kotlin.spy
import org.mockito.kotlin.whenever
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows

@RunWith(RobolectricTestRunner::class)
class SongsRetrieverTest : ContentResolverRetrieverTestBase<SongsRetriever?>() {

    var resultsParser: SongResultsParser = mock<SongResultsParser>()


    @Before
    fun setup() {
        retriever = spy(SongsRetriever(contentResolver, resultsParser))
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
        whenever(contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, retriever!!.projection, null, null, null)).thenReturn(cursor)
        whenever(resultsParser.create(cursor, contentRequest!!.mediaIdPrefix!!)).thenReturn(expectedResult)
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