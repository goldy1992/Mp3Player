package com.github.goldy1992.mp3player.service.library;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.goldy1992.mp3player.commons.MediaItemType;
import com.github.goldy1992.mp3player.service.library.content.ContentRetrievers;
import com.github.goldy1992.mp3player.service.library.content.request.ContentRequest;
import com.github.goldy1992.mp3player.service.library.content.request.ContentRequestParser;
import com.github.goldy1992.mp3player.service.library.content.retriever.ContentRetriever;
import com.github.goldy1992.mp3player.service.library.content.retriever.MediaItemFromIdRetriever;
import com.github.goldy1992.mp3player.service.library.content.retriever.RootRetriever;
import com.github.goldy1992.mp3player.service.library.content.retriever.SongFromUriRetriever;
import com.github.goldy1992.mp3player.service.library.content.searcher.ContentSearcher;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import static android.support.v4.media.MediaBrowserCompat.MediaItem;
import static com.github.goldy1992.mp3player.commons.Normaliser.normalise;

@Singleton
public class ContentManager {

    public static final String CONTENT_SCHEME = "content";
    public static final String FILE_SCHEME = "file";

    private final ContentSearchers contentSearchers;
    private final ContentRetrievers contentRetrievers;
    private final RootRetriever rootRetriever;
    private final ContentRequestParser contentRequestParser;
    private final SongFromUriRetriever songFromUriRetriever;
    private final MediaItemFromIdRetriever mediaItemFromIdRetriever;

    @Inject
    public ContentManager(ContentRetrievers contentRetrievers,
                          ContentSearchers contentSearchers,
                          ContentRequestParser contentRequestParser,
                          SongFromUriRetriever songFromUriRetriever,
                          MediaItemFromIdRetriever mediaItemFromIdRetriever) {
        this.contentSearchers = contentSearchers;
        this.contentRetrievers = contentRetrievers;
        this.contentRequestParser = contentRequestParser;
        this.rootRetriever = contentRetrievers.getRoot();
        this.songFromUriRetriever = songFromUriRetriever;
        this.mediaItemFromIdRetriever = mediaItemFromIdRetriever;
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
        ContentRetriever contentRetriever = contentRetrievers.get(request.getContentRetrieverKey());
        return contentRetriever == null ? null : contentRetriever.getChildren(request);
    }
    /**
     * @param query the search query
     * @return a list of media items which match the search query
     */
    public List<MediaItem> search(@NonNull String query) {
        query = normalise(query);
        List<MediaItem> results = new ArrayList<>();
        for (ContentSearcher contentSearcher : contentSearchers.getAll()) {
            List<MediaItem> searchResults = contentSearcher.search(query);
            if (CollectionUtils.isNotEmpty(searchResults)) {
                final MediaItemType searchCategory = contentSearcher.getSearchCategory();
                results.add(rootRetriever.getRootItem(searchCategory));
                results.addAll(searchResults);
            }
        }
        return results;
    }
    /**
     *
     */
    @Nullable
    public MediaItem getItem(@NonNull Uri uri) {
        return songFromUriRetriever.getSong(uri);
    }
    /**
     *
     */
    @Nullable
    public MediaItem getItem(long id) {
        /* TODO: in the future the content manager will need to know which type of URI will need
        *   to be parsed, e.g. song, album, artist etc. */
        return mediaItemFromIdRetriever.getItem(id);
    }
    /**
     *
     * @param id the id of the playlist
     * @return the playlist
     */
    public List<MediaItem> getPlaylist(String id) {
       return getChildren(id);
    }


}