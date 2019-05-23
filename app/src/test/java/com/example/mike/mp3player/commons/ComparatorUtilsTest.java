package com.example.mike.mp3player.commons;

import android.support.v4.media.MediaDescriptionCompat;

import com.example.mike.mp3player.commons.library.Category;

import org.junit.jupiter.api.Test;

import static android.support.v4.media.MediaBrowserCompat.MediaItem;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ComparatorUtilsTest {
    /** An id constant used for testing */
    private static final String MISC_STRING = "xyz";
    /** String to use for a greater than string */
    private static final String GREATER_STRING = "z_GREATER_STRING";
    /** String to use for a less than string */
    private static final String LESSER_STRING = "a_LESSER_STRING";

    /**
     *
     */
    @Test
    public void testCompareRootItemsByCategoryGreaterAndLessThan() {
        final MediaItem SONGS_ROOT = createRootItem(Category.SONGS);
        final MediaItem ALBUMS_ROOT = createRootItem(Category.ALBUMS);
        int result = ComparatorUtils.compareRootMediaItemsByCategory.compare(SONGS_ROOT, ALBUMS_ROOT);
        final boolean ALBUMS_GREATER_THAN_FOLDERS = result < 0;
        assertTrue(ALBUMS_GREATER_THAN_FOLDERS);
        result = ComparatorUtils.compareRootMediaItemsByCategory.compare(ALBUMS_ROOT, SONGS_ROOT);
        final boolean SONGS_LESS_THAN_ALBUMS = result > 0;
        assertTrue(SONGS_LESS_THAN_ALBUMS);
    }
    /**
     *
     */
    @Test
    public void testCompareRootItemsByCategoryNullAgainstCategory() {
        final MediaItem SONGS_ROOT = createRootItem(Category.SONGS);
        final MediaItem NULL_CATEGORY = createMediaItemWithNullCategory();
        final int result = ComparatorUtils.compareRootMediaItemsByCategory.compare(SONGS_ROOT, NULL_CATEGORY);
        final boolean SONGS_GREATER_THAN_NULL = result > 0;
        assertTrue(SONGS_GREATER_THAN_NULL);
    }
    /**
     *
     */
    @Test
    public void testCompareRootItemsEqual() {
        final MediaItem SONGS_ROOT = createRootItem(Category.SONGS);
        final MediaItem SONGS_ROOT_COPY = createRootItem(Category.SONGS);
        final int result = ComparatorUtils.compareRootMediaItemsByCategory.compare(SONGS_ROOT, SONGS_ROOT_COPY);
        final boolean SONGS_GREATER_THAN_NULL = result == 0;
        assertTrue(SONGS_GREATER_THAN_NULL);
    }
    /**
     * 
     */
    @Test
    public void testCompareMediaItemByTitleGreaterAndLessThan() {
        final MediaItem GREATER_TITLE_MEDIA_ITEM = createMediaItem(MISC_STRING, GREATER_STRING, MISC_STRING);
        final MediaItem LESSER_TITLE_MEDIA_ITEM = createMediaItem(MISC_STRING, LESSER_STRING, MISC_STRING);
        int result = ComparatorUtils.compareMediaItemsByTitle.compare(GREATER_TITLE_MEDIA_ITEM, LESSER_TITLE_MEDIA_ITEM);
        assertTrue( result > 0);
        result = ComparatorUtils.compareMediaItemsByTitle.compare(LESSER_TITLE_MEDIA_ITEM, GREATER_TITLE_MEDIA_ITEM);
        assertTrue( result < 0);
    }
    /**
     *
     */
    @Test
    public void testCompareMediaItemByTitleAgainstNull() {
        final MediaItem GREATER_TITLE_MEDIA_ITEM = createMediaItem(MISC_STRING, GREATER_STRING, MISC_STRING);
        final MediaItem NULL_TITLE_MEDIA_ITEM = createMediaItem(MISC_STRING, null, MISC_STRING);
        int result = ComparatorUtils.compareMediaItemsByTitle.compare(GREATER_TITLE_MEDIA_ITEM, NULL_TITLE_MEDIA_ITEM);
        assertTrue( result > 0);
    }
    /**
     * @return a root category item
     */
    private MediaItem createRootItem(Category category) {
        MediaDescriptionCompat mediaDescriptionCompat = new MediaDescriptionCompat.Builder()
                .setDescription(category.getDescription())
                .setTitle(category.getTitle())
                .setMediaId(category.name())
                .build();
        return new MediaItem(mediaDescriptionCompat, 0);
    }
    /**
     * @return a null category MediaItem
     */
    private MediaItem createMediaItemWithNullCategory() {
       return createMediaItem(null, null, null);
    }
    /**
     * Utility class to make a media item
     * @param id the id
     * @param title the title
     * @param description the description
     * @return a new MediaItem
     */
    private MediaItem createMediaItem(final String id, final String title, final String description) {
        MediaDescriptionCompat mediaDescriptionCompat = new MediaDescriptionCompat.Builder()
                .setDescription(description)
                .setTitle(title)
                .setMediaId(id)
                .build();
        return new MediaItem(mediaDescriptionCompat, 0);
    }
}