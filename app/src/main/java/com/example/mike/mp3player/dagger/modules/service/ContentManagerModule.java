package com.example.mike.mp3player.dagger.modules.service;

import android.content.ContentResolver;
import android.content.Context;

import com.example.mike.mp3player.commons.MediaItemType;
import com.example.mike.mp3player.service.library.contentretriever.ContentResolverRetriever;
import com.example.mike.mp3player.service.library.contentretriever.ContentRetriever;
import com.example.mike.mp3player.service.library.contentretriever.FolderRetriever;
import com.example.mike.mp3player.service.library.contentretriever.RootRetriever;
import com.example.mike.mp3player.service.library.contentretriever.SongsRetriever;
import com.example.mike.mp3player.service.library.utils.IdGenerator;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

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
    public Set<ContentResolverRetriever> provideContentResolverRetrieverMap(ContentResolver contentResolver,
                                                                            EnumMap<MediaItemType, String> ids) {
        Set<ContentResolverRetriever> returnSet = new HashSet<>();
        SongsRetriever songsRetriever = new SongsRetriever(contentResolver, ids.get(MediaItemType.SONG));
        FolderRetriever folderRetriever = new FolderRetriever(contentResolver, ids.get(MediaItemType.FOLDER));
        returnSet.add(songsRetriever);
        returnSet.add(folderRetriever);
        return returnSet;
    }

    @Singleton
    @Provides
    public RootRetriever provideRootRetriever(EnumMap<MediaItemType, String> ids) {
        Set<MediaItemType> rootIds = MediaItemType.PARENT_TO_CHILD_MAP.get(MediaItemType.ROOT);
        EnumMap<MediaItemType, String> childIds = new EnumMap<>(MediaItemType.class);
        for (MediaItemType m : rootIds) {
            childIds.put(m, ids.get(m));
        }
        return new RootRetriever(childIds);
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
    public EnumMap<MediaItemType, ContentRetriever> getContentRetrievers(RootRetriever rootRetriever, Set<ContentResolverRetriever> contentResolverRetrieverSet) {
        EnumMap<MediaItemType, ContentRetriever> map = new EnumMap<>(MediaItemType.class);
        for (MediaItemType mediaItemType : MediaItemType.values()) {
            switch (mediaItemType) {
                case FOLDER:
                case SONGS:
                case SONG:
                    map.put(mediaItemType, getContentResolverRetrieverFromSet(MediaItemType.SONG, contentResolverRetrieverSet));
                    break;
                case FOLDERS: map.put(mediaItemType, getContentResolverRetrieverFromSet(MediaItemType.FOLDER, contentResolverRetrieverSet));
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
