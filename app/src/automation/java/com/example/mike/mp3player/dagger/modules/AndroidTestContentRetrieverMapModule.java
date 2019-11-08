package com.example.mike.mp3player.dagger.modules;

import android.content.ContentResolver;
import android.os.Handler;

import com.example.mike.mp3player.commons.MediaItemType;
import com.example.mike.mp3player.service.library.content.parser.ResultsParser;
import com.example.mike.mp3player.service.library.content.retriever.ContentRetriever;
import com.example.mike.mp3player.service.library.content.retriever.FoldersRetriever;
import com.example.mike.mp3player.service.library.content.retriever.RootRetriever;
import com.example.mike.mp3player.service.library.content.retriever.SongsFromFolderRetriever;
import com.example.mike.mp3player.service.library.content.retriever.SongsRetriever;
import com.example.mike.mp3player.service.library.content.retriever.SongsRetrieverTestImpl;
import com.example.mike.mp3player.service.library.search.SearchDatabase;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static com.example.mike.mp3player.commons.MediaItemType.FOLDER;
import static com.example.mike.mp3player.commons.MediaItemType.SONG;

@Module
public class AndroidTestContentRetrieverMapModule {

    @Singleton
    @Provides
    public Map<Class<? extends ContentRetriever>, ContentRetriever> provideContentResolverRetrieverMap(ContentResolver contentResolver,
                                                                                                       Map<MediaItemType, String> ids,
                                                                                                       Map<MediaItemType, ResultsParser> mediaItemBuilderMap,SearchDatabase searchDatabase,
                                                                                                       @Named("worker") Handler handler) {
        Map<Class<? extends ContentRetriever>, ContentRetriever> mapToReturn = new HashMap<>();
        RootRetriever rootRetriever = new RootRetriever(ids);
        mapToReturn.put(RootRetriever.class, rootRetriever);
        SongsRetriever songsRetriever = new SongsRetrieverTestImpl(contentResolver, mediaItemBuilderMap.get(MediaItemType.SONG), searchDatabase.songDao(), handler);
        mapToReturn.put(SongsRetriever.class, songsRetriever);
        FoldersRetriever foldersRetriever = new FoldersRetriever(contentResolver, mediaItemBuilderMap.get(FOLDER), searchDatabase.folderDao(), handler);
        mapToReturn.put(FoldersRetriever.class, foldersRetriever);
        SongsFromFolderRetriever songsFromFolderRetriever = new SongsFromFolderRetriever(contentResolver, mediaItemBuilderMap.get(SONG), searchDatabase.songDao(), handler);
        mapToReturn.put(SongsFromFolderRetriever.class, songsFromFolderRetriever);
        return mapToReturn;
    }
}
