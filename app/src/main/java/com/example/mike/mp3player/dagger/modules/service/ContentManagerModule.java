package com.example.mike.mp3player.dagger.modules.service;

import android.content.ContentResolver;
import android.content.Context;

import com.example.mike.mp3player.commons.MediaItemType;
import com.example.mike.mp3player.service.library.content.retriever.ContentRetriever;
import com.example.mike.mp3player.service.library.content.retriever.FoldersRetriever;
import com.example.mike.mp3player.service.library.content.retriever.RootRetriever;
import com.example.mike.mp3player.service.library.content.retriever.SongsFromFolderRetriever;
import com.example.mike.mp3player.service.library.content.retriever.SongsRetriever;
import com.example.mike.mp3player.service.library.content.searcher.ContentSearcher;
import com.example.mike.mp3player.service.library.content.searcher.FolderSearcher;
import com.example.mike.mp3player.service.library.content.searcher.SongSearcher;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ContentManagerModule {

    @Singleton
    @Provides
    public Map<Class<? extends ContentRetriever>, ContentRetriever> provideContentResolverRetrieverMap(ContentResolver contentResolver,
                                                                            EnumMap<MediaItemType, String> ids) {
        Map<Class<? extends ContentRetriever>, ContentRetriever> mapToReturn = new HashMap<>();
        RootRetriever rootRetriever = new RootRetriever(ids);
        mapToReturn.put(RootRetriever.class, rootRetriever);
        SongsRetriever songsRetriever = new SongsRetriever(contentResolver, ids.get(MediaItemType.SONGS));
        mapToReturn.put(SongsRetriever.class, songsRetriever);
        FoldersRetriever foldersRetriever = new FoldersRetriever(contentResolver, ids.get(MediaItemType.FOLDER));
        mapToReturn.put(FoldersRetriever.class, foldersRetriever);
        SongsFromFolderRetriever songsFromFolderRetriever = new SongsFromFolderRetriever(contentResolver, null);
        mapToReturn.put(SongsFromFolderRetriever.class, songsFromFolderRetriever);
        return mapToReturn;
    }

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
    public ContentResolver provideContentResolver(Context context) {
        return context.getContentResolver();
    }

    @Provides
    @Singleton
    public Map<String, ContentRetriever> provideIdToContentRetrieverMap(
            Map<Class<? extends ContentRetriever>, ContentRetriever> contentRetrieverMap,
            EnumMap<MediaItemType, String> idMap) {
        Map<String, ContentRetriever> map = new HashMap<>();
        for (MediaItemType mediaItemTypeInfo : idMap.keySet()) {
            switch (mediaItemTypeInfo) {
                case SONGS:
                case SONG:
                    map.put(idMap.get(MediaItemType.SONGS), contentRetrieverMap.get(SongsRetriever.class));
                    break;
                case FOLDER:
                    map.put(idMap.get(MediaItemType.FOLDER), contentRetrieverMap.get(SongsFromFolderRetriever.class));
                    break;
                case FOLDERS: map.put(idMap.get(MediaItemType.FOLDERS), contentRetrieverMap.get(FoldersRetriever.class));
                    break;
                case ROOT: map.put(idMap.get(MediaItemType.ROOT), contentRetrieverMap.get(RootRetriever.class));
                    break;
                default:
                    break;
            }
        }
        return map;
    }

    @Provides
    @Singleton
    public EnumMap<MediaItemType, ContentSearcher> provideSearchSearcherMap(ContentResolver contentResolver,
                                                                            EnumMap<MediaItemType, String> idMap) {
        EnumMap<MediaItemType, ContentSearcher> map = new EnumMap<>(MediaItemType.class);
        map.put(MediaItemType.SONG, new SongSearcher(contentResolver, idMap.get(MediaItemType.SONG)));
        map.put(MediaItemType.FOLDER, new FolderSearcher(contentResolver, idMap.get(MediaItemType.FOLDER)));
        return map;
    }

}
