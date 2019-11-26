package com.github.goldy1992.mp3player.service.library.content.retriever;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.provider.MediaStore;
import android.support.v4.media.MediaBrowserCompat;

import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds;
import com.github.goldy1992.mp3player.service.library.content.parser.SongResultsParser;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.github.goldy1992.mp3player.service.library.content.Projections.SONG_PROJECTION;

@Singleton
public class MediaItemFromIdRetriever {
    private static final String[] PROJECTION = SONG_PROJECTION.toArray(new String[0]);
    private final ContentResolver contentResolver;
    private final SongResultsParser songResultsParser;

    @Inject
    public MediaItemFromIdRetriever(Context context,
                                ContentResolver contentResolver,
                                SongResultsParser resultsParser,
                                MediaMetadataRetriever mediaMetadataRetriever,
                                MediaItemTypeIds mediaItemTypeIds) {
        this.contentResolver = contentResolver;
        this.songResultsParser = resultsParser;
    }

    public MediaBrowserCompat.MediaItem getItem(long id) {
        final String WHERE = MediaStore.Audio.Media._ID + " = ?";
        final String[] WHERE_ARGS = {String.valueOf(id)};

        Cursor cursor =  contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                PROJECTION, WHERE, WHERE_ARGS, null);


        if (null != cursor) {
            List<MediaBrowserCompat.MediaItem> results = songResultsParser.create(cursor, "");
            if (CollectionUtils.isNotEmpty(results)) {
                return results.get(0);
            }
        }
        return null;
    }
}
