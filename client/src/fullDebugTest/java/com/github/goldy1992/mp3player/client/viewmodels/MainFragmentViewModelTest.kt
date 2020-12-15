package com.github.goldy1992.mp3player.client.viewmodels

import android.os.Looper
import android.support.v4.media.MediaBrowserCompat
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemType.*
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows

/**
 * Test class for [MainFragmentViewModel]
 */
@RunWith(RobolectricTestRunner::class)
class MainFragmentViewModelTest {

    private val viewModel : MainFragmentViewModel = MainFragmentViewModel()

    @Test
    fun testOnChildrenLoadedForRootCategory() {
        val parentId = "parentId"
        val children = ArrayList<MediaBrowserCompat.MediaItem>()
        val rootItemsSet: Set<MediaItemType> = MediaItemType.PARENT_TO_CHILD_MAP[ROOT]!!
        for (category in rootItemsSet) {
            val mediaItem = MediaItemBuilder("id1")
                    .setTitle(category.title)
                    .setDescription(category.description)
                    .setRootItemType(category)
                    .setMediaItemType(ROOT)
                    .build()
            children.add(mediaItem)
        }
        val expectedNumOfFragmentsCreated = rootItemsSet.size
        viewModel.onChildrenLoaded(parentId, children)
        Shadows.shadowOf(Looper.getMainLooper()).idle()
        val numberOfChildFragments = viewModel.menuCategories.value!!.size
        assertEquals(expectedNumOfFragmentsCreated.toLong(), numberOfChildFragments.toLong())
    }
}