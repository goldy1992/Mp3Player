package com.github.goldy1992.mp3player.client.views.fragments.viewpager

import android.os.Looper
import androidx.fragment.app.testing.FragmentScenario.FragmentAction
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.activities.HiltTestActivity
import com.github.goldy1992.mp3player.client.activities.MainActivity
import com.github.goldy1992.mp3player.client.dagger.modules.GlideModule
import com.github.goldy1992.mp3player.client.dagger.modules.MediaBrowserAdapterModule
import com.github.goldy1992.mp3player.client.dagger.modules.MediaControllerAdapterModule
import com.github.goldy1992.mp3player.client.views.fragments.FragmentTestBase
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.LooperMode

@RunWith(RobolectricTestRunner::class)
@HiltAndroidTest
@UninstallModules(GlideModule::class,
        MediaBrowserAdapterModule::class,
        MediaControllerAdapterModule::class)
@LooperMode(LooperMode.Mode.PAUSED)
class SongListFragmentTest : FragmentTestBase<SongListFragment>() {
    private var folderListFragment: SongListFragment? = null

    @Rule
    @JvmField
    val rule : HiltAndroidRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        activityScenario = Robolectric.buildActivity(HiltTestActivity::class.java).setup()
        val testMainActivity: HiltTestActivity = activityScenario!!.get()
        folderListFragment = SongListFragment.newInstance(MediaItemType.SONGS, "id")
        super.setup(folderListFragment, SongListFragment::class.java)
    }

    @Test
    fun testItemSelected() {
        val action = FragmentAction<SongListFragment> { fragment: SongListFragment? -> itemSelected(fragment) }
        performAction(action)
    }

    private fun itemSelected(fragment: MediaItemListFragment?) {
        val mediaControllerAdapter = mock<MediaControllerAdapter>()
        fragment!!.mediaControllerAdapter = mediaControllerAdapter
        val expectedLibraryId = "ID"
        val mediaItem = MediaItemBuilder("id")
                .setLibraryId(expectedLibraryId)
                .build()
        fragment.itemSelected(mediaItem)
        Shadows.shadowOf(Looper.getMainLooper()).idle()
        verify(mediaControllerAdapter).playFromMediaId(expectedLibraryId, null)
    }
}