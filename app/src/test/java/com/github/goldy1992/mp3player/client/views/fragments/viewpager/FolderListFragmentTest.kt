package com.github.goldy1992.mp3player.client.views.fragments.viewpager

import android.os.Looper
import android.support.v4.media.MediaBrowserCompat
import androidx.fragment.app.testing.FragmentScenario.FragmentAction
import com.github.goldy1992.mp3player.TestUtils
import com.github.goldy1992.mp3player.client.activities.TestMainActivity
import com.github.goldy1992.mp3player.client.views.fragments.FragmentTestBase
import com.github.goldy1992.mp3player.client.views.fragments.viewpager.FolderListFragment
import com.github.goldy1992.mp3player.commons.MediaItemType
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.android.controller.ActivityController
import org.robolectric.annotation.LooperMode

@RunWith(RobolectricTestRunner::class)
@LooperMode(LooperMode.Mode.PAUSED)
class FolderListFragmentTest : FragmentTestBase<FolderListFragment?>() {
    private var folderListFragment: FolderListFragment? = null
    private override var activityScenario: ActivityController<TestMainActivity>? = null
    @Before
    fun setup() {
        activityScenario = Robolectric.buildActivity(TestMainActivity::class.java).setup()
        val testMainActivity: TestMainActivity = activityScenario.get()
        val component = testMainActivity.mediaActivityCompatComponent
        folderListFragment = FolderListFragment.newInstance(MediaItemType.FOLDERS, "id", component)
        super.setup(folderListFragment, FolderListFragment::class.java)
    }

    @Test
    fun testItemSelected() {
        val action = FragmentAction<FolderListFragment?> { fragment: FolderListFragment? -> itemSelected(fragment) }
        performAction(action)
    }

    private fun itemSelected(fragment: MediaItemListFragment?) {
        val spiedFragment = Mockito.spy(fragment)
        val id = "ID"
        val title = "TITLE"
        val description = "description"
        val mediaItem: MediaBrowserCompat.MediaItem = TestUtils.createMediaItem(id, title, description, MediaItemType.ROOT)
        spiedFragment!!.itemSelected(mediaItem)
        Shadows.shadowOf(Looper.getMainLooper()).idle()
        Mockito.verify(spiedFragment)!!.startActivity(ArgumentMatchers.any())
    }

    companion object {
        private const val FRAGMENT_TAG = "FragmentScenario_Fragment_Tag"
    }
}