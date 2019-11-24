package com.github.goldy1992.mp3player.dagger.modules;

import dagger.Module;

@Module
public class AndroidTestContentRetrieverMapModule {

//    @Singleton
//    @Provides
//    public Map<Class<? extends ContentRetriever>, ContentRetriever> provideContentResolverRetrieverMap(ContentResolver contentResolver,
//                                                                                                       Map<MediaItemType, String> ids,
//                                                                                                       Map<MediaItemType, ResultsParser> mediaItemBuilderMap,SearchDatabase searchDatabase,
//                                                                                                       @Named("worker") Handler handler) {
//        Map<Class<? extends ContentRetriever>, ContentRetriever> mapToReturn = new HashMap<>();
//        RootRetriever rootRetriever = new RootRetriever(ids);
//        mapToReturn.put(RootRetriever.class, rootRetriever);
//        SongsRetriever songsRetriever = new SongsRetrieverTestImpl(contentResolver, mediaItemBuilderMap.get(MediaItemType.SONG), searchDatabase.songDao(), handler);
//        mapToReturn.put(SongsRetriever.class, songsRetriever);
//        FoldersRetriever foldersRetriever = new FoldersRetrieverTestImpl(contentResolver, mediaItemBuilderMap.get(FOLDER), searchDatabase.folderDao(), handler);
//        mapToReturn.put(FoldersRetriever.class, foldersRetriever);
//        SongsFromFolderRetriever songsFromFolderRetriever = new SongsFromFolderRetriever(contentResolver, mediaItemBuilderMap.get(SONG), searchDatabase.songDao(), handler);
//        mapToReturn.put(SongsFromFolderRetriever.class, songsFromFolderRetriever);
//        return mapToReturn;
//    }
}
