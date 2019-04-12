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
import static com.example.mike.mp3player.commons.Constants.SHUFFLE_MODE;

public abstract class MediaPlayerAdapterBase implements MediaPlayer.OnErrorListener, MediaPlayer.OnInfoListener  {

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
    final OnCompletionListener onCompletionListener;
    final OnSeekCompleteListener onSeekCompleteListener;
    boolean isInitialised = false;

    @PlaybackStateCompat.RepeatMode
    int repeatMode;

    /**
     * initialise to paused so the player doesn't start playing immediately
     */
    @PlaybackStateCompat.State
    int currentState = PlaybackStateCompat.STATE_PAUSED;

    public MediaPlayerAdapterBase(Context context, OnCompletionListener onCompletionListener,
                                  OnSeekCompleteListener onSeekCompleteListener) {
        this.context = context;
        this.onCompletionListener = onCompletionListener;
        this.onSeekCompleteListener = onSeekCompleteListener;
    }

    public abstract void play();
    public abstract void pause();
    abstract void changeSpeed(float newSpeed);

    public final void increaseSpeed(float by) {
        changeSpeed(currentPlaybackSpeed + by);
    }
    public final void decreaseSpeed(float by) {
        changeSpeed(currentPlaybackSpeed - by);
    }
    abstract void setPlaybackParams(MediaPlayer mediaPlayer);

    /**
     * 1) complete the current completed MediaPlayer
     * 2) set the currentMediaPlayer to me the next one that is currently playing
     * 3) create the next mediaPlayer and set it.
     * @param nextUriToPrepare next URI that needs to be prepared.
     */
    public void onComplete(Uri nextUriToPrepare) {
        this.currentMediaPlayer.release();
        this.currentMediaPlayer = this.getNextMediaPlayer();

        if (null != currentMediaPlayer) {
            play();
        }
        setNextMediaPlayer(nextUriToPrepare);
    }
    /**
     *
     */
    public void setNextMediaPlayer(Uri nextUriToPrepare) {
        // TODO: we might want to make this an asynchronous task in the future
        if (nextUriToPrepare != null) {
            this.nextMediaPlayer = createMediaPlayer(nextUriToPrepare);
        }

    }
    /**
     *
     * @param firstItemUri first URI
     * @param secondItemUri second URI
     */
    public void reset(Uri firstItemUri, Uri secondItemUri) {
        //Log.i(LOG_TAG, "reset");
        if (audioFocusManager != null && audioFocusManager.hasFocus) {
            audioFocusManager.abandonAudioFocus();
            //Log.i(LOG_TAG, "focus abandoned");
        }

        if (this.currentMediaPlayer != null) {
            currentMediaPlayer.release();
           //Log.i(LOG_TAG,"current mediaplayer released");
            currentMediaPlayer = null;
        }

        if (this.getNextMediaPlayer() != null) {
            getNextMediaPlayer().release();
            //Log.i(LOG_TAG,"next mediaplayer released");
            nextMediaPlayer = null;
        }
        this.currentMediaPlayer = createMediaPlayer(firstItemUri);
        //Log.i(LOG_TAG,"Created first mediaplayer");
        this.nextMediaPlayer = secondItemUri == null ? null : createMediaPlayer(secondItemUri);
        //Log.i(LOG_TAG,"Created second mediaplayer");

        this.audioFocusManager = new AudioFocusManager(context, this);
        this.currentState = PlaybackStateCompat.STATE_PAUSED;

        if (audioFocusManager.isInitialised() && null != currentMediaPlayer) {
            this.isInitialised = true;
        }
    }

    MediaPlayer createMediaPlayer(Uri uri) {
        if (uri == null) {
            return null;
        }
        MediaPlayer mediaPlayer = MediaPlayer.create(context, uri);
        return setListeners(mediaPlayer);
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
        if (isInitialised) {
            if (!prepare()) {
                return;
            }
            getCurrentMediaPlayer().seekTo((int) position);
        }
    }


    /**
     *
     * @return
     */
    public int getCurrentPosition() {
        return currentMediaPlayer != null ? currentMediaPlayer.getCurrentPosition() : 0;
    }
    /**
     *
     * @return
     */
    public int getCurrentDuration() {
        return currentMediaPlayer != null ? currentMediaPlayer.getDuration() : 0;
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
        //Log.i(LOG_TAG, "what: " + what + ", extra " + extra);
        return true;
    }

    MediaPlayer setListeners(MediaPlayer mediaPlayer) {
        mediaPlayer.setOnInfoListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnSeekCompleteListener(onSeekCompleteListener);
        mediaPlayer.setOnCompletionListener(onCompletionListener);
        return mediaPlayer;
    }
}
