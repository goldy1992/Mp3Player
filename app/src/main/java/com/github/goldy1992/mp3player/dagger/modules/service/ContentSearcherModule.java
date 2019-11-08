package com.github.goldy1992.mp3player.dagger.modules.service;

import android.content.ContentResolver;

import com.github.goldy1992.mp3player.commons.MediaItemType;
import com.github.goldy1992.mp3player.service.library.content.filter.FoldersResultFilter;
import com.github.goldy1992.mp3player.service.library.content.parser.ResultsParser;
import com.github.goldy1992.mp3player.service.library.content.searcher.ContentSearcher;
import com.github.goldy1992.mp3player.service.library.content.searcher.FolderSearcher;
import com.github.goldy1992.mp3player.service.library.content.searcher.SongSearcher;
import com.github.goldy1992.mp3player.service.library.search.SearchDatabase;

import java.util.EnumMap;
import java.util.Map;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static com.github.goldy1992.mp3player.commons.MediaItemType.FOLDER;

@Module
public class ContentSearcherModule {

    @Provides
    @Singleton
    public Map<MediaItemType, ContentSearcher> provideSearchSearcherMap(ContentResolver contentResolver,
                                                                        Map<MediaItemType, String> idMap,
                                                                        Map<MediaItemType, ResultsParser> mediaItemBuilderMap,
                                                                        SearchDatabase searchDatabase) {
        EnumMap<MediaItemType, ContentSearcher> map = new EnumMap<>(MediaItemType.class);
        map.put(MediaItemType.SONG, new SongSearcher(contentResolver, mediaItemBuilderMap.get(MediaItemType.SONG), idMap.get(MediaItemType.SONG), searchDatabase.songDao()));
        map.put(FOLDER, new FolderSearcher(contentResolver, mediaItemBuilderMap.get(FOLDER), new FoldersResultFilter(), idMap.get(FOLDER), searchDatabase.folderDao()));
        return map;
    }
}
