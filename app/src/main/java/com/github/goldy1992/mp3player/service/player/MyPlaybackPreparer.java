package com.github.goldy1992.mp3player.service.player;

import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.v4.media.MediaBrowserCompat.MediaItem;
import android.util.Log;

import com.github.goldy1992.mp3player.commons.MediaItemUtils;
import com.github.goldy1992.mp3player.service.MyControlDispatcher;
import com.github.goldy1992.mp3player.service.PlaylistManager;
import com.github.goldy1992.mp3player.service.library.ContentManager;
import com.google.android.exoplayer2.ControlDispatcher;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import javax.inject.Inject;

import static com.github.goldy1992.mp3player.commons.Constants.ID_DELIMITER;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

public class MyPlaybackPreparer implements MediaSessionConnector.PlaybackPreparer {

    private static final String LOG_TAG = "PLAYBACK_PREPARER";

    private final ContentManager contentManager;
    private final ExoPlayer exoPlayer;
    private final MyControlDispatcher myControlDispatcher;
    private final MediaSourceFactory mediaSourceFactory;
    private final PlaylistManager playlistManager;

    @Inject
    public MyPlaybackPreparer(ExoPlayer exoPlayer,
                              ContentManager contentManager,
                              MediaSourceFactory mediaSourceFactory,
                              MyControlDispatcher myControlDispatcher,
                              PlaylistManager playlistManager) {
        this.exoPlayer = exoPlayer;
        this.contentManager = contentManager;
        this.myControlDispatcher = myControlDispatcher;
        this.mediaSourceFactory = mediaSourceFactory;

        this.playlistManager = playlistManager;
        List<MediaItem> currentPlaylist = playlistManager.getPlaylist();
        if (isNotEmpty(currentPlaylist)) {
            String trackId = MediaItemUtils.getMediaId(currentPlaylist.get(0));
            preparePlaylist(false, trackId, currentPlaylist);
        }
    }
    @Override
    public long getSupportedPrepareActions() {
        return MediaSessionConnector.PlaybackPreparer.ACTIONS;
    }

    @Override
    public void onPrepare(boolean playWhenReady) {
        Log.i(LOG_TAG, "called onPrepare, play when ready: " + playWhenReady);
        List<MediaItem> mediaItems = playlistManager.getPlaylist();
        if (isNotEmpty(mediaItems)) {
            MediaItem currentMediaItem = playlistManager.getCurrentItem();
            if (null != currentMediaItem) {
                String currentId = currentMediaItem.getMediaId();
                preparePlaylist(playWhenReady, currentId, mediaItems);
            }
        }
    }

    @Override
    public void onPrepareFromMediaId(String mediaId, boolean playWhenReady, Bundle extras) {
        String trackId = extractTrackId(mediaId);
        if (null != trackId) {
            List<MediaItem> results = contentManager.getPlaylist(mediaId);
            this.playlistManager.createNewPlaylist(results);
            preparePlaylist(playWhenReady, trackId, results);
        }
    }

    private void preparePlaylist(boolean playWhenReady, String trackId, List<MediaItem> results) {
        if (null != results) {
            ConcatenatingMediaSource concatenatingMediaSource = new ConcatenatingMediaSource();
            ListIterator<MediaItem> resultsIterator = results.listIterator();

            while(resultsIterator.hasNext()) {
                MediaItem currentMediaItem = resultsIterator.next();
                Uri currentUri = MediaItemUtils.getMediaUri(currentMediaItem);
                MediaSource src = mediaSourceFactory.createMediaSource(currentUri);
                if (null != src) {
                    concatenatingMediaSource.addMediaSource(src);
                } else {
                    resultsIterator.remove();
                }
            }
            int uriToPlayIndex = getIndexOfCurrentTrack(trackId, results);

            if (concatenatingMediaSource.getSize() > 0) {
                this.exoPlayer.prepare(concatenatingMediaSource);
                this.myControlDispatcher.dispatchSeekTo(exoPlayer, uriToPlayIndex, 0L);
                this.myControlDispatcher.dispatchSetPlayWhenReady(exoPlayer, playWhenReady);
            }
        } // if
    }

    @Override
    public void onPrepareFromSearch(String query, boolean playWhenReady, Bundle extras) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onPrepareFromUri(Uri uri, boolean playWhenReady, Bundle extras) {

        MediaItem result = contentManager.getItem(uri);
        List<MediaItem> playlist = new ArrayList<>();
        playlist.add(result);
        this.playlistManager.createNewPlaylist(playlist);

        preparePlaylist(playWhenReady, MediaItemUtils.getMediaId(result), playlist);

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

    private int getIndexOfCurrentTrack(String trackId, List<MediaItem> items) {
        for (int i = 0; i < items.size(); i++) {
            MediaItem currentMediaItem = items.get(i);
            String id = MediaItemUtils.getMediaId(currentMediaItem);
            if (id != null && id.equals(trackId)) {
                return i;
            } // if
        } // for
        return 0;
    }
}
