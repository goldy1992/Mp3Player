package com.github.goldy1992.mp3player.client.callbacks.playback

import android.os.Handler
import android.support.v4.media.session.PlaybackStateCompat
import com.github.goldy1992.mp3player.client.callbacks.Listener
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

    private var myPlaybackStateCallback: MyPlaybackStateCallback = MyPlaybackStateCallback()


    /**
     * GIVEN: a registered PlaybackStateListener 'L'
     * WHEN: removePlaybackStateListener is invoked with 'L' as the parameter
     * THEN: the result is true
     */
    @Test
    fun testRemovePlaybackStateListener() {
        myPlaybackStateCallback.registerListener(mockPlaybackStateListener1)
        val result = myPlaybackStateCallback.removeListener(mockPlaybackStateListener1)
        assertTrue(result)
   }

    /**
     * GIVEN: an empty PlaybackStateListener set
     * WHEN: registerPlaybackStateListener is invoked
     * THEN: the result is false because nothing was removed
     */
    @Test
    fun testRemovePlaybackStateListenerOnEmptySet() {
        val result = myPlaybackStateCallback.removeListener(mock<Listener>())
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