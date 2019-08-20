package com.example.mike.mp3player.commons;

import android.util.Log;

import androidx.annotation.Nullable;

import com.example.mike.mp3player.client.Category;

import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.Comparator;

import static android.support.v4.media.MediaBrowserCompat.MediaItem;
import static com.example.mike.mp3player.commons.MediaItemUtils.getTitle;

public final class ComparatorUtils {

    private static final String LOG_TAG = "COMPARATOR_UTILS";
    public static final Comparator<MediaItem> compareRootMediaItemsByCategory = ComparatorUtils::compareRootMediaItemsByCategory;
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


    public static final Comparator<MediaItem> compareRootMediaItemsByMediaItemType = ComparatorUtils::compareRootMediaItemsByMediaItemType;
    public static int compareRootMediaItemsByMediaItemType(MediaItem m1, MediaItem m2) {
        MediaItemType c1 = MediaItemUtils.getMediaItemType(m1);
        MediaItemType c2 = MediaItemUtils.getMediaItemType(m2);
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
//    public static final Comparator<Root> compareRootByCategory = ComparatorUtils::compareRootByCategory;
//    private static int compareRootByCategory(Root root1, Root root2) {
//        Category c1 = root1.category;
//        Category c2 = root2.category;
//        if (c1 == null && c2 == null) {
//            return 0;
//        } else if (c1 == null) {
//            return -1;
//        } else if (c2 == null) {
//            return 1;
//        } else {
//            return c1.compareTo(c2);
//        }
//    }

    public static final Comparator<MediaItem> compareMediaItemsByTitle = ComparatorUtils::compareMediaItemsByTitle;

    private static int compareMediaItemsByTitle(MediaItem m1, MediaItem m2) {
        return uppercaseStringCompare(getTitle(m1), getTitle(m2));
    }

    public static final Comparator<MediaItem> compareMediaItemById = ComparatorUtils::compareMediaItemById;
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
            Log.e(LOG_TAG, ExceptionUtils.getMessage(ex));
        }
        return c;
    }

    private static MediaItemType parseMediaItemType(String categoryString) {
        MediaItemType type = null;
        try{
            type = MediaItemType.valueOf(categoryString);
        } catch (IllegalArgumentException | NullPointerException ex) {
            Log.e(LOG_TAG, ExceptionUtils.getMessage(ex));
        }
        return type;
    }

    public static int uppercaseStringCompare(@Nullable String string1, @Nullable String string2) {
        if (string1 == null && string2 == null) {
            return 0;
        } else if (null == string1) {
            return -1;
        } else if (null == string2) {
            return 1;
        } else {
            return string1.toUpperCase().compareTo(string2.toUpperCase());
        }
    }
}
