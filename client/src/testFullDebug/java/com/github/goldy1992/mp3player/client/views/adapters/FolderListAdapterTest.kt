package com.github.goldy1992.mp3player.client.views.adapters

import android.support.v4.media.MediaBrowserCompat
import androidx.recyclerview.widget.RecyclerView
import com.github.goldy1992.mp3player.client.views.viewholders.MyFolderViewHolder
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.junit.Assert
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class FolderListAdapterTest : MediaItemRecyclerViewAdapterTestBase() {

    private lateinit var myFolderViewAdapter: FolderListAdapter
    private val myFolderViewHolder: MyFolderViewHolder = mock<MyFolderViewHolder>()

    @Before
    override fun setup() {
        super.setup()
        myFolderViewAdapter = FolderListAdapter(albumArtPainter)
    }

    /**
     * GIVEN: A [MediaItemType] of [MediaItemType.FOLDER].
     * WHEN: A [RecyclerView.ViewHolder] is created using [FolderListAdapter.onCreateViewHolder].
     * THEN: A [MyFolderViewHolder] is created.
     */
    @Test
    fun testOnCreateViewHolder() {
        val result = myFolderViewAdapter.onCreateViewHolder(viewGroup, MediaItemType.FOLDER.value)
        assertTrue(result is MyFolderViewHolder)
    }

    @Test
    fun testOnBindViewHolder() {
        val directoryPath = "/a/b/c"
        val directoryName = "c"
        mediaItems.add(MediaItemBuilder(directoryPath)
                .setTitle(directoryName)
                .setDescription(directoryPath)
                .build()
        )
        myFolderViewAdapter.notifyDataSetChanged()
        myFolderViewAdapter.submitList(mediaItems)
        argumentCaptor<MediaBrowserCompat.MediaItem>().apply {
            myFolderViewAdapter.onBindViewHolder(myFolderViewHolder, 0)
            verify(myFolderViewHolder, times(1)).bindMediaItem(capture())
            val result = allValues[0]
            Assert.assertEquals(directoryName, MediaItemUtils.getTitle(result))
            Assert.assertEquals(directoryPath, MediaItemUtils.getMediaId(result))
            Assert.assertEquals(directoryPath, MediaItemUtils.getDescription(result))
        }
    }
}