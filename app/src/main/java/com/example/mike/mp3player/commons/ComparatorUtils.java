package com.example.mike.mp3player.commons;

import android.util.Log;

import com.example.mike.mp3player.commons.library.Category;

import org.apache.commons.lang.exception.ExceptionUtils;

import java.util.Comparator;

import static android.support.v4.media.MediaBrowserCompat.MediaItem;
import static com.example.mike.mp3player.commons.MediaItemUtils.getTitle;

public final class ComparatorUtils {

    private static final String LOG_TAG = "COMPARATOR_UTILS";
    public static final Comparator<MediaItem> compareRootMediaItemsByCategory = (MediaItem m1, MediaItem m2) -> compareRootMediaItemsByCategory(m1, m2);
    private static int compareRootMediaItemsByCategory(MediaItem m1, MediaItem m2) {
        Category c1 = parseCategory(MediaItemUtils.getMediaId(m1));
        Category c2 = parseCategory(MediaItemUtils.getMediaId(m2));
        if (c1 == null && c2 == null) {
            return 0;
        } else if (c1 == null) {
            return -1;
        } else if (c2 == null) {
            return 1;
        } else {
           return c1.compareTo(c2);
        }
    }
    public static Comparator<MediaItem> compareMediaItemsByTitle = (MediaItem m1, MediaItem m2) -> compareMediaItemsByTitle(m1, m2);
    private static int compareMediaItemsByTitle(MediaItem m1, MediaItem m2) {
        String title1 = getTitle(m1);
        String title2 = getTitle(m2);
        if (title1 == null && title2 == null) {
            return 0;
        } else if (null == title1) {
            return -1;
        } else if (null == title2) {
            return 1;
        } else {
            return title1.compareTo(title2);
        }
    }

    public static  Comparator<MediaItem> compareMediaItemById = (MediaItem m1, MediaItem m2) -> compareMediaItemById(m1, m2);
    private static int compareMediaItemById(MediaItem o1, MediaItem o2) {
        String m1 = MediaItemUtils.getMediaId(o1);
        String m2 = MediaItemUtils.getMediaId(o2);
        if (m1 == null && m2 == null ) {
            return 0;
        } else if (m1 == null) {
            return -1;
        } else if (m2 == null) {
            return 1;
        } else {
            return m1.compareTo(m2);
        }
    }


    private static Category parseCategory(String categoryString) {
        Category c = null;
        try{
            c = Category.valueOf(categoryString);
        } catch (IllegalArgumentException | NullPointerException ex) {
            Log.e(LOG_TAG, ExceptionUtils.getFullStackTrace(ex));
        }
        return c;
    }
}
