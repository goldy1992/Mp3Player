package com.example.mike.mp3player.service.library.contentretriever;

import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;

import com.example.mike.mp3player.commons.ComparatorUtils;
import com.example.mike.mp3player.commons.Constants;
import com.example.mike.mp3player.commons.MediaItemType;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.TreeSet;

import javax.inject.Inject;

public class RootRetriever implements ContentRetriever {

    private List<MediaBrowserCompat.MediaItem> CHILDREN;
    private final EnumMap<MediaItemType, String> childIds;


    @Inject
    public RootRetriever(EnumMap<MediaItemType, String> childIds) {
        this.childIds = childIds;
        TreeSet<MediaBrowserCompat.MediaItem> categorySet = new TreeSet<>(ComparatorUtils.compareRootMediaItemsByMediaItemType);
        for (MediaItemType category : MediaItemType.PARENT_TO_CHILD_MAP.get(MediaItemType.ROOT)) {
            categorySet.add(createRootItem(category));
        }
        CHILDREN = new ArrayList<>(categorySet);
    }

    @Override
    public List<MediaBrowserCompat.MediaItem> getChildren(String id) {
        return CHILDREN;
    }

    @Override
    public List<MediaBrowserCompat.MediaItem> search(String query) {
        return null;
    }

    /**
     * @return a root category item
     */
    private MediaBrowserCompat.MediaItem createRootItem(MediaItemType category) {
        Bundle extras = new Bundle();
        extras.putSerializable(Constants.MEDIA_ITEM_TYPE, category);
        MediaDescriptionCompat mediaDescriptionCompat = new MediaDescriptionCompat.Builder()
                .setDescription(category.getDescription())
                .setTitle(category.getTitle())
                .setMediaId(childIds.get(category))
                .setExtras(extras)
                .build();
        return new MediaBrowserCompat.MediaItem(mediaDescriptionCompat, 0);
    }

}