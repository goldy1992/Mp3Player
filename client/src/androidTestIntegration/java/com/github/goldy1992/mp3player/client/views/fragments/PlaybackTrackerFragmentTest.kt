package com.github.goldy1992.mp3player.client.views.fragments

import android.os.HandlerThread
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.fragment.app.FragmentFactory
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ActivityScenario.ActivityAction
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.activities.MediaActivityCompatAutomationImpl
import com.goldy1992.mp3player.commons.CustomMatchers
import org.hamcrest.CoreMatchers
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(androidx.test.ext.junit.runners.AndroidJUnit4::class)
class PlaybackTrackerFragmentTest {

    companion object {
        private val FRAGMENT_TAG = "PlaybackTrackerFragment"
    }

    private val fragmentFactory : FragmentFactory = FragmentFactory()

    private val HANDLER_THREAD_ID = "HANDLER_THREAD_ID"
    private var duration: Long = 0
    private var position = 0
    private var playbackSpeed = 0f
    @PlaybackStateCompat.State
    private var playbackState = PlaybackStateCompat.STATE_PAUSED

    private lateinit var playbackTrackerFragment: PlaybackTrackerFragment

    private lateinit var activityScenario : ActivityScenario<MediaActivityCompatAutomationImpl>
  //  var fragmentScenario: FragmentScenario<PlaybackTrackerFragment>? = null
    var onMetadataChangedAction: ActivityAction<MediaActivityCompatAutomationImpl>? = null
    var onPlaybackStateChangedAction: ActivityAction<MediaActivityCompatAutomationImpl>? = null

    @Before
    fun init() {
       this.playbackTrackerFragment = PlaybackTrackerFragment()
        activityScenario = ActivityScenario.launch(MediaActivityCompatAutomationImpl::class.java)
        activityScenario.onActivity { activity ->

            activity.supportFragmentManager.fragmentFactory = fragmentFactory
            activity.supportFragmentManager
                    .beginTransaction()
                    .add(R.id.fragmentContainer, playbackTrackerFragment, FRAGMENT_TAG)
                    .commitNow()
        }

//              fragmentScenario!!.onFragment { fragment: PlaybackTrackerFragment -> initFragment(fragment) }
        onMetadataChangedAction = ActivityAction { activity: MediaActivityCompatAutomationImpl -> changeMetaData(activity) }
        onPlaybackStateChangedAction = ActivityAction { activity: MediaActivityCompatAutomationImpl -> changePlaybackState(activity) }
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
        activityScenario.onActivity(onMetadataChangedAction!!)
        Espresso.onView(ViewMatchers.withId(R.id.duration)).check(ViewAssertions.matches(ViewMatchers.withText(CoreMatchers.containsString(EXPECTED))))
        activityScenario.onActivity(onPlaybackStateChangedAction!!)
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

    private fun changeMetaData(activity: MediaActivityCompatAutomationImpl) {
        val fragment = getFragment(activity)
        val metadataCompat = MediaMetadataCompat.Builder()
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, duration).build()
        fragment!!.onMetadataChanged(metadataCompat)
    }

    private fun changePlaybackState(activity: MediaActivityCompatAutomationImpl) {
        val fragment = getFragment(activity)
        val playbackStateCompat = PlaybackStateCompat.Builder()
                .setState(playbackState, position.toLong(), playbackSpeed).build()
        fragment!!.onPlaybackStateChanged(playbackStateCompat)
    }

    private fun getFragment(activity : MediaActivityCompatAutomationImpl) : PlaybackTrackerFragment? {
        return activity.supportFragmentManager.fragments[0] as? PlaybackTrackerFragment
    }
}