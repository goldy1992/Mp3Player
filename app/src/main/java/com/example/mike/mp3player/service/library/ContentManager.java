package com.example.mike.mp3player.service.library;

import com.example.mike.mp3player.commons.MediaItemType;
import com.example.mike.mp3player.service.library.contentretriever.ContentRetriever;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import static android.support.v4.media.MediaBrowserCompat.MediaItem;

public class ContentManager {

    private final Map<String, ContentRetriever> idToContentRetrieverMap;
    private final EnumMap<MediaItemType, ContentRetriever> typeToContentRetrieverMap;

    @Inject
    public ContentManager(Map<String, ContentRetriever> idToContentRetrieverMap,
                          EnumMap<MediaItemType, ContentRetriever> typeContentRetrieverMap) {
       this.idToContentRetrieverMap = idToContentRetrieverMap;
       this.typeToContentRetrieverMap = typeContentRetrieverMap;
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
        ContentRequest request = ContentRequest.parse(parentId);
        ContentRetriever contentRetriever = idToContentRetrieverMap.get(request.getContentRetrieverKey());
        return contentRetriever == null ? null : contentRetriever.getChildren(request);
    }
    /**
     * @param query the search query
     * @return a list of media items which match the search query
     */
    public List<MediaItem> search(String query) {
        List<MediaItem> results = new ArrayList<>();
        for (ContentRetriever contentRetriever : idToContentRetrieverMap.values()) {
            results.addAll(contentRetriever.search(query));
        }
        return results;
    }
    public List<MediaItem> getPlaylist(String id) {
       return getChildren(id);
    }

    private ContentRetriever getContentRetrieverFromId(List<String> splitId) {
        if (StringUtils.isEmpty(splitId.toString())) {
            return null;
        }
        ContentRetriever contentRetriever = null;
        for (String s : splitId) {
            contentRetriever = idToContentRetrieverMap.get(s);
            if (contentRetriever != null) {
                return contentRetriever;
            }
        }
        return null;
    }



}
