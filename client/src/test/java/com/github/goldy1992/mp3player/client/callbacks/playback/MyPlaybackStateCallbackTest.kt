package com.github.goldy1992.mp3player.client.callbacks.playback

import android.os.Handler
import android.support.v4.media.session.PlaybackStateCompat
import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MyPlaybackStateCallbackTest {
    /** a mock PlaybackStateListener  */

    private val mockPlaybackStateListener1: PlaybackStateListener = mock<PlaybackStateListener>()
    /** a mock PlaybackStateListener  */

    private val mockPlaybackStateListener2: PlaybackStateListener = mock<PlaybackStateListener>()
    /** the Playback State Callback to test  */
    private var myPlaybackStateCallback: MyPlaybackStateCallback = MyPlaybackStateCallback()


    /**
     * GIVEN: a registered PlaybackStateListener 'L'
     * WHEN: removePlaybackStateListener is invoked with 'L' as the parameter
     * THEN: the result is true
     */
    @Test
    fun testRemovePlaybackStateListener() {
        myPlaybackStateCallback!!.registerPlaybackStateListener(mockPlaybackStateListener1!!)
        val originalSize = 1
        val result = myPlaybackStateCallback!!.removePlaybackStateListener(mockPlaybackStateListener1)
        assertTrue(result)
        val expectedNewSize = originalSize - 1
    //    Assert.assertEquals(expectedNewSize.toLong(), myPlaybackStateCallback.li.toLong())
    }

    /**
     * GIVEN: an empty PlaybackStateListener set
     * WHEN: registerPlaybackStateListener is invoked
     * THEN: the result is false because nothing was removed
     */
    @Test
    fun testRemovePlaybackStateListenerOnEmptySet() {
        val result = myPlaybackStateCallback!!.removePlaybackStateListener(null)
        Assert.assertFalse(result)
    }

    /**
     * Util method to create a PlaybackStateCompat object
     * @param actions the actions
     * @return a PlaybackStateCompat with am actions long variable appended
     */
    private fun createPlaybackStateCompat(actions: Long): PlaybackStateCompat {
        return PlaybackStateCompat.Builder().setActions(actions).build()
    }
}