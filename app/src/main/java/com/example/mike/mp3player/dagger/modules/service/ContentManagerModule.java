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
    public Map<Class<? extends ContentResolverRetriever>, ContentResolverRetriever> provideContentResolverRetrieverMap(ContentResolver contentResolver,
                                                                            EnumMap<MediaItemType, String> ids) {
        Map<Class<? extends ContentResolverRetriever>, ContentResolverRetriever> mapToReturn = new HashMap<>();
        SongsRetriever songsRetriever = new SongsRetriever(contentResolver, ids.get(MediaItemType.SONG), ids.get(MediaItemType.SONGS));
        mapToReturn.put(SongsRetriever.class, songsRetriever);
        FoldersRetriever foldersRetriever = new FoldersRetriever(contentResolver, ids.get(MediaItemType.FOLDER), ids.get(MediaItemType.FOLDERS));
        mapToReturn.put(FoldersRetriever.class, foldersRetriever);
        SongsFromFolderRetriever songsFromFolderRetriever = new SongsFromFolderRetriever(contentResolver, ids.get(MediaItemType.SONG), ids.get(MediaItemType.FOLDER));
        mapToReturn.put(SongsFromFolderRetriever.class, songsFromFolderRetriever);
        return mapToReturn;
    }

    @Singleton
    @Provides
    public RootRetriever provideRootRetriever(EnumMap<MediaItemType, String> ids) {
        return new RootRetriever(ids);
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

    @Singleton
    @Provides
    public EnumMap<MediaItemType, ContentRetriever> getContentRetrievers(RootRetriever rootRetriever,
        Map<Class<? extends ContentResolverRetriever>, ContentResolverRetriever> contentResolverRetrieverMap) {
        EnumMap<MediaItemType, ContentRetriever> map = new EnumMap<>(MediaItemType.class);
        for (MediaItemType mediaItemType : MediaItemType.values()) {
            switch (mediaItemType) {
                case SONGS:
                case SONG:
                    map.put(mediaItemType, contentResolverRetrieverMap.get(SongsRetriever.class));
                    break;
                case FOLDER:
                    map.put(mediaItemType, contentResolverRetrieverMap.get(SongsFromFolderRetriever.class));
                    break;
                case FOLDERS: map.put(mediaItemType, contentResolverRetrieverMap.get(FoldersRetriever.class));
                    break;
                case ROOT: map.put(mediaItemType, rootRetriever);
                    break;
                default:
                    break;
            }
        }
        return map;
    }

    private ContentResolverRetriever getContentResolverRetrieverFromSet(MediaItemType mediaItemType,
                                                                        Set<ContentResolverRetriever> contentResolverRetrieverSet) {
        for (ContentResolverRetriever contentResolverRetriever : contentResolverRetrieverSet) {
            if (contentResolverRetriever.getType() == mediaItemType) {
                return contentResolverRetriever;
            }
        }
        return null;
    }



}
