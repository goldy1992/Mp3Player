package com.github.goldy1992.mp3player.commons

import android.support.v4.media.MediaBrowserCompat
import com.github.goldy1992.mp3player.commons.MediaItemType
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ComparatorUtilsTest {
    /**
     *
     */
    @Test
    fun testCompareRootItemsByCategoryGreaterAndLessThan() {
        var result: Int = ComparatorUtils.compareRootMediaItemsByMediaItemType.compare(SONGS_ROOT, FOLDERS_ROOT)
        val albumsGreaterThanZero = result < 0
        Assert.assertTrue(albumsGreaterThanZero)
        result = ComparatorUtils.compareRootMediaItemsByMediaItemType.compare(FOLDERS_ROOT, SONGS_ROOT)
        val songsLessThanAlbums = result > 0
        Assert.assertTrue(songsLessThanAlbums)
    }

    /**
     *
     */
    @Test
    fun testCompareRootItemsByCategoryNullAgainstCategory() {
        var result: Int = ComparatorUtils.compareRootMediaItemsByMediaItemType.compare(SONGS_ROOT, null)
        val songsGreaterThanNull = result > 0
        Assert.assertTrue(songsGreaterThanNull)
        result = ComparatorUtils.compareRootMediaItemsByMediaItemType.compare(null, SONGS_ROOT)
        val nullLessThanSongs = result < 0
        Assert.assertTrue(nullLessThanSongs)
    }

    /**
     *
     */
    @Test
    fun testCompareRootItemsByCategoryNullAgainstNull() {
        val firstNullCategory = MediaItemBuilder(MISC_STRING)
                .setRootItemType(null)
                .build()
        val secondNullCategory = MediaItemBuilder(MISC_STRING)
                .setRootItemType(null)
                .build()
        val result: Int = ComparatorUtils.compareRootMediaItemsByMediaItemType.compare(firstNullCategory, secondNullCategory)
        val equal = result == 0
        Assert.assertTrue(equal)
    }

    /**
     *
     */
    @Test
    fun testCompareRootItemsEqual() {
        val songsRootCopy = MediaItemBuilder("id")
                .setRootItemType(MediaItemType.SONGS)
                .setMediaItemType(MediaItemType.ROOT)
                .build()
        val result: Int = ComparatorUtils.compareRootMediaItemsByMediaItemType.compare(SONGS_ROOT, songsRootCopy)
        val songsGreaterThanNull = result == 0
        Assert.assertTrue(songsGreaterThanNull)
    }

    /**
     *
     */
    @Test
    fun testCompareMediaItemByTitleGreaterAndLessThan() {
        val greaterTitleMediaItem = MediaItemBuilder(MISC_STRING)
                .setTitle(GREATER_STRING)
                .setDescription(MISC_STRING)
                .setMediaItemType(MediaItemType.ROOT)
                .build()
        val lesserTitleMediaItem = MediaItemBuilder(MISC_STRING)
                .setTitle(LESSER_STRING)
                .setDescription(MISC_STRING)
                .setMediaItemType(MediaItemType.ROOT)
                .build()
        var result: Int = ComparatorUtils.compareMediaItemsByTitle.compare(greaterTitleMediaItem, lesserTitleMediaItem)
        Assert.assertTrue(result > 0)
        result = ComparatorUtils.compareMediaItemsByTitle.compare(lesserTitleMediaItem, greaterTitleMediaItem)
        Assert.assertTrue(result < 0)
    }

    /**
     *
     */
    @Test
    fun testCompareMediaItemByTitleAgainstNull() {
        val greaterTitleMediaItem = MediaItemBuilder(MISC_STRING)
                .setTitle(GREATER_STRING)
                .setDescription(MISC_STRING)
                .setMediaItemType(MediaItemType.ROOT)
                .build()
        val nullTitleMediaItem = MediaItemBuilder(MISC_STRING)
                .setTitle(null)
                .setDescription(MISC_STRING)
                .setMediaItemType(MediaItemType.ROOT)
                .build()
        var result: Int = ComparatorUtils.compareMediaItemsByTitle.compare(greaterTitleMediaItem, nullTitleMediaItem)
        Assert.assertTrue(result > 0)
        result = ComparatorUtils.compareMediaItemsByTitle.compare(nullTitleMediaItem, greaterTitleMediaItem)
        Assert.assertTrue(result < 0)
    }

    /**
     *
     */
    @Test
    fun testCompareMediaItemByTitleNullAgainstNull() {
        val firstNullTitleMediaItem = MediaItemBuilder(MISC_STRING)
                .setTitle(null)
                .setDescription(MISC_STRING)
                .setMediaItemType(MediaItemType.ROOT)
                .build()
        val secondNullTitleMediaItem = MediaItemBuilder(MISC_STRING)
                .setTitle(null)
                .setDescription(MISC_STRING)
                .setMediaItemType(MediaItemType.ROOT)
                .build()
        val result: Int = ComparatorUtils.compareMediaItemsByTitle.compare(firstNullTitleMediaItem, secondNullTitleMediaItem)
        val equal = result == 0
        Assert.assertTrue(equal)
    }

    /**
     *
     */
    @Test
    fun testCompareMediaItemByTitleEqual() {
        val mediaItem = MediaItemBuilder(MISC_STRING)
                .setTitle(MISC_STRING)
                .setDescription(MISC_STRING)
                .setMediaItemType(MediaItemType.ROOT)
                .build()
        val mediaItemSameId = MediaItemBuilder(MISC_STRING)
                .setTitle(MISC_STRING)
                .setDescription(MISC_STRING)
                .setMediaItemType(MediaItemType.ROOT)
                .build()
        val result: Int = ComparatorUtils.compareMediaItemById.compare(mediaItem, mediaItemSameId)
        val equal = result == 0
        Assert.assertTrue(equal)
    }

    /**
     *
     */
    @Test
    fun testCompareMediaItemByIdGreaterAndLessThan() {
        val greaterTitleMediaItem = MediaItemBuilder(GREATER_STRING)
                .setTitle(MISC_STRING)
                .setDescription(MISC_STRING)
                .setMediaItemType(MediaItemType.ROOT)
                .build()
        val lesserTitleMediaItem = MediaItemBuilder(LESSER_STRING)
                .setTitle(MISC_STRING)
                .setDescription(MISC_STRING)
                .setMediaItemType(MediaItemType.ROOT)
                .build()
        var result: Int = ComparatorUtils.compareMediaItemById.compare(greaterTitleMediaItem, lesserTitleMediaItem)
        Assert.assertTrue(result > 0)
        result = ComparatorUtils.compareMediaItemById.compare(lesserTitleMediaItem, greaterTitleMediaItem)
        Assert.assertTrue(result < 0)
    }

    /**
     *
     */
    @Test
    fun testCompareMediaItemByIdAgainstNull() {
        val nullMediaItem: MediaBrowserCompat.MediaItem? = null
        val mediaItem = MediaItemBuilder(LESSER_STRING).build()
        var result: Int = ComparatorUtils.compareMediaItemById.compare(nullMediaItem, mediaItem)
        Assert.assertTrue(result < 0)
        result = ComparatorUtils.compareMediaItemById.compare(mediaItem, nullMediaItem)
        Assert.assertTrue(result > 0)
    }

    /**
     *
     */
    @Test
    fun testCompareMediaItemByIdNullAgainstNull() {
        val result: Int = ComparatorUtils.compareMediaItemById.compare(null, null)
        val equal = result == 0
        Assert.assertTrue(equal)
    }

    /**
     *
     */
    @Test
    fun testCompareMediaItemByIdEqualIds() {
        val mediaItem = MediaItemBuilder(MISC_STRING)
                .setTitle(GREATER_STRING)
                .setDescription(MISC_STRING)
                .setMediaItemType(MediaItemType.ROOT)
                .build()
        val mediaItemSameId = MediaItemBuilder(MISC_STRING)
                .setTitle(LESSER_STRING)
                .setDescription(MISC_STRING)
                .setMediaItemType(MediaItemType.ROOT)
                .build()
        val result: Int = ComparatorUtils.compareMediaItemById.compare(mediaItem, mediaItemSameId)
        val EQUAL = result == 0
        Assert.assertTrue(EQUAL)
    }

    companion object {
        /** An id constant used for testing  */
        private const val MISC_STRING = "xyz"
        /** String to use for a greater than string  */
        private const val GREATER_STRING = "z_GREATER_STRING"
        /** String to use for a less than string  */
        private const val LESSER_STRING = "a_LESSER_STRING"
        /**
         * SONGS_ROOT
         */
        private val SONGS_ROOT = MediaItemBuilder("id")
                .setRootItemType(MediaItemType.SONGS)
                .setMediaItemType(MediaItemType.ROOT)
                .build()
        /**
         * FOLDERS_ROOT
         */
        private val FOLDERS_ROOT = MediaItemBuilder("id")
                .setRootItemType(MediaItemType.FOLDERS)
                .setMediaItemType(MediaItemType.ROOT)
                .build()
    }
}