package com.github.goldy1992.mp3player.client.views.fragments.viewpager

import android.os.Looper
import androidx.fragment.app.testing.FragmentScenario.FragmentAction
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.activities.TestMainActivity
import com.github.goldy1992.mp3player.client.views.fragments.FragmentTestBase
import com.github.goldy1992.mp3player.client.views.fragments.viewpager.SongListFragment
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.github.goldy1992.mp3player.commons.MediaItemType
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.android.controller.ActivityController
import org.robolectric.annotation.LooperMode

@RunWith(RobolectricTestRunner::class)
@LooperMode(LooperMode.Mode.PAUSED)
class SongListFragmentTest : FragmentTestBase<SongListFragment>() {
    private var folderListFragment: SongListFragment? = null

    @Before
    fun setup() {
        activityScenario = Robolectric.buildActivity(TestMainActivity::class.java).setup()
        val testMainActivity: TestMainActivity = activityScenario!!.get()
        val component = testMainActivity.mediaActivityCompatComponent
        folderListFragment = SongListFragment.newInstance(MediaItemType.SONGS, "id", component)
        super.setup(folderListFragment, SongListFragment::class.java)
    }

    @Test
    fun testItemSelected() {
        val action = FragmentAction<SongListFragment> { fragment: SongListFragment? -> itemSelected(fragment) }
        performAction(action)
    }

    private fun itemSelected(fragment: MediaItemListFragment?) {
        val mediaControllerAdapter = Mockito.mock(MediaControllerAdapter::class.java)
        fragment!!.mediaControllerAdapter = mediaControllerAdapter
        val expectedLibraryId = "ID"
        val mediaItem = MediaItemBuilder("id")
                .setLibraryId(expectedLibraryId)
                .build()
        fragment.itemSelected(mediaItem)
        Shadows.shadowOf(Looper.getMainLooper()).idle()
        Mockito.verify(mediaControllerAdapter).playFromMediaId(expectedLibraryId, null)
    }
}