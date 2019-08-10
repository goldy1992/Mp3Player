package com.example.mike.mp3player.service.library;

import android.support.v4.media.MediaBrowserCompat;

import com.example.mike.mp3player.commons.MediaItemType;
import com.example.mike.mp3player.service.library.contentretriever.ContentResolverRetriever;
import com.example.mike.mp3player.service.library.contentretriever.ContentRetriever;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import static android.support.v4.media.MediaBrowserCompat.*;

public class ContentManager {

    private final Map<String, MediaItemType> idToMediaItemMap;
    private final Map<MediaItemType, ContentRetriever> contentRetrieverMap;
    private final String rootId;

    @Inject
    public ContentManager(String rootId,
    Map<String, MediaItemType> mediaItemTypeStringBiMap,
                          EnumMap<MediaItemType, ContentRetriever> contentRetrieverMap) {
        this.rootId = rootId;
        this.idToMediaItemMap = mediaItemTypeStringBiMap;
        this.contentRetrieverMap = contentRetrieverMap;


    }
    public List<MediaItem> getChildren(String parentId) {
        List<String> splitId = Arrays.asList(parentId.split("\\|"));
        if (StringUtils.isEmpty(splitId.toString())) {
            return null;
        }
        String mediaItemTypeId = splitId.get(0);

        MediaItemType mediaItemType = idToMediaItemMap.get(mediaItemTypeId);
        if (null == mediaItemType) {
            return null;
        }

        String idSuffix = null;
        if (splitId.size() > 1) {
            idSuffix = splitId.get(1);
        }

        ContentRetriever contentRetriever = contentRetrieverMap.get(mediaItemType);
        return contentRetriever == null ? null : contentRetriever.getChildren(idSuffix);
    }

    public List<MediaItem> getAllSongs() {
        return contentRetrieverMap.get(MediaItemType.SONG).getChildren(null);
    }



}
