package com.github.goldy1992.mp3player.client.callbacks.metadata

import android.os.Handler
import android.support.v4.media.MediaMetadataCompat
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
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


    companion object {
        private var myMetadataCallback: MyMetadataCallback? = null
        private val metadataListener = mock<MetadataListener>()
        @BeforeClass
        @JvmStatic
        fun setupClass() {
            val handler = Mockito.mock(Handler::class.java)
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