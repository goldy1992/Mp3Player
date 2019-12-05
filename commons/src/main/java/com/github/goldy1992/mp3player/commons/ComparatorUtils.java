package com.github.goldy1992.mp3player.commons;

import androidx.annotation.Nullable;

import java.util.Comparator;

import static android.support.v4.media.MediaBrowserCompat.MediaItem;
import static com.github.goldy1992.mp3player.commons.MediaItemUtils.getTitle;

public final class ComparatorUtils {

    private static final String LOG_TAG = "COMPARATOR_UTILS";


    public static final Comparator<MediaItem> compareRootMediaItemsByMediaItemType = ComparatorUtils::compareRootMediaItemsByMediaItemType;
    public static int compareRootMediaItemsByMediaItemType(MediaItem m1, MediaItem m2) {
        MediaItemType c1 = MediaItemUtils.getRootMediaItemType(m1);
        MediaItemType c2 = MediaItemUtils.getRootMediaItemType(m2);
        if (c1 == null && c2 == null) {
            return 0;
        } else if (c1 == null) {
            return -1;
        } else if (c2 == null) {
            return 1;
        } else {
            return c1.getRank() - c2.getRank();
        }
    }

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

    public static int caseSensitiveStringCompare(@Nullable String string1, @Nullable String string2) {
        if (string1 == null && string2 == null) {
            return 0;
        } else if (null == string1) {
            return -1;
        } else if (null == string2) {
            return 1;
        } else {
            return string1.compareTo(string2);
        }
    }
}
