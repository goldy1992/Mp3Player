package com.github.goldy1992.mp3player.service.library.content;

import com.github.goldy1992.mp3player.commons.MediaItemType;
import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds;
import com.github.goldy1992.mp3player.service.library.content.retriever.ContentRetriever;
import com.github.goldy1992.mp3player.service.library.content.retriever.FoldersRetriever;
import com.github.goldy1992.mp3player.service.library.content.retriever.RootRetriever;
import com.github.goldy1992.mp3player.service.library.content.retriever.SongsFromFolderRetriever;
import com.github.goldy1992.mp3player.service.library.content.retriever.SongsRetriever;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ContentRetrievers {

    /** */
    Map<Class<? extends ContentRetriever>, ContentRetriever> contentRetrieverMap;
    /** */
    Map<String, ContentRetriever> idToContentRetrieverMap;

    private final RootRetriever rootRetriever;

    @Inject
    public ContentRetrievers(MediaItemTypeIds mediaItemTypeIds,
                             RootRetriever rootRetriever,
                             SongsRetriever songsRetriever,
                             FoldersRetriever foldersRetriever,
                             SongsFromFolderRetriever songsFromFolderRetriever) {

        Map<Class<? extends ContentRetriever>, ContentRetriever> mapToReturn = new HashMap<>();
        mapToReturn.put(RootRetriever.class, rootRetriever);
        mapToReturn.put(SongsRetriever.class, songsRetriever);
        mapToReturn.put(FoldersRetriever.class, foldersRetriever);
        mapToReturn.put(SongsFromFolderRetriever.class, songsFromFolderRetriever);
        this.contentRetrieverMap = mapToReturn;
                createIdMap(mediaItemTypeIds);
        this.rootRetriever = rootRetriever;
    }

    public ContentRetriever get(String id) {
        return this.idToContentRetrieverMap.get(id);
    }

    public ContentRetriever get(Class<? extends ContentRetriever> clazz) {
        return this.contentRetrieverMap.get(clazz);
    }

    private void createIdMap(MediaItemTypeIds mediaItemTypeIds) {
        this.idToContentRetrieverMap = new HashMap<>();
        for (MediaItemType mediaItemTypeInfo : MediaItemType.values()) {
            String key = mediaItemTypeIds.getId(mediaItemTypeInfo);
            if (null != key) {
                switch (mediaItemTypeInfo) {
                    case SONGS:
                    case SONG:
                        addToIdToContentRetrieverMap(key, SongsRetriever.class);
                        break;
                    case FOLDER:
                        addToIdToContentRetrieverMap(key, SongsFromFolderRetriever.class);
                        break;
                    case FOLDERS:
                        addToIdToContentRetrieverMap(key, FoldersRetriever.class);
                        break;
                    case ROOT:
                        addToIdToContentRetrieverMap(key, RootRetriever.class);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public RootRetriever getRoot() {
        return rootRetriever;
    }

    private void addToIdToContentRetrieverMap(String key, Class<? extends ContentRetriever> clazz) {
        ContentRetriever contentRetriever = contentRetrieverMap.get(clazz);
        if (null != contentRetriever) {
            idToContentRetrieverMap.put(key, contentRetriever);

        }
    }
}
