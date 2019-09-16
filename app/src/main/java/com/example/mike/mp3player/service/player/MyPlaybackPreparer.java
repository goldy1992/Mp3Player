package com.example.mike.mp3player.service.player;

import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.v4.media.MediaBrowserCompat;
import android.util.Log;

import com.example.mike.mp3player.commons.MediaItemUtils;
import com.example.mike.mp3player.service.library.ContentManager;
import com.google.android.exoplayer2.ControlDispatcher;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.FileDataSource;
import com.google.android.exoplayer2.upstream.FileDataSourceFactory;

import java.util.Arrays;
import java.util.List;

import static android.support.v4.media.session.PlaybackStateCompat.ACTION_PREPARE_FROM_MEDIA_ID;
import static com.example.mike.mp3player.commons.Constants.ID_DELIMITER;

public class MyPlaybackPreparer implements MediaSessionConnector.PlaybackPreparer {

    private static final String LOG_TAG = "PLAYBACK_PREPARER";

    private final ContentManager contentManager;

    public MyPlaybackPreparer(ContentManager contentManager) {
        this.contentManager = contentManager;
    }
    @Override
    public long getSupportedPrepareActions() {
        return 0;
    }

    @Override
    public void onPrepare(boolean playWhenReady) {

    }

    @Override
    public void onPrepareFromMediaId(String mediaId, boolean playWhenReady, Bundle extras) {
        String trackId = extractTrackId(mediaId);
        if (null != trackId) {
            List<MediaBrowserCompat.MediaItem> results = contentManager.getPlaylist(mediaId);
            if (null != results) {

                ConcatenatingMediaSource concatenatingMediaSource = new ConcatenatingMediaSource();


                DataSource.Factory dataSrcFactory = new DataSource.Factory() {
                    @Override
                    public DataSource createDataSource() {
                        return fileDataSource;
                    }
                };
                ProgressiveMediaSource.Factory factory = new ProgressiveMediaSource.Factory(dataSrcFactory);

                Uri uriToPlay = null;
                Uri followingUri = null;
                for (MediaBrowserCompat.MediaItem m : results) {
                    String id = MediaItemUtils.getMediaId(m);
                    if (id != null && id.equals(trackId)) {
                        uriToPlay = m.getDescription().getMediaUri();
                        MediaSource src = factory.createMediaSource(MediaItemUtils.getMediaUri(m));
                        concatenatingMediaSource.addMediaSource(src);
                        break;
                    }
                }
                if (uriToPlay == null) {
                    Log.e(LOG_TAG, "failed to find requested uri in collection");
                    return;
                }
            }
        }
    }

    @Override
    public void onPrepareFromSearch(String query, boolean playWhenReady, Bundle extras) {

    }

    @Override
    public void onPrepareFromUri(Uri uri, boolean playWhenReady, Bundle extras) {

    }

    @Override
    public boolean onCommand(Player player, ControlDispatcher controlDispatcher, String command, Bundle extras, ResultReceiver cb) {
        return false;
    }

    private String extractTrackId(String mediaId) {
        if (null != mediaId) {
            List<String> splitId = Arrays.asList(mediaId.split(ID_DELIMITER));
            if (!splitId.isEmpty()) {
                return splitId.get(splitId.size() - 1);
            }
        }
        else {
            Log.e(LOG_TAG, "received null mediaId");
        }
        return null;
    }
}
