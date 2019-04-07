package com.example.mike.mp3player.commons;

import com.example.mike.mp3player.commons.library.Category;

import java.util.Comparator;

import static android.support.v4.media.MediaBrowserCompat.MediaItem;
import static com.example.mike.mp3player.commons.MediaItemUtils.getTitle;

public final class ComparatorUtils {

    public static Comparator<MediaItem> compareRootMediaItemsByCategory = (MediaItem m1, MediaItem m2) -> compareRootMediaItemsByCategory(m1, m2);
    private static int compareRootMediaItemsByCategory(MediaItem m1, MediaItem m2) {
        Category c1 = parseCategory(MediaItemUtils.getMediaId(m1));
        Category c2 = parseCategory(MediaItemUtils.getMediaId(m2));
        if (null != c1) {
           return c1.compareTo(c2);
        }
        if (null != c2) { // we can assume c1 to be null
            return 1;
        }
        return 0;
    }
    public static Comparator<MediaItem> compareMediaItemsByTitle = (MediaItem m1, MediaItem m2) -> compareMediaItemsByTitle(m1, m2);
    private static int compareMediaItemsByTitle(MediaItem m1, MediaItem m2) {
        String title1 = getTitle(m1);
        String title2 = getTitle(m2);
        if (null != title1) {
            return title1.compareTo(title2);
        }
        else if (null != title2) {
            return 1;
        }
        return 0;
    }

    public static  Comparator<MediaItem> compareMediaItemById = (MediaItem m1, MediaItem m2) -> compareMediaItembyId(m1, m2);
    private static int compareMediaItembyId(MediaItem o1, MediaItem o2) {
        String m1 = MediaItemUtils.getMediaId(o1);
        String m2 = MediaItemUtils.getMediaId(o2);
        if (m1 != null && m2 != null ) {
            return m1.compareTo(m2);
        }
        return  0;
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
