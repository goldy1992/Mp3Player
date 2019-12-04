package com.github.goldy1992.mp3player.client.callbacks.metadata

import android.os.Handler
import android.support.v4.media.MediaMetadataCompat
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MyMetadataCallbackTest {
    @Test
    fun testNotifyListenerWithValidMetadata() {
        val mediaMetadataCompat = MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, "id")
                .build()
        myMetadataCallback!!.processCallback(mediaMetadataCompat)
        Mockito.verify(metadataListener, Mockito.times(1)).onMetadataChanged(mediaMetadataCompat)
    }

    @Test
    fun testNotifyListenerWithNullMetadataCompat() {
        myMetadataCallback!!.processCallback(null)
        Mockito.verify(metadataListener, Mockito.never()).onMetadataChanged(ArgumentMatchers.any())
    }

    companion object {
        private var myMetadataCallback: MyMetadataCallback? = null
        private val metadataListener = Mockito.mock(MetadataListener::class.java)
        @BeforeClass
        fun setupClass() {
            val handler = Mockito.mock(Handler::class.java)
            myMetadataCallback = MyMetadataCallback(handler)
            myMetadataCallback!!.registerMetaDataListener(metadataListener)
        }

        @AfterClass
        fun tearDownClass() {
            myMetadataCallback!!.removeMetaDataListener(metadataListener)
        }
    }
}