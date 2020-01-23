package com.github.goldy1992.mp3player.client.callbacks.metadata

import android.os.Handler
import android.support.v4.media.MediaMetadataCompat
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MyMetadataCallbackTest {
    @Test
    fun testNotifyListenerWithValidMetadata() {
        val mediaMetadataCompat = MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, "id")
                .build()
        myMetadataCallback!!.processCallback(mediaMetadataCompat)
        verify(metadataListener, times(1)).onMetadataChanged(mediaMetadataCompat)
    }


    companion object {
        private var myMetadataCallback: MyMetadataCallback? = null
        private val metadataListener = mock<MetadataListener>()
        @BeforeClass
        @JvmStatic
        fun setupClass() {
            val handler = mock<Handler>()
            myMetadataCallback = MyMetadataCallback()
            myMetadataCallback!!.registerMetaDataListener(metadataListener)
        }

        @AfterClass
        @JvmStatic
        fun tearDownClass() {
            myMetadataCallback!!.removeMetaDataListener(metadataListener)
        }
    }
}