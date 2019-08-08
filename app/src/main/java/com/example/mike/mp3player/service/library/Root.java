package com.example.mike.mp3player.service.library;

import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;

import com.example.mike.mp3player.commons.ComparatorUtils;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public final class Root {

    private static final String ROOT_ID = "root" + RandomStringUtils.randomAlphanumeric(15);

    private static List<MediaBrowserCompat.MediaItem> CHILDREN;

    static {
        TreeSet<MediaBrowserCompat.MediaItem> categorySet = new TreeSet<>(ComparatorUtils.compareRootMediaItemsByCategory);
        for (Category category : Category.values()) {
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
    private static MediaBrowserCompat.MediaItem createRootItem(Category category) {
        MediaDescriptionCompat mediaDescriptionCompat = new MediaDescriptionCompat.Builder()
                .setDescription(category.getDescription())
                .setTitle(category.getTitle())
                .setMediaId(category.name())
                .build();
        return new MediaBrowserCompat.MediaItem(mediaDescriptionCompat, 0);
    }
}
