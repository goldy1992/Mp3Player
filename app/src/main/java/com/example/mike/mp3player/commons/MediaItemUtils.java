package com.example.mike.mp3player.commons;

import android.os.Bundle;

import static android.support.v4.media.MediaBrowserCompat.MediaItem;

public final class MediaItemUtils {

    public static boolean hasExtras(MediaItem item) {
        return item != null && item.getDescription().getExtras() != null;
    }

    public static boolean hasMediaId(MediaItem item) {
        return item != null && item.getDescription().getMediaId() != null;
    }

    public static boolean hasTitle(MediaItem item) {
        return item != null && item.getDescription().getTitle() != null;
    }

    public static boolean hasDescription(MediaItem item) {
        return item != null && item.getDescription().getDescription() != null;
    }

    public static Bundle getExtras(MediaItem item) {
        if (!hasExtras(item)) {
            return null;
        }
        return item.getDescription().getExtras();
    }

    public static Object getExtra(String key, MediaItem item) {
        if (!hasExtras(item)) {
            return null;
        }
        return item.getDescription().getExtras().get(key);
    }

    public static String getMediaId(MediaItem item) {
        if (!hasMediaId(item)) {
            return null;
        }
        return item.getDescription().getMediaId();
    }

    public static String getTitle(MediaItem i) {
        if (hasTitle(i)) {
            return i.getDescription().getTitle().toString();
        }
        return null;
    }

    public static String getDescription(MediaItem item) {
        if (hasDescription(item)) {
            return item.getDescription().getDescription().toString();
        }
        return null;
    }

//    public static List<MediaItem> orderMediaItemSetByCategory(Set<MediaItem> mediaItemSet) {
//        List<MediaItem> mediaItemList = new ArrayList<>(mediaItemSet);
//        Collections.sort(mediaItemList, ComparatorUtils.compareRootMediaItemsByCategory);
//        return mediaItemList;
//    }
//
//    public static final MediaItem findMediaItemInSet(LibraryObject requestedId, Set<MediaItem> itemSet) {
//        if (requestedId == null || requestedId.getId() == null || itemSet == null || itemSet.isEmpty()) {
//            return null;
//        }
//        for (MediaItem i : itemSet) {
//            String itemId = getMediaId(i);
//            if ( requestedId.getId().equals(itemId)) {
//                return i;
//            }
//        }
//        return null;
//    }
}
