package com.example.mike.mp3player.service.library;

import com.example.mike.mp3player.commons.MediaItemType;
import com.example.mike.mp3player.service.library.contentretriever.ContentRetriever;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import static android.support.v4.media.MediaBrowserCompat.MediaItem;

public class ContentManager {

    private final Map<String, ContentRetriever> contentRetrieverMap;

    @Inject
    public ContentManager(Map<String, ContentRetriever> contentRetrieverMap) {

       this.contentRetrieverMap = contentRetrieverMap;
    }
    /**
     * The id is in the following format
     * CATEGORY_ID | CHILD_ID where CHILD_ID is optional.
     * e.g. ROOT_CATEGORY_ID
     * FOLDER_CATEGORY_ID | FOLDER_ID
     * where X_CATEGORY_ID is a unique id defined by the service to ensure that the subscriber has
     * gained authority to access the parent category and also tells the method which category to
     * look in for the data.
     * @param parentId the id of the children to get
     * @return all the children of the id specified by the parentId parameter
     */
    public List<MediaItem> getChildren(String parentId) {
        List<String> splitId = Arrays.asList(parentId.split("\\|"));
        if (StringUtils.isEmpty(splitId.toString())) {
            return null;
        }
        String mediaItemTypeId = splitId.get(0);

        String idSuffix = null;
        if (splitId.size() > 1) {
            idSuffix = splitId.get(1);
        }

        ContentRetriever contentRetriever = contentRetrieverMap.get(mediaItemTypeId);
        return contentRetriever == null ? null : contentRetriever.getChildren(idSuffix);
    }
    public List<MediaItem> getPlaylist(String id) {
        List<String> splitId = Arrays.asList(id.split("\\|"));
        if (StringUtils.isEmpty(splitId.toString())) {
            return null;
        }
        String mediaItemTypeId = splitId.get(0);

        String idSuffix = null;
        if (splitId.size() > 1) {
            idSuffix = splitId.get(1);
        }

        ContentRetriever contentRetriever = contentRetrieverMap.get(mediaItemTypeId);
        return contentRetriever == null ? null : contentRetriever.getChildren(idSuffix);
    }

    public List<MediaItem> getAllSongs() {
        return contentRetrieverMap.get(MediaItemType.SONG).getChildren(null);
    }



}
