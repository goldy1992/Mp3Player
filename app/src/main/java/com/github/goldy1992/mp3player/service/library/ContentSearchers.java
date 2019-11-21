package com.github.goldy1992.mp3player.service.library;

import com.github.goldy1992.mp3player.commons.MediaItemType;
import com.github.goldy1992.mp3player.service.library.content.searcher.ContentSearcher;
import com.github.goldy1992.mp3player.service.library.content.searcher.FolderSearcher;
import com.github.goldy1992.mp3player.service.library.content.searcher.SongSearcher;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.github.goldy1992.mp3player.commons.MediaItemType.FOLDER;

@Singleton
public class ContentSearchers {

    /** */
    EnumMap<MediaItemType, ContentSearcher> contentSearcherMap;

    @Inject
    public ContentSearchers(SongSearcher songSearcher,
                            FolderSearcher folderSearcher) {
        EnumMap<MediaItemType, ContentSearcher> map = new EnumMap<>(MediaItemType.class);
        map.put(MediaItemType.SONG, songSearcher);
        map.put(FOLDER, folderSearcher);
        this.contentSearcherMap = map;
    }

    public ContentSearcher get(MediaItemType mediaItemType) {
        return this.contentSearcherMap.get(mediaItemType);
    }

    public Collection<ContentSearcher> getAll() {
        return contentSearcherMap.values();
    }

}
