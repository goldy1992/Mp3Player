package com.example.mike.mp3player.service.library.contentretriever;

import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;

import com.example.mike.mp3player.commons.ComparatorUtils;
import com.example.mike.mp3player.commons.Constants;
import com.example.mike.mp3player.commons.MediaItemLibraryInfo;
import com.example.mike.mp3player.commons.MediaItemType;
import com.example.mike.mp3player.commons.MediaItemTypeInfo;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.inject.Inject;

import static com.example.mike.mp3player.commons.ComparatorUtils.compareRootMediaItemsByMediaItemType;
import static com.example.mike.mp3player.commons.Constants.MEDIA_ITEM_TYPE_ID;
import static com.example.mike.mp3player.commons.Constants.MEDIA_LIBRARY_INFO;
import static com.example.mike.mp3player.commons.Constants.PARENT_MEDIA_ITEM_TYPE;
import static com.example.mike.mp3player.commons.Constants.PARENT_MEDIA_ITEM_TYPE_ID;
import static com.example.mike.mp3player.commons.MediaItemType.PARENT_TO_CHILD_MAP;
import static com.example.mike.mp3player.commons.MediaItemType.ROOT;

public class RootRetriever extends ContentRetriever {

    private List<MediaBrowserCompat.MediaItem> CHILDREN;
    private final EnumMap<MediaItemType, String> childIds;


    @Inject
    public RootRetriever(Map<MediaItemTypeInfo, String> childInfos) {
        super(childInfos);
        this.childIds = childIds;
        TreeSet<MediaBrowserCompat.MediaItem> categorySet = new TreeSet<>(this);
        for (MediaItemType category : MediaItemType.PARENT_TO_CHILD_MAP.get(ROOT)) {
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

    @Override
    public MediaItemType getType() {
        return ROOT;
    }

    @Override
    public MediaItemType getParentType() {
        return null;
    }

    /**
     * @return a root category item
     */
    private MediaBrowserCompat.MediaItem createRootItem(MediaItemTypeInfo category) {
        Bundle extras = new Bundle();
        //extras.putSerializable(MEDIA_ITEM_TYPE,);
        extras.putSerializable(MEDIA_LIBRARY_INFO, childIds.get(category));
        MediaDescriptionCompat mediaDescriptionCompat = new MediaDescriptionCompat.Builder()
                .setDescription(category.getDescription())
                .setTitle(category.getTitle())
                .setMediaId(childIds.get(category))
                .setExtras(extras)
                .build();
        return new MediaBrowserCompat.MediaItem(mediaDescriptionCompat, 0);
    }

    @Override
    public int compare(MediaBrowserCompat.MediaItem o1, MediaBrowserCompat.MediaItem o2) {
        return compareRootMediaItemsByMediaItemType(o1, o2);
    }
}
