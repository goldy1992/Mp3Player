package com.example.mike.mp3player.service;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.net.Uri;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import java.io.IOException;

public class MyMediaPlayerAdapter {

    private static final float DEFAULT_SPEED = 1.0f;
    private static final float MINIMUM_PLAYBACK_SPEED = 0.25f;
    private static final float MAXIMUM_PLAYBACK_SPEED = 2f;
    private static final String LOG_TAG = "MEDIA_PLAYER_ADAPTER";
    private MediaPlayer mediaPlayer;
    private AudioFocusManager audioFocusManager;
    private Context context;
    /**
     * initialise to paused so the player doesn't start playing immediately
     */
    @PlaybackStateCompat.State
    private int currentState = PlaybackStateCompat.STATE_PAUSED;;
    private float currentPlaybackSpeed = DEFAULT_SPEED;
    private boolean isPrepared = false;

    public MyMediaPlayerAdapter(Context context) {
        this.context = context;
    }

    /**
     * TODO: Provide a track that is prepared for when the service starts, to stop the Activities from
     * crashing
     */
    public void init(Uri uri) {
        resetPlayer();
        prepareFromUri(uri);
        audioFocusManager = new AudioFocusManager(context, this);
        audioFocusManager.init();
    }

    public void play() {
        if (!prepare()) {
            return;
        }

        if (audioFocusManager.requestAudioFocus() || isPlaying()) {
            try {
                // Set the session active  (and update metadata and state)
                currentState = PlaybackStateCompat.STATE_PLAYING;
                // start the player (custom call)
                getMediaPlayer().start();
                updatePlaybackParameters();
                //            // Register BECOME_NOISY BroadcastReceiver
//            registerReceiver(myNoisyAudioStreamReceiver, intentFilter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean prepareFromUri(Uri uri) {
        if (null != uri)
        {
            try {
                resetPlayer();
                setCurrentUri(uri);
                prepare();
                updatePlaybackParameters();
                return true;
            } catch (Exception ex) {
                Log.e(LOG_TAG, ex.getMessage());
                return false;
            }
        }
        return false;
    }

    private void resetPlayer() {
        if (null != getMediaPlayer()) {
            getMediaPlayer().reset();
        } else {
            this.mediaPlayer = new MediaPlayer();
        }
        this.isPrepared = false;
    }

    /**
     * we never want to use stop when calling the player,
     * because we can just reset the mediaplayer and when a song is
     * prepared we can put it into the paused state.
     */
    @Deprecated
    public void stop() {
        if (!isPrepared()) {
            return;
        }
        // unregisterReceiver(myNoisyAudioStreamReceiver);
        currentState= PlaybackStateCompat.STATE_STOPPED;
        isPrepared = false;
        getMediaPlayer().stop();
        resetPlayer();
        // Take the service out of the foreground
    }

    public void pause() {
        if (!isPrepared() || isPaused()) {
            return;
        }
        // Update metadata and state
        getMediaPlayer().pause();
        audioFocusManager.playbackPaused();
        currentState = PlaybackStateCompat.STATE_PAUSED;
    }

    public void increaseSpeed(float by) {
        float currentSpeed = getMediaPlayer().getPlaybackParams().getSpeed();
        float newSpeed = currentSpeed + by;
        if (newSpeed <= MAXIMUM_PLAYBACK_SPEED) {
            this.currentPlaybackSpeed = newSpeed;
            updatePlaybackParameters();
        }
    }

    public void decreaseSpeed(float by) {
        float currentSpeed = getMediaPlayer().getPlaybackParams().getSpeed();
        float newSpeed = currentSpeed - by;
        if (newSpeed >= MINIMUM_PLAYBACK_SPEED) {
            this.currentPlaybackSpeed = newSpeed;
            updatePlaybackParameters();
        }
    }

    public void seekTo(long position) {
        if (!prepare()) {
            return;
        }
        getMediaPlayer().seekTo((int)position);
    }

    private void setCurrentUri(Uri uri) {
        try {
            getMediaPlayer().setDataSource(context, uri, null);
        } catch (IOException ex) {
            Log.e(LOG_TAG, ex.getStackTrace().toString());
        }
    }

    private boolean prepare() {
        if (!isPrepared()) {
            try {
                getMediaPlayer().prepare();
                currentState = PlaybackStateCompat.STATE_PAUSED;
                isPrepared = true;
            } catch (IOException ex) {
                Log.e(LOG_TAG, ex.getMessage());
            }
        }
        return isPrepared();
    }

    private void updatePlaybackParameters() {
        if (currentState == PlaybackStateCompat.STATE_PLAYING) {
            if (getMediaPlayer() != null && getMediaPlayer().getPlaybackParams() != null) {
                PlaybackParams newParams = getMediaPlayer().getPlaybackParams();
                newParams.setSpeed(getCurrentPlaybackSpeed());
                getMediaPlayer().setPlaybackParams(newParams);
            }
        }
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public PlaybackStateCompat getMediaPlayerState() {
        return new PlaybackStateCompat.Builder()
                .setState(getCurrentState(),
                        mediaPlayer.getCurrentPosition(),
                        getCurrentPlaybackSpeed())
                .build();
    }

    public MediaMetadataCompat.Builder getCurrentMetaData() {
        MediaMetadataCompat.Builder builder = new MediaMetadataCompat.Builder();
        return builder.putLong(MediaMetadataCompat.METADATA_KEY_DURATION, mediaPlayer.getDuration());
    }

    public int getCurrentTrackPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    public int getCurrentState() {
        return currentState;
    }

    public boolean isPrepared() {
        return isPrepared;
    }

    public void setVolume(float volume) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume, volume);
        }
    }

    public boolean isPlaying() {
        return currentState == PlaybackStateCompat.STATE_PLAYING;
    }

    public boolean isPaused() {
        return currentState == PlaybackStateCompat.STATE_PAUSED;
    }

    public float getCurrentPlaybackSpeed() {
        return currentPlaybackSpeed;
    }
}
