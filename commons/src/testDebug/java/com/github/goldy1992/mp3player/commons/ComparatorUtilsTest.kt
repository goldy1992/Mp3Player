package com.github.goldy1992.mp3player.commons

import junit.framework.TestCase.assertTrue
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
        var result: Int = ComparatorUtils.Companion.compareRootMediaItemsByMediaItemType.compare(SONGS_ROOT, FOLDERS_ROOT)
        val albumsGreaterThanZero = result < 0
        Assert.assertTrue(albumsGreaterThanZero)
        result = ComparatorUtils.Companion.compareRootMediaItemsByMediaItemType.compare(FOLDERS_ROOT, SONGS_ROOT)
        val songsLessThanAlbums = result > 0
        Assert.assertTrue(songsLessThanAlbums)
    }

    /**
     *
     */
    @Test
    fun testCompareRootItemsByCategoryNullAgainstCategory() {
        var result: Int = ComparatorUtils.Companion.compareRootMediaItemsByMediaItemType.compare(SONGS_ROOT, null)
        val songsGreaterThanNull = result > 0
        Assert.assertTrue(songsGreaterThanNull)
        result = ComparatorUtils.Companion.compareRootMediaItemsByMediaItemType.compare(null, SONGS_ROOT)
        val nullLessThanSongs = result < 0
        Assert.assertTrue(nullLessThanSongs)
    }

    /**
     *
     */
    @Test
    fun testCompareRootItemsByCategoryNullAgainstNull() {
        val firstNullCategory = MediaItemBuilder(
            mediaId = MISC_STRING
        ).build()
        
        val secondNullCategory = MediaItemBuilder(
            mediaId = MISC_STRING
        ).build()
        
        val result: Int = ComparatorUtils.Companion.compareRootMediaItemsByMediaItemType.compare(firstNullCategory, secondNullCategory)
        val equal = result == 0
        Assert.assertTrue(equal)
    }

    /**
     *
     */
    @Test
    fun testCompareRootItemsEqual() {
        val songsRootCopy = MediaItemBuilder(
            mediaId = "id",
            rootMediaItemType = MediaItemType.SONGS,
            mediaItemType = MediaItemType.ROOT
        ).build()
        
        val result: Int = ComparatorUtils.Companion.compareRootMediaItemsByMediaItemType.compare(SONGS_ROOT, songsRootCopy)
        val songsGreaterThanNull = result == 0
        Assert.assertTrue(songsGreaterThanNull)
    }

    /**
     *
     */
    @Test
    fun testCompareMediaItemByTitleGreaterAndLessThan() {
        val greaterTitleMediaItem = MediaItemBuilder(
            mediaId = MISC_STRING,
            title = GREATER_STRING,
            description = MISC_STRING,
            mediaItemType = MediaItemType.ROOT
        ).build()
        
        val lesserTitleMediaItem = MediaItemBuilder(
            mediaId = MISC_STRING,
            title = LESSER_STRING,
            description = MISC_STRING,
            mediaItemType = MediaItemType.ROOT
        ).build()
        
        var result: Int = ComparatorUtils.Companion.compareMediaItemsByTitle.compare(greaterTitleMediaItem, lesserTitleMediaItem)
        Assert.assertTrue(result > 0)
        result = ComparatorUtils.Companion.compareMediaItemsByTitle.compare(lesserTitleMediaItem, greaterTitleMediaItem)
        Assert.assertTrue(result < 0)
    }

    /**
     *
     */
    @Test
    fun testCompareMediaItemByTitleAgainstNull() {
        val greaterTitleMediaItem = MediaItemBuilder(
            mediaId = MISC_STRING,
            title = GREATER_STRING,
            description =  MISC_STRING,
            mediaItemType = MediaItemType.ROOT
        ).build()
        
        val nullTitleMediaItem = MediaItemBuilder(
            mediaId = MISC_STRING,
            title = null,
            description = MISC_STRING,
            mediaItemType = MediaItemType.ROOT
        ) .build()
        var result: Int = ComparatorUtils.Companion.compareMediaItemsByTitle.compare(greaterTitleMediaItem, nullTitleMediaItem)
        Assert.assertTrue(result > 0)
        result = ComparatorUtils.Companion.compareMediaItemsByTitle.compare(nullTitleMediaItem, greaterTitleMediaItem)
        Assert.assertTrue(result < 0)
    }

    /**
     *
     */
    @Test
    fun testCompareMediaItemByTitleNullAgainstNull() {
        val firstNullTitleMediaItem = MediaItemBuilder(
            mediaId = MISC_STRING,
            title = null,
            description = MISC_STRING,
            mediaItemType = MediaItemType.ROOT
        ).build()
        
        val secondNullTitleMediaItem = MediaItemBuilder(
            mediaId = MISC_STRING,
            title = null,
            description = MISC_STRING,
            mediaItemType = MediaItemType.ROOT
        ).build()
        
        val result: Int = ComparatorUtils.Companion.compareMediaItemsByTitle.compare(firstNullTitleMediaItem, secondNullTitleMediaItem)
        val equal = result == 0
        Assert.assertTrue(equal)
    }

    /**
     *
     */
    @Test
    fun testCompareMediaItemByTitleEqual() {
        val mediaItem = MediaItemBuilder(
            mediaId = MISC_STRING,
            title = MISC_STRING,
            description = MISC_STRING,
            mediaItemType = MediaItemType.ROOT
        ).build()

        val mediaItemSameId = MediaItemBuilder(
            mediaId = MISC_STRING,
            title = MISC_STRING,
            description = MISC_STRING,
            mediaItemType = MediaItemType.ROOT
        ).build()

        val result: Int = ComparatorUtils.Companion.compareMediaItemById.compare(mediaItem, mediaItemSameId)
        val equal = result == 0
        Assert.assertTrue(equal)
    }

    /**
     *
     */
    @Test
    fun testCompareMediaItemByIdGreaterAndLessThan() {
        val greaterTitleMediaItem = MediaItemBuilder(
            mediaId = GREATER_STRING,
            title = MISC_STRING,
            description = MISC_STRING,
            mediaItemType = MediaItemType.ROOT
        ).build()

        val lesserTitleMediaItem = MediaItemBuilder(
            mediaId =  LESSER_STRING,
            title = MISC_STRING,
            description = MISC_STRING,
            mediaItemType = MediaItemType.ROOT
        ).build()

        var result: Int = ComparatorUtils.Companion.compareMediaItemById.compare(greaterTitleMediaItem, lesserTitleMediaItem)
        Assert.assertTrue(result > 0)
        result = ComparatorUtils.Companion.compareMediaItemById.compare(lesserTitleMediaItem, greaterTitleMediaItem)
        Assert.assertTrue(result < 0)
    }

    /**
     *
     */
    @Test
    fun testCompareMediaItemByIdEqualIds() {
        val mediaItem = MediaItemBuilder(
            mediaId = MISC_STRING,
            title = GREATER_STRING,
            description = MISC_STRING,
            mediaItemType = MediaItemType.ROOT
        ).build()
        val mediaItemSameId = MediaItemBuilder(
            mediaId = MISC_STRING,
            title =LESSER_STRING,
            description = MISC_STRING,
            mediaItemType = MediaItemType.ROOT
        ).build()
        
        val result: Int = ComparatorUtils.Companion.compareMediaItemById.compare(mediaItem, mediaItemSameId)
        assertTrue(result == 0)
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
        private val SONGS_ROOT = MediaItemBuilder(
            mediaId  ="id",
            rootMediaItemType = MediaItemType.SONGS,
            mediaItemType = MediaItemType.ROOT
        ).build()
        /**
         * FOLDERS_ROOT
         */
        private val FOLDERS_ROOT = MediaItemBuilder(
            mediaId = "id",
            rootMediaItemType = MediaItemType.FOLDERS,
            mediaItemType = MediaItemType.ROOT
        ).build()
    }
}