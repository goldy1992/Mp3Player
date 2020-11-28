package com.github.goldy1992.mp3player.client.views.fragments.viewpager

import android.os.Looper
import com.github.goldy1992.mp3player.client.activities.HiltTestActivity
import com.github.goldy1992.mp3player.client.dagger.modules.GlideModule
import com.github.goldy1992.mp3player.client.views.fragments.FragmentTestBase
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.verify
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.*
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.LooperMode

@RunWith(RobolectricTestRunner::class)
@HiltAndroidTest
@UninstallModules(GlideModule::class)
@LooperMode(LooperMode.Mode.PAUSED)
@Ignore
class FolderListFragmentTest : FragmentTestBase<FolderListFragment>() {

    private var folderListFragment: FolderListFragment? = null

    @Rule
    @JvmField
    val rule : HiltAndroidRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        rule.inject()
        activityScenario = Robolectric.buildActivity(HiltTestActivity::class.java)
                .setup()
    //    val testMainActivity: HiltTestActivity = activityScenario!!.get()
        folderListFragment = FolderListFragment.newInstance(MediaItemType.FOLDERS, "id")
        super.setup(folderListFragment, FolderListFragment::class.java)
    }

    @Test
    fun firstTest() {
        Assert.assertTrue(true)
    }

/*    @Test
    fun testItemSelected() {
        val action = FragmentAction<FolderListFragment> { fragment: FolderListFragment? -> itemSelected(fragment) }
        performAction(action)
    }
*/
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