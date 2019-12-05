package com.github.goldy1992.mp3player.client.callbacks.playback

import android.os.Handler
import android.support.v4.media.session.PlaybackStateCompat
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MyPlaybackStateCallbackTest {
    /** a mock PlaybackStateListener  */
    @Mock
    private val mockPlaybackStateListener1: PlaybackStateListener? = null
    /** a mock PlaybackStateListener  */
    @Mock
    private val mockPlaybackStateListener2: PlaybackStateListener? = null
    /** the Playback State Callback to test  */
    private var myPlaybackStateCallback: MyPlaybackStateCallback? = null
    /** Handler for initialisation  */
    @Mock
    private val handler: Handler? = null

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        myPlaybackStateCallback = MyPlaybackStateCallback(handler!!)
    }

    /**
     * GIVEN: a registered PlaybackStateListener 'L'
     * WHEN: removePlaybackStateListener is invoked with 'L' as the parameter
     * THEN: the result is true
     */
    @Test
    fun testRemovePlaybackStateListener() {
        myPlaybackStateCallback!!.registerPlaybackStateListener(mockPlaybackStateListener1!!)
        val originalSize = myPlaybackStateCallback!!.getListeners().size
        val result = myPlaybackStateCallback!!.removePlaybackStateListener(mockPlaybackStateListener1)
        Assertions.assertTrue(result)
        val expectedNewSize = originalSize - 1
        Assert.assertEquals(expectedNewSize.toLong(), myPlaybackStateCallback!!.getListeners().size.toLong())
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