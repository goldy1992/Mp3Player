package com.github.goldy1992.mp3player.commons;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static android.support.v4.media.MediaBrowserCompat.MediaItem;
import static com.github.goldy1992.mp3player.TestUtils.createMediaItem;
import static com.github.goldy1992.mp3player.TestUtils.createMediaItemWithNullCategory;
import static org.junit.Assert.assertTrue;


@RunWith(RobolectricTestRunner.class)
public class ComparatorUtilsTest {
    /** An id constant used for testing */
    private static final String MISC_STRING = "xyz";
    /** String to use for a greater than string */
    private static final String GREATER_STRING = "z_GREATER_STRING";
    /** String to use for a less than string */
    private static final String LESSER_STRING = "a_LESSER_STRING";
    /**
     * SONGS_ROOT
     */
    private static final MediaItem SONGS_ROOT = new MediaItemBuilder("id")
        .setRootItemType(MediaItemType.SONGS)
        .setMediaItemType(MediaItemType.ROOT)
        .build();
    /**
     * FOLDERS_ROOT
     */
    private static final MediaItem FOLDERS_ROOT = new MediaItemBuilder("id")
            .setRootItemType(MediaItemType.FOLDERS)
            .setMediaItemType(MediaItemType.ROOT)
            .build();    /**
     *
     */
    @Test
    public void testCompareRootItemsByCategoryGreaterAndLessThan() {
        int result = ComparatorUtils.compareRootMediaItemsByMediaItemType.compare(SONGS_ROOT, FOLDERS_ROOT);
        final boolean albumsGreaterThanZero = result < 0;
        assertTrue(albumsGreaterThanZero);
        result = ComparatorUtils.compareRootMediaItemsByMediaItemType.compare(FOLDERS_ROOT, SONGS_ROOT);
        final boolean songsLessThanAlbums = result > 0;
        assertTrue(songsLessThanAlbums);
    }
    /**
     *
     */
    @Test
    public void testCompareRootItemsByCategoryNullAgainstCategory() {
        int result = ComparatorUtils.compareRootMediaItemsByMediaItemType.compare(SONGS_ROOT, null);
        final boolean songsGreaterThanNull = result > 0;
        assertTrue(songsGreaterThanNull);
        result = ComparatorUtils.compareRootMediaItemsByMediaItemType.compare(null, SONGS_ROOT);
        final boolean nullLessThanSongs = result < 0;
        assertTrue(nullLessThanSongs);
    }
    /**
     *
     */
    @Test
    public void testCompareRootItemsByCategoryNullAgainstNull() {
        final MediaItem firstNullCategory = createMediaItemWithNullCategory();
        final MediaItem secondNullCategory = createMediaItemWithNullCategory();
        final int result = ComparatorUtils.compareRootMediaItemsByMediaItemType.compare(firstNullCategory, secondNullCategory);
        final boolean equal = result == 0;
        assertTrue(equal);
    }
    /**
     *
     */
    @Test
    public void testCompareRootItemsEqual() {
        final MediaItem songsRootCopy = new MediaItemBuilder("id")
                .setRootItemType(MediaItemType.SONGS)
                .setMediaItemType(MediaItemType.ROOT)
                .build();
        final int result = ComparatorUtils.compareRootMediaItemsByMediaItemType.compare(SONGS_ROOT, songsRootCopy);
        final boolean songsGreaterThanNull = result == 0;
        assertTrue(songsGreaterThanNull);
    }
    /**
     * 
     */
    @Test
    public void testCompareMediaItemByTitleGreaterAndLessThan() {
        final MediaItem greaterTitleMediaItem = createMediaItem(MISC_STRING, GREATER_STRING, MISC_STRING, MediaItemType.ROOT);
        final MediaItem lesserTitleMediaItem = createMediaItem(MISC_STRING, LESSER_STRING, MISC_STRING, MediaItemType.ROOT);
        int result = ComparatorUtils.compareMediaItemsByTitle.compare(greaterTitleMediaItem, lesserTitleMediaItem);
        assertTrue( result > 0);
        result = ComparatorUtils.compareMediaItemsByTitle.compare(lesserTitleMediaItem, greaterTitleMediaItem);
        assertTrue( result < 0);
    }
    /**
     *
     */
    @Test
    public void testCompareMediaItemByTitleAgainstNull() {
        final MediaItem greaterTitleMediaItem = createMediaItem(MISC_STRING, GREATER_STRING, MISC_STRING, MediaItemType.ROOT);
        final MediaItem nullTitleMediaItem = createMediaItem(MISC_STRING, null, MISC_STRING, MediaItemType.ROOT);
        int result = ComparatorUtils.compareMediaItemsByTitle.compare(greaterTitleMediaItem, nullTitleMediaItem);
        assertTrue( result > 0);
        result = ComparatorUtils.compareMediaItemsByTitle.compare(nullTitleMediaItem, greaterTitleMediaItem);
        assertTrue( result < 0);
    }
    /**
     *
     */
    @Test
    public void testCompareMediaItemByTitleNullAgainstNull() {
        final MediaItem firstNullTitleMediaItem = createMediaItem(MISC_STRING, null, MISC_STRING, MediaItemType.ROOT);
        final MediaItem secondNullTitleMediaItem = createMediaItem(MISC_STRING, null, MISC_STRING, MediaItemType.ROOT);
        int result = ComparatorUtils.compareMediaItemsByTitle.compare(firstNullTitleMediaItem, secondNullTitleMediaItem);
        boolean equal = result == 0;
        assertTrue(equal);

    }
    /**
     *
     */
    @Test
    public void testCompareMediaItemByTitleEqual() {
        final MediaItem mediaItem = createMediaItem(MISC_STRING, MISC_STRING, MISC_STRING, MediaItemType.ROOT);
        final MediaItem mediaItemSameId = createMediaItem(MISC_STRING, MISC_STRING, MISC_STRING, MediaItemType.ROOT);
        final int result = ComparatorUtils.compareMediaItemById.compare(mediaItem, mediaItemSameId);
        final boolean equal = result == 0;
        assertTrue(equal);
    }
    /**
     *
     */
    @Test
    public void testCompareMediaItemByIdGreaterAndLessThan() {
        final MediaItem greaterTitleMediaItem = createMediaItem(GREATER_STRING, MISC_STRING, MISC_STRING, MediaItemType.ROOT);
        final MediaItem lesserTitleMediaItem = createMediaItem(LESSER_STRING, MISC_STRING, MISC_STRING, MediaItemType.ROOT);
        int result = ComparatorUtils.compareMediaItemById.compare(greaterTitleMediaItem, lesserTitleMediaItem);
        assertTrue( result > 0);
        result = ComparatorUtils.compareMediaItemById.compare(lesserTitleMediaItem, greaterTitleMediaItem);
        assertTrue( result < 0);
    }
    /**
     *
     */
    @Test
    public void testCompareMediaItemByIdAgainstNull() {

        final MediaItem nullMediaItem = null;
        final MediaItem mediaItem = new MediaItemBuilder(LESSER_STRING).build();
        int result = ComparatorUtils.compareMediaItemById.compare(nullMediaItem, mediaItem);
        assertTrue( result < 0);
        result = ComparatorUtils.compareMediaItemById.compare(mediaItem, nullMediaItem);
        assertTrue( result > 0);
    }
    /**
     *
     */
    @Test
    public void testCompareMediaItemByIdNullAgainstNull() {
        int result = ComparatorUtils.compareMediaItemById.compare(null, null);
        final boolean equal = result == 0;
        assertTrue( equal);
    }
    /**
     *
     */
    @Test
    public void testCompareMediaItemByIdEqualIds() {
        final MediaItem mediaItem = createMediaItem(MISC_STRING, GREATER_STRING, MISC_STRING, MediaItemType.ROOT);
        final MediaItem mediaItemSameId = createMediaItem(MISC_STRING, LESSER_STRING, MISC_STRING, MediaItemType.ROOT);
        final int result = ComparatorUtils.compareMediaItemById.compare(mediaItem, mediaItemSameId);
        final boolean EQUAL = result == 0;
        assertTrue(EQUAL);
    }

}