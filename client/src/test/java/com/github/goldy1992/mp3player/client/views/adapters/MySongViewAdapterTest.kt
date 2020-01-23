package com.github.goldy1992.mp3player.client.views.adapters

import android.support.v4.media.MediaBrowserCompat

import com.github.goldy1992.mp3player.client.views.viewholders.MySongViewHolder
import com.github.goldy1992.mp3player.commons.*
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
class MySongViewAdapterTest : MediaItemRecyclerViewAdapterTestBase() {
    private var mySongViewAdapter: MySongViewAdapter? = null

    private val mySongViewHolder: MySongViewHolder =  mock<MySongViewHolder>()

    @Before
    override fun setup() {
        super.setup()
        mySongViewAdapter = MySongViewAdapter(albumArtPainter!!)
    }

    @Test
    fun testOnCreateViewHolder() {
        val result = mySongViewAdapter!!.onCreateViewHolder(viewGroup!!, 0) as MySongViewHolder
        Assert.assertNotNull(result)
    }

    @Test
    fun testOnBindViewHolderEmptyValues() {
        val expectedArtist = Constants.UNKNOWN
        val expectedTitle = ""
        mediaItems.add(
                MediaItemBuilder("101")
                        .setDescription("description1")
                        .setMediaItemType(MediaItemType.SONG)
                        .setDuration(45646L)
                        .setTitle(expectedTitle)
                        .setArtist(expectedArtist)
                        .build()
        )
        mySongViewAdapter!!.notifyDataSetChanged()
        argumentCaptor<MediaBrowserCompat.MediaItem>().apply {
            bindViewHolder()
            verify(mySongViewHolder, times(1))!!.bindMediaItem(capture())
            val result = firstValue
            Assert.assertEquals(expectedArtist, MediaItemUtils.getArtist(result))
            Assert.assertEquals(expectedTitle, MediaItemUtils.getTitle(result))
        }
    }

    @Test
    fun testBindNullTitleUseFileName() {
        val fileName = "FILE_NAME"
        val extension = ".mp3"
        val fullFileName = fileName + extension
        val mediaItem: MediaBrowserCompat.MediaItem = MediaItemBuilder("ID")
                .setTitle(null)
                .setDescription("description")
                .setMediaItemType(MediaItemType.ROOT)
                .setDuration(32525L)
                .build()
        mediaItem.description.extras!!.putString(MetaDataKeys.META_DATA_KEY_FILE_NAME, fullFileName)
        mediaItems.add(mediaItem)
        argumentCaptor<MediaBrowserCompat.MediaItem>().apply {
            bindViewHolder()
            verify(mySongViewHolder, times(1))!!.bindMediaItem(capture())
            val result = firstValue
            Assert.assertNull(MediaItemUtils.getTitle(result))
        }
    }

    @Test
    fun testOnBindViewHolder() { // TODO: refactor to have an OnBindViewHolder setup method and test for different list indices
        val expectedArtist = "artist"
        val originalDuration = 34234L
        //        final String expectedDuration = TimerUtils.formatTime(Long.valueOf(originalDuration));
        val expectedTitle = "title"
        mediaItems.add(
                MediaItemBuilder("101")
                        .setTitle(expectedTitle)
                        .setDescription("description1")
                        .setMediaItemType(MediaItemType.ROOT)
                        .setDuration(originalDuration)
                        .setArtist(expectedArtist)
                        .build())
        mediaItems.add(
                MediaItemBuilder("102")
                        .setTitle("title2")
                        .setDescription("description2")
                        .setMediaItemType(MediaItemType.ROOT)
                        .setDuration(3424L)
                        .build())
        argumentCaptor<MediaBrowserCompat.MediaItem>().apply {
            bindViewHolder()
            verify(mySongViewHolder, times(1))!!.bindMediaItem(capture ())
            val result = firstValue
            Assert.assertEquals(expectedArtist, MediaItemUtils.getArtist(result))
            Assert.assertEquals(expectedTitle, MediaItemUtils.getTitle(result))
            Assert.assertEquals(originalDuration, MediaItemUtils.getDuration(result))
        }
    }

    private fun bindViewHolder() {
        mySongViewAdapter!!.items = mediaItems
        mySongViewAdapter!!.onBindViewHolder(mySongViewHolder!!, 0)
    }
}