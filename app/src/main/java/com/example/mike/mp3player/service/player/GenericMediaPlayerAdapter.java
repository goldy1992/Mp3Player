package com.example.mike.mp3player.service.player;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import com.example.mike.mp3player.service.AudioFocusManager;

import java.io.IOException;

import static android.media.MediaPlayer.MEDIA_INFO_STARTED_AS_NEXT;
import static android.support.v4.media.session.PlaybackStateCompat.REPEAT_MODE_ONE;
import static com.example.mike.mp3player.commons.Constants.DEFAULT_PITCH;
import static com.example.mike.mp3player.commons.Constants.DEFAULT_SPEED;
import static com.example.mike.mp3player.commons.Constants.REPEAT_MODE;

public abstract class GenericMediaPlayerAdapter implements MediaPlayer.OnErrorListener, MediaPlayer.OnInfoListener  {

    static final int NOT_IN_USE = -1;

    static final float MINIMUM_PLAYBACK_SPEED = 0.25f;
    static final float MAXIMUM_PLAYBACK_SPEED = 2f;
    static final String LOG_TAG = "MEDIA_PLAYER_ADAPTER";
    final Context context;
    float currentPlaybackSpeed = DEFAULT_SPEED;
    float currentPitch = DEFAULT_PITCH;
    boolean isPrepared = true;
    MediaPlayer nextMediaPlayer;
    MediaPlayer currentMediaPlayer;
    AudioFocusManager audioFocusManager;

    @PlaybackStateCompat.RepeatMode
    int repeatMode;

    /**
     * initialise to paused so the player doesn't start playing immediately
     */
    @PlaybackStateCompat.State
    int currentState = PlaybackStateCompat.STATE_PAUSED;

    public GenericMediaPlayerAdapter(Context context) {
        this.context = context;
    }

    public abstract void play();
    public abstract void pause();
    public abstract void increaseSpeed(float by);
    public abstract void decreaseSpeed(float by);
    abstract void setPlaybackParams(MediaPlayer mediaPlayer);

    /**
     * 1) complete the current completed MediaPlayer
     * 2) set the currentMediaPlayer to me the next one that is currently playing
     * 3) create the next mediaPlayer and set it.
     * @param nextUriToPrepare next URI that needs to be prepared.
     */
    public void onComplete(Uri nextUriToPrepare, OnCompletionListener newOnCompletionListener, OnSeekCompleteListener onSeekCompleteListener) {
        this.currentMediaPlayer.release();
        this.currentMediaPlayer = this.getNextMediaPlayer();
        // TODO: we might want to make this an asynchronous task in the future
        if (nextUriToPrepare != null) {
            this.nextMediaPlayer = createMediaPlayer(nextUriToPrepare, newOnCompletionListener, onSeekCompleteListener);
            this.currentMediaPlayer.setNextMediaPlayer(getNextMediaPlayer());
        }
    }
    /**
     *
     * @param firstItemUri
     * @param secondItemUri
     * @param onCompletionListener
     */
    public void reset(Uri firstItemUri, Uri secondItemUri, OnCompletionListener onCompletionListener, OnSeekCompleteListener onSeekCompleteListener) {
        Log.i(LOG_TAG, "reset");
        if (audioFocusManager != null && audioFocusManager.hasFocus) {
            audioFocusManager.abandonAudioFocus();
            Log.i(LOG_TAG, "focus abandoned");
        }

        if (this.currentMediaPlayer != null) {
            currentMediaPlayer.release();
            Log.i(LOG_TAG,"current mediaplayer released");
            currentMediaPlayer = null;
        }

        if (this.getNextMediaPlayer() != null) {
            getNextMediaPlayer().release();
            Log.i(LOG_TAG,"next mediaplayer released");
            nextMediaPlayer = null;
        }
        this.currentMediaPlayer = createMediaPlayer(firstItemUri, onCompletionListener, onSeekCompleteListener);
        Log.i(LOG_TAG,"Created first mediaplayer");
        this.nextMediaPlayer = secondItemUri == null ? null : createMediaPlayer(secondItemUri, onCompletionListener, onSeekCompleteListener);
        Log.i(LOG_TAG,"Created second mediaplayer");
        this.currentMediaPlayer.setNextMediaPlayer(getNextMediaPlayer());
        this.audioFocusManager = new AudioFocusManager(context, this);
        this.audioFocusManager.init();
        this.currentState = PlaybackStateCompat.STATE_PAUSED;
    }

    MediaPlayer createMediaPlayer(Uri uri, OnCompletionListener onCompletionListener, OnSeekCompleteListener onSeekCompleteListener) {
        MediaPlayer mediaPlayer = MediaPlayer.create(context, uri);
        mediaPlayer.setOnInfoListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnSeekCompleteListener(onSeekCompleteListener);
        mediaPlayer.setOnCompletionListener(onCompletionListener);
        return mediaPlayer;
    }

    /**
     * we never _want to use stop when calling the player,
     * because we can just reset the mediaplayer and when a song is
     * prepared we can put it into the paused state.
     */
    @Deprecated
    public void stop() {
        if (!isPrepared()) {
            return;
        }
        currentState= PlaybackStateCompat.STATE_STOPPED;
        isPrepared = false;
        //getCurrentMediaPlayer().stop();
        // Take the service out of the foreground
    }

    public void seekTo(long position) {
        if (!prepare()) {
            return;
        }
        getCurrentMediaPlayer().seekTo((int)position);
    }

    public PlaybackStateCompat getMediaPlayerState(long actions) {
        return getMediaPlayerState(actions, false);
    }

    public PlaybackStateCompat getMediaPlayerState(long actions, boolean startOfSong) {
        if (startOfSong) {
            Log.i(LOG_TAG, "start of song");
        }
        Bundle ex = new Bundle();
        ex.putInt(REPEAT_MODE, repeatMode);
        return new PlaybackStateCompat.Builder()
                .setActions(actions)
                .setExtras(ex)
                .setState(getCurrentState(),
                        startOfSong ? 0 : currentMediaPlayer.getCurrentPosition(),
                        getCurrentPlaybackSpeed())
                .build();
    }

    public MediaMetadataCompat.Builder getCurrentMetaData() {
        MediaMetadataCompat.Builder builder = new MediaMetadataCompat.Builder();
        return builder.putLong(MediaMetadataCompat.METADATA_KEY_DURATION, currentMediaPlayer.getDuration());
    }

    public boolean isPlaying() {
        return currentState == PlaybackStateCompat.STATE_PLAYING;
    }

    public float getCurrentPlaybackSpeed() {
        return currentPlaybackSpeed;
    }

    public boolean isPaused() {
        return currentState == PlaybackStateCompat.STATE_PAUSED;
    }

    public int getRepeatMode() {
        return repeatMode;
    }

    public boolean isLooping() { return repeatMode == REPEAT_MODE_ONE;
    }

    public void setRepeatMode(int repeatMode) {
        this.repeatMode = repeatMode;
    }

    public int getCurrentState() {
        return currentState;
    }

    public boolean isPrepared() {
        return isPrepared;
    }

    public MediaPlayer getNextMediaPlayer() {
        return nextMediaPlayer;
    }

    public void setVolume(float volume) {
        if (currentMediaPlayer != null) {
            currentMediaPlayer.setVolume(volume, volume);
        }
    }

    public int getCurrentPlaybackPosition() {
        return currentMediaPlayer.getCurrentPosition();
    }

    public MediaPlayer getCurrentMediaPlayer() {
        return currentMediaPlayer;
    }
    /**
     *
     * @param speed
     * @return
     */
    boolean validSpeed(float speed) {
        return speed >= MINIMUM_PLAYBACK_SPEED &&
                speed <= MAXIMUM_PLAYBACK_SPEED;
    }
    /**
     *
     * @param repeatMode
     */
    public void updateRepeatMode(int repeatMode) {
        this.repeatMode = repeatMode;
        if (currentMediaPlayer != null) {
            currentMediaPlayer.setLooping(isLooping());
        }

        if (isLooping()) {
            currentMediaPlayer.setNextMediaPlayer(null);
        } else {
            currentMediaPlayer.setNextMediaPlayer(nextMediaPlayer);
        }
    }

    boolean prepare() {
        if (!isPrepared()) {
            try {
                getCurrentMediaPlayer().prepare();
                currentState = PlaybackStateCompat.STATE_PAUSED;
                isPrepared = true;
            } catch (IOException ex) {
                Log.e(LOG_TAG, ex.getMessage());
            }
        }
        return isPrepared();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.e(LOG_TAG, "what: " + what + ", extra " + extra);
        return true;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        if (what == MEDIA_INFO_STARTED_AS_NEXT) {
            setPlaybackParams(mp);
        }
        Log.i(LOG_TAG, "what: " + what + ", extra " + extra);
        return true;
    }
}
