package com.github.goldy1992.mp3player.service.library.content.retriever

import android.content.ContentResolver
import android.database.Cursor
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.github.goldy1992.mp3player.service.library.content.parser.SongResultsParser
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MediaItemFromIdRetrieverTest {
    private var mediaItemFromIdRetriever: MediaItemFromIdRetriever? = null
    @Mock
    private val contentResolver: ContentResolver? = null
    @Mock
    var songResultsParser: SongResultsParser? = null

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        mediaItemFromIdRetriever = MediaItemFromIdRetriever(contentResolver!!, songResultsParser!!)
    }

    @Test
    fun testNullCursor() {
        val id = 23L
        Mockito.`when`(contentResolver!!.query(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(null)
        val result = mediaItemFromIdRetriever!!.getItem(id)
        Assert.assertNull(result)
        Mockito.verify(songResultsParser, Mockito.never())!!.create(ArgumentMatchers.any(), ArgumentMatchers.any())
    }

    @Test
    fun testGetItemWithResult() {
        val id = 5464L
        val cursor = Mockito.mock(Cursor::class.java)
        val expectedMediaItem = MediaItemBuilder("anId")
                .build()
        val listToReturn = listOf(expectedMediaItem)
        Mockito.`when`(contentResolver!!.query(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(cursor)
        Mockito.`when`(songResultsParser!!.create(ArgumentMatchers.eq(cursor), ArgumentMatchers.anyString())).thenReturn(listToReturn)
        val result = mediaItemFromIdRetriever!!.getItem(id)
        Assert.assertEquals(expectedMediaItem, result)
    }
}