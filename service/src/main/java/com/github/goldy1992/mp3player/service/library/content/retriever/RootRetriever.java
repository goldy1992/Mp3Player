package com.github.goldy1992.mp3player.service.library.content.retriever;

import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserCompat.MediaItem;
import android.support.v4.media.MediaDescriptionCompat;

import androidx.annotation.Nullable;

import com.github.goldy1992.mp3player.commons.MediaItemType;
import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds;
import com.github.goldy1992.mp3player.service.library.content.request.ContentRequest;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.inject.Inject;

import static com.github.goldy1992.mp3player.commons.ComparatorUtils.Companion.compareRootMediaItemsByMediaItemType;
import static com.github.goldy1992.mp3player.commons.Constants.MEDIA_ITEM_TYPE;
import static com.github.goldy1992.mp3player.commons.Constants.ROOT_ITEM_TYPE;
import static com.github.goldy1992.mp3player.commons.MediaItemType.ROOT;

public class RootRetriever extends ContentRetriever implements Comparator<MediaItem> {

    private final List<MediaItem> CHILDREN;
    private final MediaItemTypeIds mediaItemTypeIds;
    private final Map<MediaItemType, MediaItem> typeToMediaItemMap;


    @Inject
    public RootRetriever(MediaItemTypeIds mediaItemTypeIds) {
        this.mediaItemTypeIds = mediaItemTypeIds;
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
                .setMediaId(mediaItemTypeIds.getId(category))
                .setExtras(extras)
                .build();
        return new MediaItem(mediaDescriptionCompat, 0);
    }

    @Override
    public int compare(MediaItem o1, MediaItem o2) {
        return compareRootMediaItemsByMediaItemType.compare(o1, o2);
    }
}
