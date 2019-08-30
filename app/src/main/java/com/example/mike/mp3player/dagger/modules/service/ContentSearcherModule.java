package com.example.mike.mp3player.dagger.modules.service;

import android.content.ContentResolver;

import com.example.mike.mp3player.commons.MediaItemType;
import com.example.mike.mp3player.service.library.content.builder.MediaItemBuilder;
import com.example.mike.mp3player.service.library.content.retriever.ContentRetriever;
import com.example.mike.mp3player.service.library.content.retriever.RootRetriever;
import com.example.mike.mp3player.service.library.content.searcher.ContentSearcher;
import com.example.mike.mp3player.service.library.content.searcher.FolderSearcher;
import com.example.mike.mp3player.service.library.content.searcher.SongSearcher;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static com.example.mike.mp3player.commons.MediaItemType.FOLDER;

@Module
public class ContentSearcherModule {

    @Provides
    @Singleton
    public Map<MediaItemType, ContentSearcher> provideSearchSearcherMap(ContentResolver contentResolver,
                                                                        Map<MediaItemType, String> idMap,
                                                                        Map<MediaItemType, MediaItemBuilder> mediaItemBuilderMap) {
        EnumMap<MediaItemType, ContentSearcher> map = new EnumMap<>(MediaItemType.class);
        map.put(MediaItemType.SONG, new SongSearcher(contentResolver, mediaItemBuilderMap.get(MediaItemType.SONG), idMap.get(MediaItemType.SONG)));
        map.put(FOLDER, new FolderSearcher(contentResolver, mediaItemBuilderMap.get(FOLDER), idMap.get(FOLDER)));
        return map;
    }
}
