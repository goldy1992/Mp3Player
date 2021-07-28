package com.github.goldy1992.mp3player.service.player

import android.content.ContentResolver
import android.net.Uri
import com.google.android.exoplayer2.upstream.ContentDataSource
import com.google.android.exoplayer2.upstream.ContentDataSource.ContentDataSourceException
import com.google.android.exoplayer2.upstream.FileDataSource
import com.google.android.exoplayer2.upstream.FileDataSource.FileDataSourceException
import org.mockito.kotlin.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.IOException

@RunWith(RobolectricTestRunner::class)
class MediaSourceFactoryTest {
    private var mediaSourceFactory: MediaSourceFactory? = null

    private val fileDataSource: FileDataSource = mock<FileDataSource>()

    private val contentDataSource: ContentDataSource = mock<ContentDataSource>()

    private val testUri: Uri = mock<Uri>()

    @Before
    fun setup() {
        mediaSourceFactory = MediaSourceFactory(fileDataSource, contentDataSource)
    }

    @Test
    @Throws(ContentDataSourceException::class, FileDataSourceException::class)
    fun testCreateContentMediaSource() {
        whenever(testUri.scheme).thenReturn(ContentResolver.SCHEME_CONTENT)
        val result = mediaSourceFactory!!.createMediaSource(testUri)
        Assert.assertNotNull(result)
        verify(contentDataSource, times(1)).open(any())
        verify(fileDataSource, never()).open(any())
    }

    @Test
    @Throws(ContentDataSourceException::class, FileDataSourceException::class)
    fun testCreateFileMediaSource() {
        whenever(testUri.scheme).thenReturn(ContentResolver.SCHEME_FILE)
        val result = mediaSourceFactory!!.createMediaSource(testUri)
        Assert.assertNotNull(result)
        verify(fileDataSource, times(1)).open(any())
        verify(contentDataSource, never()).open(any())
    }

    @Test
    @Throws(ContentDataSourceException::class)
    fun returnNullOnException() {
        whenever(contentDataSource.open(any())).thenThrow(ContentDataSourceException(IOException()))
        Assert.assertNull(mediaSourceFactory!!.createMediaSource(testUri))
    }
}