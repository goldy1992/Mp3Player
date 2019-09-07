package com.example.mike.mp3player.service.session;

import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import androidx.annotation.NonNull;

import com.example.mike.mp3player.service.PlaybackManager;
import com.example.mike.mp3player.service.player.MediaPlayerAdapter;

import javax.inject.Inject;

import static com.example.mike.mp3player.commons.Constants.REPEAT_MODE;
import static com.example.mike.mp3player.commons.Constants.SHUFFLE_MODE;
import static com.example.mike.mp3player.commons.Constants.UNKNOWN;
import static com.example.mike.mp3player.commons.MediaItemUtils.getAlbumArtPath;
import static com.example.mike.mp3player.commons.MediaItemUtils.getArtist;
import static com.example.mike.mp3player.commons.MediaItemUtils.getMediaId;
import static com.example.mike.mp3player.commons.MediaItemUtils.getTitle;



public class MediaSessionAdapter {

    private final MediaSessionCompat mediaSession;
    private final PlaybackManager playbackManager;
    private final MediaPlayerAdapter mediaPlayerAdapter;

    @Inject
    public MediaSessionAdapter(@NonNull MediaSessionCompat mediaSession, PlaybackManager playbackManager,
                               MediaPlayerAdapter mediaPlayerAdapter) {
        this.mediaSession = mediaSession;
        this.playbackManager = playbackManager;
        this.mediaPlayerAdapter = mediaPlayerAdapter;
    }

    public void updatePlaybackState(long actions) {
        PlaybackStateCompat state = getCurrentPlaybackState(actions);
        mediaSession.setPlaybackState(state);
    }
    public PlaybackStateCompat getCurrentPlaybackState(long actions) {
        Bundle ex = new Bundle();
        ex.putInt(REPEAT_MODE, mediaPlayerAdapter.getRepeatMode());
        ex.putInt(SHUFFLE_MODE, playbackManager.getShuffleMode());
        return new PlaybackStateCompat.Builder()
                .setActions(actions)
                .setExtras(ex)
                .setState(mediaPlayerAdapter.getCurrentState(),
                        mediaPlayerAdapter.getCurrentPosition(),
                        mediaPlayerAdapter.getCurrentPlaybackSpeed())
                .build();
    }

    public void updateMetaData() {
        mediaSession.setMetadata(getCurrentMetaData());
    }

    /**
     * added this method instead of calling both of the playback and meta data methods when
     * wanting to update the Media Session
     * @param actions
     */
    public void updateAll(long actions) {
        updatePlaybackState(actions);
        updateMetaData();
    }
    public MediaMetadataCompat getCurrentMetaData() {
        MediaMetadataCompat.Builder builder = new MediaMetadataCompat.Builder();
        builder.putLong(MediaMetadataCompat.METADATA_KEY_DURATION, mediaPlayerAdapter.getCurrentDuration());

        MediaBrowserCompat.MediaItem currentItem = playbackManager.getCurrentItem();

        String mediaId = getMediaId(currentItem);
        builder.putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, mediaId);

        String title = getTitle(currentItem);
        builder.putString(MediaMetadataCompat.METADATA_KEY_TITLE, null != title ? title : UNKNOWN);

        String artist = getArtist(currentItem);
        builder.putString(MediaMetadataCompat.METADATA_KEY_ARTIST, null != artist ? artist : UNKNOWN);

        String albumArt = getAlbumArtPath(currentItem);
        builder.putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, albumArt);
        return builder.build();
    }

    public void setQueue(MediaBrowserCompat.MediaItem item) {
        // TODO: find a way to implement queue
       // mediaSession.setQueue(playbackManager.onAddQueueItem(item));
    }

    public void setActive(boolean isActive) {
        this.mediaSession.setActive(isActive);
    }

    public MediaSessionCompat.Token getMediaSessionToken() {
        return mediaSession.getSessionToken();
    }


}
