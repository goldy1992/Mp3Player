package com.github.goldy1992.mp3player.dagger.modules.service;

import android.content.ContentResolver;

import com.github.goldy1992.mp3player.commons.MediaItemType;
import com.github.goldy1992.mp3player.service.library.ContentManager;
import com.github.goldy1992.mp3player.service.library.MediaLibrary;
import com.github.goldy1992.mp3player.service.library.content.retriever.ContentResolverRetriever;

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
