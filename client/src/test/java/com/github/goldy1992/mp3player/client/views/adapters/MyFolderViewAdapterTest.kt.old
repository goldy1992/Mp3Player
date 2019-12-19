package com.github.goldy1992.mp3player.client.views.adapters

import android.support.v4.media.MediaBrowserCompat
import com.github.goldy1992.mp3player.client.views.viewholders.MyFolderViewHolder
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MyFolderViewAdapterTest : MediaItemRecyclerViewAdapterTestBase() {
    private var myFolderViewAdapter: MyFolderViewAdapter = mock<MyFolderViewAdapter>()
    private val myFolderViewHolder: MyFolderViewHolder = mock<MyFolderViewHolder>()

    @Before
    override fun setup() {
        super.setup()
        myFolderViewAdapter = MyFolderViewAdapter(albumArtPainter!!)
    }

    @Test
    fun testOnCreateViewHolder() {
        val result = myFolderViewAdapter!!.onCreateViewHolder(viewGroup!!, 0) as MyFolderViewHolder
        Assert.assertNotNull(result)
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
        myFolderViewAdapter!!.notifyDataSetChanged()
        myFolderViewAdapter!!.items = mediaItems
        argumentCaptor<MediaBrowserCompat.MediaItem>().apply {
            myFolderViewAdapter!!.onBindViewHolder(myFolderViewHolder, 0)
            verify(myFolderViewHolder, times(1)).bindMediaItem(capture())
            val result = allValues[0]
            Assert.assertEquals(directoryName, MediaItemUtils.getTitle(result))
            Assert.assertEquals(directoryPath, MediaItemUtils.getMediaId(result))
            Assert.assertEquals(directoryPath, MediaItemUtils.getDescription(result))
        }
    }
}