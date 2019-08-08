package com.example.mike.mp3player.service.library;

import android.net.Uri;
import android.support.v4.media.MediaBrowserCompat.MediaItem;

import androidx.annotation.NonNull;

import com.example.mike.mp3player.commons.MediaItemType;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.example.mike.mp3player.commons.MediaItemType.getMediaItemTypeById;

@Singleton
public class MediaLibrary {

    private boolean playlistRecursInSubDirectory = false;
    private Map<MediaItemType, ContentRetriever> contentRetrieverMap;
    private final String LOG_TAG = "MEDIA_LIBRARY";

    @Inject
    public MediaLibrary(Map<MediaItemType, ContentRetriever> contentRetrieverMap) {
        this.contentRetrieverMap = contentRetrieverMap;
    }

    public List<MediaItem> getChildren(@NonNull String parentId) {
        List<String> splitId = Arrays.asList(parentId.split("\\|"));

        if (StringUtils.isEmpty(splitId.toString())) {
            return null;
        }
        String mediaItemTypeId = splitId.get(0);
        MediaItemType mediaItemType = getMediaItemTypeById(mediaItemTypeId);

        if (null == mediaItemType) {
            return null;
        }
        String idSuffix = null;
        if (splitId.size() > 1) {
            idSuffix = splitId.get(1);
        }

        ContentRetriever contentRetriever = contentRetrieverMap.get(mediaItemType);
        return contentRetriever != null ? contentRetriever.getChildren(idSuffix) : null;

    }



    public Uri getMediaUriFromMediaId(String mediaId){
        throw new UnsupportedOperationException();
    }

}
