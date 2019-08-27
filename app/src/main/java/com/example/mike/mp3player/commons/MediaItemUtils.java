package com.example.mike.mp3player.commons;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.MediaMetadataCompat;

import static android.support.v4.media.MediaBrowserCompat.MediaItem;
import static com.example.mike.mp3player.commons.Constants.MEDIA_ITEM_TYPE;
import static com.example.mike.mp3player.commons.Constants.MEDIA_ITEM_TYPE_ID;
import static com.example.mike.mp3player.commons.Constants.ROOT_ITEM_TYPE;

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

    public static boolean hasMediaItemType(MediaItem item) {
        Bundle extras = getExtras(item);
        return extras == null ? null : extras.containsKey(MEDIA_ITEM_TYPE);
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

    public static String getArtist(MediaItem item) {
        return (String) getExtra(MediaMetadataCompat.METADATA_KEY_ARTIST, item);
    }

    public static String getAlbumArtPath(MediaItem item) {
        Uri uri = (Uri) getExtra(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, item);
        if (uri != null) {
            return uri.toString();
        }
        return null;
    }

    public static MediaItemType getMediaItemType(MediaItem item) {
        return (MediaItemType)getExtra(MEDIA_ITEM_TYPE, item);
    }

    public static MediaItemType getRootMediaItemType(MediaItem item) {
        return (MediaItemType)getExtra(ROOT_ITEM_TYPE, item);
    }

    public static String getMediaItemTypeId(MediaItem item) {
        return (String) getExtra(MEDIA_ITEM_TYPE_ID, item);
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
