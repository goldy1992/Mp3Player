package com.github.goldy1992.mp3player.service.library.content.request;

import androidx.annotation.Nullable;

import com.github.goldy1992.mp3player.commons.MediaItemType;
import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import static com.github.goldy1992.mp3player.commons.Constants.ID_DELIMITER;
import static com.github.goldy1992.mp3player.commons.Constants.ID_SEPARATOR;
import static com.github.goldy1992.mp3player.commons.MediaItemType.SINGLETON_PARENT_TO_CHILD_MAP;

public class ContentRequestParser {


    private final MediaItemTypeIds mediaItemTypeIds;

    @Inject
    public ContentRequestParser(MediaItemTypeIds mediaItemTypeIds) {
        this.mediaItemTypeIds = mediaItemTypeIds;
    }

    public ContentRequest parse(String id) {
        List<String> splitId = Arrays.asList(id.split(ID_DELIMITER));
        String mediaIdPrefix = calculateMediaIdPrefix(splitId);
        final int splitIdSize = splitId.size();
        if (splitIdSize == 1) {
            return new ContentRequest(splitId.get(0), splitId.get(0), mediaIdPrefix);
        } else if (splitIdSize >= 2) {
            return new ContentRequest(splitId.get(1), splitId.get(0), mediaIdPrefix);
        }
        return null;
    }

    private String calculateMediaIdPrefix(List<String> splitId) {
        final int splitIdSize = splitId.size();
        if (splitIdSize == 1) {
            MediaItemType mediaItemType = getMediaItemTypeFromId(splitId.get(0));
            if (mediaItemType != null) {
                MediaItemType typeToReturn = SINGLETON_PARENT_TO_CHILD_MAP.get(mediaItemType);
                if (null != typeToReturn) {
                    return mediaItemTypeIds.getId(typeToReturn);
                }
            }
        } else if (splitIdSize >= 2) {
            return splitId.get(0) + ID_SEPARATOR + splitId.get(1);
        }
        return null;
    }

    @Nullable
    private MediaItemType getMediaItemTypeFromId(String id) {
        return mediaItemTypeIds.getMediaItemType(id);
    }
}
