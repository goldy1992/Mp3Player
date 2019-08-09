package com.example.mike.mp3player.service.library;

import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;

import com.example.mike.mp3player.commons.ComparatorUtils;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import static com.example.mike.mp3player.commons.Constants.MEDIA_ITEM_TYPE;
import static com.example.mike.mp3player.commons.Constants.PARENT_MEDIA_ITEM;

public final class Root {

    private static final String ROOT_ID = "root" + RandomStringUtils.randomAlphanumeric(15);

    private static List<MediaBrowserCompat.MediaItem> CHILDREN;

    static {
        TreeSet<MediaBrowserCompat.MediaItem> categorySet = new TreeSet<>(ComparatorUtils.compareRootMediaItemsByCategory);
        for (MediaItemType category : MediaItemType.PARENT_TO_CHILD_MAP.get(MediaItemType.ROOT)) {
            categorySet.add(createRootItem(category));
        }
        CHILDREN = new ArrayList<>(categorySet);
    }

    private Root() {    }

    public static List<MediaBrowserCompat.MediaItem> getChildren(String id) {
        if (ROOT_ID.equals(id)) {
            return CHILDREN;
        }
        return null;
    }

    /**
     * @return a root category item
     */
    private static MediaBrowserCompat.MediaItem createRootItem(MediaItemType category) {

        Bundle extras = new Bundle();
        extras.putSerializable(MEDIA_ITEM_TYPE, category);
        extras.putLong(PARENT_MEDIA_ITEM);

        MediaDescriptionCompat mediaDescriptionCompat = new MediaDescriptionCompat.Builder()
                .setDescription(category.getDescription())
                .setTitle(category.getTitle())
                .setMediaId(category.getId())
                .build();
        return new MediaBrowserCompat.MediaItem(mediaDescriptionCompat, 0);
    }
}
