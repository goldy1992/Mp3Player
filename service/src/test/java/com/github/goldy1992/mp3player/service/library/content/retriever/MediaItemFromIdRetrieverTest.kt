package com.github.goldy1992.mp3player.service.library.content.retriever

import android.content.ContentResolver
import android.database.Cursor
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.github.goldy1992.mp3player.service.library.content.parser.SongResultsParser
import com.nhaarman.mockitokotlin2.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MediaItemFromIdRetrieverTest {
    private var mediaItemFromIdRetriever: MediaItemFromIdRetriever? = null
    
    private val contentResolver: ContentResolver = mock<ContentResolver>()
    
    var songResultsParser: SongResultsParser = mock<SongResultsParser>()

    @Before
    fun setup() {
        mediaItemFromIdRetriever = MediaItemFromIdRetriever(contentResolver, songResultsParser)
    }

    @Test
    fun testNullCursor() {
        val id = 23L
        whenever(contentResolver.query(any(), any(), any(), any(), any())).thenReturn(null)
        val result = mediaItemFromIdRetriever!!.getItem(id)
        Assert.assertNull(result)
        verify(songResultsParser, never()).create(any(), any())
    }

    @Test
    fun testGetItemWithResult() {
        val id = 5464L
        val cursor = mock<Cursor>()
        val expectedMediaItem = MediaItemBuilder("anId")
                .build()
        val listToReturn = listOf(expectedMediaItem)
        whenever(contentResolver.query(any(), any(), any(), any(), eq(null))).thenReturn(cursor)
        whenever(songResultsParser.create(eq(cursor), any<String>())).thenReturn(listToReturn)
        val result = mediaItemFromIdRetriever!!.getItem(id)
        Assert.assertEquals(expectedMediaItem, result)
    }
}