package com.github.goldy1992.mp3player.service.library.content.searcher;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.provider.MediaStore;

import com.github.goldy1992.mp3player.commons.MediaItemType;
import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds;
import com.github.goldy1992.mp3player.service.library.content.parser.SongResultsParser;
import com.github.goldy1992.mp3player.service.library.search.Song;
import com.github.goldy1992.mp3player.service.library.search.SongDao;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.github.goldy1992.mp3player.service.library.content.Projections.SONG_PROJECTION;

public class SongSearcher extends ContentResolverSearcher<Song> {

    private static final String PARAMETER = "?";
    private MediaItemTypeIds mediaItemTypeIds;

    @Inject
    public SongSearcher(ContentResolver contentResolver,
                        SongResultsParser resultsParser,
                        MediaItemTypeIds mediaItemTypeIds,
                        SongDao songDao) {
        super(contentResolver, resultsParser, null, songDao);
        this.mediaItemTypeIds = mediaItemTypeIds;
    }

    @Override
    String getIdPrefix() {
        return mediaItemTypeIds.getId(MediaItemType.SONG);
    }

    @Override
    String[] getProjection() {
        return SONG_PROJECTION.toArray(new String[0]);
    }

    @Override
    public MediaItemType getSearchCategory() {
        return MediaItemType.SONGS;
    }

    @Override
    public Cursor performSearchQuery(String query) {
        List<Song> results =  searchDatabase.query(query);
        List<String> ids = new ArrayList<>();
        List<String> parameters = new ArrayList<>();
        if (results != null && !results.isEmpty()) {
            for (Song song : results) {
                ids.add(song.getId());
                parameters.add(PARAMETER);
            }

        }


        String WHERE_CLAUSE = BaseColumns._ID + " IN(" + StringUtils.join(parameters, ", ") + ") COLLATE NOCASE";
        String[] WHERE_ARGS = ids.toArray(new String[ids.size()]);
        return  contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                getProjection(),
                WHERE_CLAUSE, WHERE_ARGS, null);
    }
}
