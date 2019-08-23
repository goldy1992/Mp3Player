package com.example.mike.mp3player.dagger.modules.service;

import android.content.ContentResolver;
import android.content.Context;

import com.example.mike.mp3player.commons.MediaItemType;
import com.example.mike.mp3player.service.library.contentretriever.ContentResolverRetriever;
import com.example.mike.mp3player.service.library.contentretriever.ContentRetriever;
import com.example.mike.mp3player.service.library.contentretriever.FoldersRetriever;
import com.example.mike.mp3player.service.library.contentretriever.RootRetriever;
import com.example.mike.mp3player.service.library.contentretriever.SongsFromFolderRetriever;
import com.example.mike.mp3player.service.library.contentretriever.SongsRetriever;

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
        SongsRetriever songsRetriever = new SongsRetriever(contentResolver);
        mapToReturn.put(SongsRetriever.class, songsRetriever);
        FoldersRetriever foldersRetriever = new FoldersRetriever(contentResolver);
        mapToReturn.put(FoldersRetriever.class, foldersRetriever);
        SongsFromFolderRetriever songsFromFolderRetriever = new SongsFromFolderRetriever(contentResolver);
        mapToReturn.put(SongsFromFolderRetriever.class, songsFromFolderRetriever);
        return mapToReturn;
    }

    @Singleton
    @Provides
    public Set<ContentRetriever> provideContentRetrievers(RootRetriever rootRetriever, Set<ContentResolverRetriever> contentResolverRetrievers) {
        Set<ContentRetriever> contentRetrievers = new HashSet<>();
        contentRetrievers.add(rootRetriever);
        contentRetrievers.addAll(contentResolverRetrievers);
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
    public EnumMap<MediaItemType, ContentRetriever> provideTypeToContentRetrieverMap(
            Map<Class<? extends ContentRetriever>, ContentRetriever> contentRetrieverMap) {
        EnumMap<MediaItemType, ContentRetriever> map = new EnumMap<>(MediaItemType.class);
        for (MediaItemType mediaItemType  :MediaItemType.values()) {
            switch (mediaItemType) {
                case SONG:
                case SONGS:
                    map.put(mediaItemType, contentRetrieverMap.get(SongsRetriever.class));
                    break;
                case FOLDER:
                    map.put(mediaItemType, contentRetrieverMap.get(SongsFromFolderRetriever.class));
                    break;
                case FOLDERS:
                    map.put(mediaItemType, contentRetrieverMap.get(FoldersRetriever.class));
                    break;
                case ROOT:
                    map.put(mediaItemType, contentRetrieverMap.get(RootRetriever.class));
                    break;
                default:
                    break;
            }
        }
        return map;
    }

}
