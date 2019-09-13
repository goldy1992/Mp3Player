package com.example.mike.mp3player.service.library.content.searcher;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;

import com.example.mike.mp3player.commons.MediaItemType;
import com.example.mike.mp3player.service.library.content.parser.ResultsParser;
import com.example.mike.mp3player.service.library.search.SearchDatabase;
import com.example.mike.mp3player.service.library.search.Song;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.example.mike.mp3player.service.library.content.Projections.SONG_PROJECTION;

public class SongSearcher extends ContentResolverSearcher {

    private static final String PARAMETER = "?";

    public SongSearcher(ContentResolver contentResolver, ResultsParser resultsParser, String idPrefix, SearchDatabase searchDatabase) {
        super(contentResolver, resultsParser, null, idPrefix, searchDatabase);
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
        String searchQuery = StringUtils.stripAccents(query);
        List<Song> results =  searchDatabase.songDao().query(searchQuery);
        List<String> ids = new ArrayList<>();
        List<String> parameters = new ArrayList<>();
        if (results != null && !results.isEmpty()) {
            for (Song song : results) {
                ids.add(song.getId());
                parameters.add(PARAMETER);
            }

        }


        String WHERE_CLAUSE = MediaStore.Audio.Media._ID + " IN(" + StringUtils.join(parameters, ", ") + ") COLLATE NOCASE";
        String[] WHERE_ARGS = ids.toArray(new String[ids.size()]);
        return  contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                getProjection(),
                WHERE_CLAUSE, WHERE_ARGS, null);
    }
}
