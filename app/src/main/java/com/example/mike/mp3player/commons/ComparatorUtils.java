package com.example.mike.mp3player.commons;

import android.support.v4.media.MediaBrowserCompat;

import com.example.mike.mp3player.commons.library.Category;

public final class ComparatorUtils {

    public static int compareRootMediaItemsByCategory(MediaBrowserCompat.MediaItem m1, MediaBrowserCompat.MediaItem m2) {
        Category c1 = parseCategory(MediaItemUtils.getMediaId(m1));
        Category c2 = parseCategory(MediaItemUtils.getMediaId(m2));


        if (c1 == null && c2 ==  null) {
            return 0;
        }
        if (c1 == null) {
            return 1;
        }
        if (c2 == null ) {
            return -1;
        }
        return c1.compare(c1, c2);
    }

    private static Category parseCategory(String categoryString) {
        Category c = null;
        try{
            c = Category.valueOf(categoryString);
        } catch (IllegalArgumentException | NullPointerException ex) {

        }
        return c;
    }
}
