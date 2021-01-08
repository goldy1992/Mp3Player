package com.github.goldy1992.mp3player.client.views.adapters

import android.support.v4.media.MediaBrowserCompat.MediaItem
import androidx.recyclerview.widget.RecyclerView
import androidx.test.platform.app.InstrumentationRegistry
import com.github.goldy1992.mp3player.client.views.viewholders.MyFolderViewHolder
import com.github.goldy1992.mp3player.client.views.viewholders.MySongViewHolder
import com.github.goldy1992.mp3player.client.views.viewholders.NoSearchResultsViewHolder
import com.github.goldy1992.mp3player.client.views.viewholders.RootItemViewHolder
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SearchResultAdapterTest : MediaItemRecyclerViewAdapterTestBase() {

    private var searchResultAdapter: SearchResultAdapterList? = null

    @Before
    override fun setup() {
        super.setup()
        context = InstrumentationRegistry.getInstrumentation().context
        searchResultAdapter = SearchResultAdapterList(albumArtPainter)
    }

    /**
     * Test creation of RootItemViewHolder
     */
    @Test
    fun testOnCreateViewHolderRootItem() {
        assertCreatedViewItem(RootItemViewHolder::class.java, MediaItemType.ROOT.value)
    }

    /**
     * Test creation of SongItemViewHolder
     */
    @Test
    fun testOnFolderViewHolderSongItem() {
        assertCreatedViewItem(MySongViewHolder::class.java, MediaItemType.SONG.value)
    }

    @Test
    fun testEmptyItemReturnsNoSearchResultsViewHolder() {
        assertCreatedViewItem(NoSearchResultsViewHolder::class.java, -1)
    }

    /**
     * Test creation of FolderItemViewHolder
     */
    @Test
    fun testOnFolderViewHolderFolderItem() {
        assertCreatedViewItem(MyFolderViewHolder::class.java, MediaItemType.FOLDER.value)
    }

    /**  */
    @Test
    fun testBindViewHolder() {
        val mySongViewHolder = mock<MySongViewHolder>()
        mediaItems.add(
                MediaItemBuilder("101")
                        .build()
        )
        searchResultAdapter!!.submitList(mediaItems)
        searchResultAdapter!!.onBindViewHolder(mySongViewHolder, 0)
    }

    @Test
    fun testGetItemViewType() {
        val mediaItemType = MediaItemType.FOLDER
        val mediaItem = MediaItemBuilder("id")
                .setMediaItemType(mediaItemType)
                .build()
        searchResultAdapter!!.submitList(arrayListOf(mediaItem)) // add as the first item
        val result = searchResultAdapter!!.getItemViewType(0)
        Assert.assertEquals(mediaItemType.value.toLong(), result.toLong())
    }

    @Test
    fun testGetItemViewTypeNoMediaItemType() {
        val mediaItem = MediaItemBuilder("id")
                .build()
        searchResultAdapter!!.submitList(arrayListOf(mediaItem)) // add as the first item
        val result = searchResultAdapter!!.getItemViewType(0)
        Assert.assertEquals(0, result.toLong())
    }

    @Test
    fun testItemCount() {
        val mediaItem = mock<MediaItem>()
        val expectedSize = 5
        for (i in 1..5) {
            mediaItems.add(mediaItem)
        }
        searchResultAdapter!!.submitList(mediaItems)
        Assert.assertEquals(expectedSize.toLong(), searchResultAdapter!!.itemCount.toLong())
    }

    /**
     *
     * @param viewHolderType the view holder class type
     * @param viewType the view type code
     * @param <T> The type of ViewHolder
    </T> */
    private fun <T : RecyclerView.ViewHolder?> assertCreatedViewItem(viewHolderType: Class<T>, viewType: Int) {
        val viewHolder: RecyclerView.ViewHolder = searchResultAdapter!!.onCreateViewHolder(viewGroup, viewType)
        Assert.assertEquals(viewHolder.javaClass, viewHolderType)
    }
}