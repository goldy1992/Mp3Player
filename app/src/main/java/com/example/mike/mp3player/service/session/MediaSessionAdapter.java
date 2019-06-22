package com.example.mike.mp3player.service.session;

import android.os.Bundle;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import androidx.annotation.NonNull;

import com.example.mike.mp3player.service.PlaybackManager;
import com.example.mike.mp3player.service.library.utils.ValidMetaDataUtil;
import com.example.mike.mp3player.service.player.MediaPlayerAdapterBase;

import javax.inject.Inject;

import static com.example.mike.mp3player.commons.Constants.NO_ACTION;
import static com.example.mike.mp3player.commons.Constants.REPEAT_MODE;
import static com.example.mike.mp3player.commons.Constants.SHUFFLE_MODE;
import static com.example.mike.mp3player.commons.Constants.UNKNOWN;
import static com.example.mike.mp3player.commons.MetaDataKeys.STRING_METADATA_KEY_ARTIST;

public class MediaSessionAdapter {

    private final MediaSessionCompat mediaSession;
    private final PlaybackManager playbackManager;
    private final MediaPlayerAdapterBase mediaPlayerAdapter;

    @Inject
    public MediaSessionAdapter(@NonNull MediaSessionCompat mediaSession, PlaybackManager playbackManager,
                               MediaPlayerAdapterBase mediaPlayerAdapterBase) {
        this.mediaSession = mediaSession;
        this.playbackManager = playbackManager;
        this.mediaPlayerAdapter = mediaPlayerAdapterBase;
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
    public void updateAll() {
        updateAll(NO_ACTION);
    }

    public MediaMetadataCompat getCurrentMetaData() {
        MediaMetadataCompat.Builder builder = new MediaMetadataCompat.Builder();
        builder.putLong(MediaMetadataCompat.METADATA_KEY_DURATION, mediaPlayerAdapter.getCurrentDuration());

        MediaSessionCompat.QueueItem currentItem = playbackManager.getCurrentItem();
        if(ValidMetaDataUtil.validMediaId(currentItem)) {
            builder.putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, currentItem.getDescription()
                    .getMediaId());
        }

        if (ValidMetaDataUtil.validTitle(currentItem)) {
            builder.putString(MediaMetadataCompat.METADATA_KEY_TITLE, currentItem.getDescription()
                    .getTitle().toString());
        } else {
            builder.putString(MediaMetadataCompat.METADATA_KEY_TITLE, UNKNOWN);
        }

        if (ValidMetaDataUtil.validArtist(currentItem)) {
            builder.putString(MediaMetadataCompat.METADATA_KEY_ARTIST, currentItem.getDescription()
                    .getExtras().getString(STRING_METADATA_KEY_ARTIST));
        } else {
            builder.putString(MediaMetadataCompat.METADATA_KEY_ARTIST, UNKNOWN);
        }
        return builder.build();
    }

    public void setQueue(MediaSessionCompat.QueueItem item) {
        mediaSession.setQueue(playbackManager.onAddQueueItem(item));
    }

    public void setActive(boolean isActive) {
        this.mediaSession.setActive(isActive);
    }

    public MediaSessionCompat.Token getMediaSessionToken() {
        return mediaSession.getSessionToken();
    }


}
