package com.github.goldy1992.mp3player.client.views.fragments

import android.os.Bundle
import android.os.HandlerThread
import android.provider.Settings.Global.putInt
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.FragmentScenario.FragmentAction
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.goldy1992.mp3player.CustomMatchers
import com.github.goldy1992.mp3player.client.R
import org.hamcrest.CoreMatchers
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PlaybackTrackerFragmentTest {

    private val HANDLER_THREAD_ID = "HANDLER_THREAD_ID"
    private var duration: Long = 0
    private var position = 0
    private var playbackSpeed = 0f
    @PlaybackStateCompat.State
    private var playbackState = PlaybackStateCompat.STATE_PAUSED
    var fragmentScenario: FragmentScenario<PlaybackTrackerFragment>? = null
    var onMetadataChangedAction: FragmentAction<PlaybackTrackerFragment>? = null
    var onPlaybackStateChangedAction: FragmentAction<PlaybackTrackerFragment>? = null

    @Before
    fun init() {
        val fragmentFactory = FragmentFactory()
        // The "fragmentArgs" and "factory" arguments are optional.
        val fragmentArgs = Bundle().apply {
            putInt("selectedListItem", 0)
        }
        fragmentScenario = launchFragmentInContainer<PlaybackTrackerFragment>()
        fragmentScenario!!.onFragment { fragment: PlaybackTrackerFragment -> initFragment(fragment) }
        onMetadataChangedAction = FragmentAction { fragment: PlaybackTrackerFragment -> changeMetaData(fragment) }
        onPlaybackStateChangedAction = FragmentAction { fragment: PlaybackTrackerFragment -> changePlaybackState(fragment) }
    }

    /**
     * GIVEN an initialised instance of PlaybackTrackerFragment
     * WHEN: onMetadataChanged is called with a duration of twenty seconds
     * THEN: the duration TextView is updated accordingly.
     */
    @Test
    fun testDurationUpdated() {
        duration = 20000 // 20 seconds
        position = 10000
        playbackState = PlaybackStateCompat.STATE_PAUSED
        playbackSpeed = 1.0f
        val EXPECTED = "20"
        fragmentScenario!!.onFragment(onMetadataChangedAction!!)
        Espresso.onView(ViewMatchers.withId(R.id.duration)).check(ViewAssertions.matches(ViewMatchers.withText(CoreMatchers.containsString(EXPECTED))))
        fragmentScenario!!.onFragment(onPlaybackStateChangedAction!!)
        Espresso.onView(ViewMatchers.withId(R.id.seekBar)).check(ViewAssertions.matches(ViewMatchers.isCompletelyDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.seekBar)).check(ViewAssertions.matches(CustomMatchers.withSeekbarProgress(10000)))
    }

    /**
     *
     * @param fragment
     */
    private fun initFragment(fragment: PlaybackTrackerFragment) {
        val worker = HandlerThread(HANDLER_THREAD_ID)
        worker.start()
        //        MediaControllerAdapter mediaControllerAdapter = new MediaControllerAdapter(getApplicationContext(), worker.getLooper());
//        fragment.init(mediaControllerAdapter);
    }

    private fun changeMetaData(fragment: PlaybackTrackerFragment) {
        val metadataCompat = MediaMetadataCompat.Builder()
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, duration).build()
        fragment.onMetadataChanged(metadataCompat)
    }

    private fun changePlaybackState(fragment: PlaybackTrackerFragment) {
        val playbackStateCompat = PlaybackStateCompat.Builder()
                .setState(playbackState, position.toLong(), playbackSpeed).build()
        fragment.onPlaybackStateChanged(playbackStateCompat)
    }
}