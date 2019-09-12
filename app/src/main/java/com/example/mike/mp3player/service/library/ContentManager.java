package com.example.mike.mp3player.service.library;

import com.example.mike.mp3player.commons.MediaItemType;
import com.example.mike.mp3player.service.library.content.request.ContentRequest;
import com.example.mike.mp3player.service.library.content.request.ContentRequestParser;
import com.example.mike.mp3player.service.library.content.retriever.ContentRetriever;
import com.example.mike.mp3player.service.library.content.retriever.RootRetriever;
import com.example.mike.mp3player.service.library.content.searcher.ContentSearcher;
import com.example.mike.mp3player.service.library.search.SearchDatabase;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import static android.support.v4.media.MediaBrowserCompat.MediaItem;

public class ContentManager {

    private final Map<String, ContentRetriever> idToContentRetrieverMap;
    private final Map<MediaItemType, ContentSearcher> searchContentRetrieverMap;
    private final RootRetriever rootRetriever;
    private final ContentRequestParser contentRequestParser;

    @Inject
    public ContentManager(Map<String, ContentRetriever> idToContentRetrieverMap,
                          Map<MediaItemType, ContentSearcher> searchContentRetrieverMap,
                          ContentRequestParser contentRequestParser,
                          RootRetriever rootRetriever) {
       this.idToContentRetrieverMap = idToContentRetrieverMap;
       this.searchContentRetrieverMap = searchContentRetrieverMap;
       this.contentRequestParser = contentRequestParser;
       this.rootRetriever = rootRetriever;
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
        ContentRequest request = this.contentRequestParser.parse(parentId);
        ContentRetriever contentRetriever = idToContentRetrieverMap.get(request.getContentRetrieverKey());
        return contentRetriever == null ? null : contentRetriever.getChildren(request);
    }
    /**
     * @param query the search query
     * @return a list of media items which match the search query
     */
    public List<MediaItem> search(String query) {
        query = query.trim();
        List<MediaItem> results = new ArrayList<>();
        for (ContentSearcher contentSearcher : searchContentRetrieverMap.values()) {
            List<MediaItem> searchResults = contentSearcher.search(query);
            if (CollectionUtils.isNotEmpty(searchResults)) {
                final MediaItemType searchCategory = contentSearcher.getSearchCategory();
                results.add(rootRetriever.getRootItem(searchCategory));
                results.addAll(searchResults);
            }
        }
        return results;
    }
    public List<MediaItem> getPlaylist(String id) {
       return getChildren(id);
    }

}
