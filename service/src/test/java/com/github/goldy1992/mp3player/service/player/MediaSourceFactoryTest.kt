package com.github.goldy1992.mp3player.service.player

import android.content.ContentResolver
import android.net.Uri
import com.google.android.exoplayer2.upstream.ContentDataSource
import com.google.android.exoplayer2.upstream.ContentDataSource.ContentDataSourceException
import com.google.android.exoplayer2.upstream.FileDataSource
import com.google.android.exoplayer2.upstream.FileDataSource.FileDataSourceException
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import java.io.IOException

@RunWith(RobolectricTestRunner::class)
class MediaSourceFactoryTest {
    private var mediaSourceFactory: MediaSourceFactory? = null
    @Mock
    private val fileDataSource: FileDataSource? = null
    @Mock
    private val contentDataSource: ContentDataSource? = null
    @Mock
    private val testUri: Uri? = null

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        mediaSourceFactory = MediaSourceFactory(fileDataSource!!, contentDataSource!!)
    }

    @Test
    @Throws(ContentDataSourceException::class, FileDataSourceException::class)
    fun testCreateContentMediaSource() {
        Mockito.`when`(testUri!!.scheme).thenReturn(ContentResolver.SCHEME_CONTENT)
        val result = mediaSourceFactory!!.createMediaSource(testUri)
        Assert.assertNotNull(result)
        Mockito.verify(contentDataSource, Mockito.times(1))!!.open(ArgumentMatchers.any())
        Mockito.verify(fileDataSource, Mockito.never())!!.open(ArgumentMatchers.any())
    }

    @Test
    @Throws(ContentDataSourceException::class, FileDataSourceException::class)
    fun testCreateFileMediaSource() {
        Mockito.`when`(testUri!!.scheme).thenReturn(ContentResolver.SCHEME_FILE)
        val result = mediaSourceFactory!!.createMediaSource(testUri)
        Assert.assertNotNull(result)
        Mockito.verify(fileDataSource, Mockito.times(1))!!.open(ArgumentMatchers.any())
        Mockito.verify(contentDataSource, Mockito.never())!!.open(ArgumentMatchers.any())
    }

    @Test
    @Throws(ContentDataSourceException::class)
    fun returnNullOnException() {
        Mockito.`when`(contentDataSource!!.open(ArgumentMatchers.any())).thenThrow(ContentDataSourceException(IOException()))
        Assert.assertNull(mediaSourceFactory!!.createMediaSource(testUri))
    }
}