package com.example.mike.mp3player.service.library.content.request;

import androidx.annotation.Nullable;

import com.example.mike.mp3player.commons.MediaItemType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import static com.example.mike.mp3player.commons.MediaItemType.SINGLETON_PARENT_TO_CHILD_MAP;

public class ContentRequestParser {

    private static final String DELIMITER = "\\|";
    private static final String SEPARATOR = "|";

    private final Map<MediaItemType, String> mediaItemTypeToIdMap;
    private final Map<String, MediaItemType> idToMediaItemTypeMap;

    @Inject
    public ContentRequestParser(Map<MediaItemType, String> mediaItemTypeToIdMap,
                                Map<String, MediaItemType> idToMediaItemTypeMap) {
        this.mediaItemTypeToIdMap = mediaItemTypeToIdMap;
        this.idToMediaItemTypeMap = idToMediaItemTypeMap;
    }

    public ContentRequest parse(String id) {
        List<String> splitId = Arrays.asList(id.split(DELIMITER));
        String mediaIdPrefix = calculateMediaIdPrefix(splitId);
        final int splidIdSize = splitId.size();
        if (splidIdSize == 1) {
            return new ContentRequest(splitId.get(0), splitId.get(0), mediaIdPrefix);
        } else if (splidIdSize >= 2) {
            return new ContentRequest(splitId.get(1), splitId.get(0), mediaIdPrefix);
        }
        return null;
    }

    private String calculateMediaIdPrefix(List<String> splitId) {
        final int splidIdSize = splitId.size();
        if (splidIdSize == 1) {
            MediaItemType mediaItemType = getMediaItemTypeFromId(splitId.get(0));
            if (mediaItemType != null) {
                MediaItemType typeToReturn = SINGLETON_PARENT_TO_CHILD_MAP.get(mediaItemType);
                if (null != typeToReturn) {
                    return mediaItemTypeToIdMap.get(typeToReturn);
                }
            }
        } else if (splidIdSize >= 2) {
            return splitId.get(0) + SEPARATOR + splitId.get(1);
        }
        return null;
    }

    @Nullable
    private MediaItemType getMediaItemTypeFromId(String id) {
        return idToMediaItemTypeMap.get(id);
    }
}
