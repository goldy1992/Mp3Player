package com.example.mike.mp3player.service.library.content.retriever;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.media.MediaBrowserCompat.MediaItem;

import com.example.mike.mp3player.commons.MediaItemBuilder;
import com.example.mike.mp3player.service.library.content.parser.SongResultsParser;

import org.apache.commons.collections4.CollectionUtils;

import java.io.File;
import java.util.List;

import static android.media.MediaMetadataRetriever.METADATA_KEY_ARTIST;
import static android.media.MediaMetadataRetriever.METADATA_KEY_DURATION;
import static android.media.MediaMetadataRetriever.METADATA_KEY_TITLE;
import static com.example.mike.mp3player.service.library.content.Projections.SONG_PROJECTION;

public class SongFromUriRetriever  {

    private static final String[] PROJECTION = SONG_PROJECTION.toArray(new String[0]);
    private final ContentResolver contentResolver;
    private final SongResultsParser songResultsParser;
    private final String idPrefix;
    private final Context context;

    public SongFromUriRetriever(Context context, ContentResolver contentResolver, SongResultsParser resultsParser, String idPrefix) {
        this.context = context;
        this.contentResolver = contentResolver;
        this.songResultsParser = resultsParser;
        this.idPrefix = idPrefix;
    }

    public MediaItem getSong(Uri uri) {

        if (uri != null && uri.getScheme() != null) {
            if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
                MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                mmr.setDataSource(context, uri);
                MediaItemBuilder mediaItemBuilder = new MediaItemBuilder("1");
                mediaItemBuilder.setMediaUri(uri);
                mediaItemBuilder.setAlbumArtImage(mmr.getEmbeddedPicture());
                mediaItemBuilder.setTitle(mmr.extractMetadata(METADATA_KEY_TITLE));
                mediaItemBuilder.setArtist(mmr.extractMetadata(METADATA_KEY_ARTIST));
                mediaItemBuilder.setDuration(Long.parseLong(mmr.extractMetadata(METADATA_KEY_DURATION)));
                return mediaItemBuilder.build();
            } else {
                Cursor cursor =  contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        SONG_PROJECTION.toArray(new String[0]),
                        null, null, null);

                if (null != cursor) {
                    List<MediaItem> results = songResultsParser.create(cursor, idPrefix);
                    if (CollectionUtils.isNotEmpty(results)) {
                        return results.get(0);
                    }
                }
            }
        }
        return null;
    }

    private String getAbsolutePathFromUri(Uri uri) {
        File file = new File(uri.getPath());
        return file.getAbsolutePath();
    }
}
