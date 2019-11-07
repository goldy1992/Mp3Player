package com.example.mike.mp3player.dagger.modules.service;

import android.content.ContentResolver;
import android.content.Context;
import android.media.MediaMetadataRetriever;

import androidx.annotation.NonNull;

import com.example.mike.mp3player.commons.MediaItemType;
import com.example.mike.mp3player.service.library.content.parser.ResultsParser;
import com.example.mike.mp3player.service.library.content.parser.SongResultsParser;
import com.example.mike.mp3player.service.library.content.retriever.ContentRetriever;
import com.example.mike.mp3player.service.library.content.retriever.FoldersRetriever;
import com.example.mike.mp3player.service.library.content.retriever.RootRetriever;
import com.example.mike.mp3player.service.library.content.retriever.SongFromUriRetriever;
import com.example.mike.mp3player.service.library.content.retriever.SongsFromFolderRetriever;
import com.example.mike.mp3player.service.library.content.retriever.SongsRetriever;
import com.google.common.collect.BiMap;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ContentRetrieverModule {

    @Singleton
    @Provides
    public RootRetriever providesRootRetriever(Map<Class<? extends ContentRetriever>, ContentRetriever> contentRetrieverMap) {
        return (RootRetriever) contentRetrieverMap.get(RootRetriever.class);
    }

    @Singleton
    @Provides
    public Set<ContentRetriever> provideContentRetrievers(Map<Class<? extends ContentRetriever>, ContentRetriever> contentRetrieverMap) {
        Set<ContentRetriever> contentRetrievers = new HashSet<>();
        contentRetrievers.addAll(contentRetrieverMap.values());
        return contentRetrievers;
    }


    @Provides
    @Singleton
    public Map<String, ContentRetriever> provideIdToContentRetrieverMap(
            @NonNull Map<Class<? extends ContentRetriever>, ContentRetriever> contentRetrieverMap,
            Map<MediaItemType, String> idMap) {
        Map<String, ContentRetriever> map = new HashMap<>();
        for (MediaItemType mediaItemTypeInfo : idMap.keySet()) {
            String key = idMap.get(mediaItemTypeInfo);
            if (null != key) {
                switch (mediaItemTypeInfo) {
                    case SONGS:
                    case SONG:
                        map.put(key, contentRetrieverMap.get(SongsRetriever.class));
                        break;
                    case FOLDER:
                        map.put(key, contentRetrieverMap.get(SongsFromFolderRetriever.class));
                        break;
                    case FOLDERS:
                        map.put(key, contentRetrieverMap.get(FoldersRetriever.class));
                        break;
                    case ROOT:
                        map.put(key, contentRetrieverMap.get(RootRetriever.class));
                        break;
                    default:
                        break;
                }
            }
        }
        return map;
    }

    @Provides
    @Singleton
    public SongFromUriRetriever providesSongFromUriRetriever(Context context,
                                                             ContentResolver contentResolver,
                                                             Map<MediaItemType, ResultsParser> resultsParserMap,
                                                             BiMap<MediaItemType, String> ids) {
        return new SongFromUriRetriever(context, contentResolver, (SongResultsParser) resultsParserMap.get(MediaItemType.SONGS), new MediaMetadataRetriever(), ids.get(MediaItemType.SONGS));
    }
}
