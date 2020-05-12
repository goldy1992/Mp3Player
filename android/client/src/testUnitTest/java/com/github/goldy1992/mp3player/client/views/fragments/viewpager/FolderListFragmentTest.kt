package com.github.goldy1992.mp3player.client.views.fragments.viewpager

import android.os.Looper
import androidx.fragment.app.testing.FragmentScenario.FragmentAction
import com.github.goldy1992.mp3player.client.activities.MainActivity
import com.github.goldy1992.mp3player.client.views.fragments.FragmentTestBase
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.LooperMode

@RunWith(RobolectricTestRunner::class)
@LooperMode(LooperMode.Mode.PAUSED)
class FolderListFragmentTest : FragmentTestBase<FolderListFragment>() {

    private var folderListFragment: FolderListFragment? = null

    @Before
    fun setup() {
        activityScenario = Robolectric.buildActivity(MainActivity::class.java)
                .setup()
        val testMainActivity: MainActivity = activityScenario!!.get()
        val component = testMainActivity.mediaActivityCompatComponent
        folderListFragment = FolderListFragment.newInstance(MediaItemType.FOLDERS, "id")
        super.setup(folderListFragment, FolderListFragment::class.java)
    }

    @Test
    fun testItemSelected() {
        val action = FragmentAction<FolderListFragment> { fragment: FolderListFragment? -> itemSelected(fragment) }
        performAction(action)
    }

    private fun itemSelected(fragment: MediaItemListFragment?) {
        val spiedFragment = spy(fragment)

        val id = "ID"
        val title = "TITLE"
        val description = "description"
        val mediaItem = MediaItemBuilder(id)
                 .setTitle(title)
                 .setDescription(description)
                 .build()
        spiedFragment!!.itemSelected(mediaItem)
        Shadows.shadowOf(Looper.getMainLooper()).idle()
        verify(spiedFragment).startActivity(any())
    }

    companion object {
        private const val FRAGMENT_TAG = "FragmentScenario_Fragment_Tag"
    }
}