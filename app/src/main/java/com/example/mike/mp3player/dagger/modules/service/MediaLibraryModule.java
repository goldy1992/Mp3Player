package com.example.mike.mp3player.dagger.modules.service;

import android.content.ContentResolver;

import com.example.mike.mp3player.service.library.ContentManager;
import com.example.mike.mp3player.service.library.content.retriever.ContentResolverRetriever;
import com.example.mike.mp3player.commons.MediaItemType;
import com.example.mike.mp3player.service.library.MediaLibrary;

import java.util.EnumMap;
import java.util.Map;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MediaLibraryModule {

    @Provides
    @Singleton
    public MediaLibrary provideMediaLibrary(ContentManager contentManager) {
        return new MediaLibrary(contentManager);
    }



    @Provides
    @Singleton
    public Map<MediaItemType, ContentResolverRetriever> provideMediaItemTypeToContentRetrieverMap(ContentResolver contentResolver) {
        Map<MediaItemType, ContentResolverRetriever> map = new EnumMap<>(MediaItemType.class);

        return map;
    }
}
