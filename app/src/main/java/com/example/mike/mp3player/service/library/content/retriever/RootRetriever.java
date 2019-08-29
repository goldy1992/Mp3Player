package com.example.mike.mp3player.service.library.content.retriever;

import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserCompat.MediaItem;
import android.support.v4.media.MediaDescriptionCompat;

import com.example.mike.mp3player.commons.MediaItemType;
import com.example.mike.mp3player.service.library.ContentRequest;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.annotation.Nullable;
import javax.inject.Inject;

import static com.example.mike.mp3player.commons.ComparatorUtils.compareRootMediaItemsByMediaItemType;
import static com.example.mike.mp3player.commons.Constants.MEDIA_ITEM_TYPE;
import static com.example.mike.mp3player.commons.Constants.ROOT_ITEM_TYPE;
import static com.example.mike.mp3player.commons.MediaItemType.ROOT;

public class RootRetriever extends ContentRetriever implements Comparator<MediaItem> {

    private final List<MediaItem> CHILDREN;
    private final Map<MediaItemType, String> childIds;
    private final Map<MediaItemType, MediaItem> typeToMediaItemMap;


    @Inject
    public RootRetriever(Map<MediaItemType, String> childInfos) {
        this.childIds = childInfos;
        TreeSet<MediaItem> categorySet = new TreeSet<>(this);
        typeToMediaItemMap = new EnumMap<>(MediaItemType.class);
        for (MediaItemType category : MediaItemType.PARENT_TO_CHILD_MAP.get(ROOT)) {
            MediaItem mediaItem = createRootItem(category);
            categorySet.add(mediaItem);
            typeToMediaItemMap.put(category, mediaItem);
        }
        CHILDREN = new ArrayList<>(categorySet);
    }

    @Override
    public List<MediaItem> getChildren(ContentRequest request) {
        return CHILDREN;
    }

    @Nullable
    public MediaItem getRootItem(MediaItemType mediaItemType) {
        return typeToMediaItemMap.get(mediaItemType);
    }

    @Override
    public MediaItemType getType() {
        return ROOT;
    }

    /**
     * @return a root category item
     */
    private MediaBrowserCompat.MediaItem createRootItem(MediaItemType category) {
        Bundle extras = new Bundle();
        extras.putSerializable(MEDIA_ITEM_TYPE, ROOT);
        extras.putSerializable(ROOT_ITEM_TYPE, category);
        MediaDescriptionCompat mediaDescriptionCompat = new MediaDescriptionCompat.Builder()
                .setDescription(category.getDescription())
                .setTitle(category.getTitle())
                .setMediaId(childIds.get(category))
                .setExtras(extras)
                .build();
        return new MediaItem(mediaDescriptionCompat, 0);
    }

    @Override
    public int compare(MediaItem o1, MediaItem o2) {
        return compareRootMediaItemsByMediaItemType(o1, o2);
    }
}
