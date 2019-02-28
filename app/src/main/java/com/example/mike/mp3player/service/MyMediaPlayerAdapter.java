package com.example.mike.mp3player.service;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import org.apache.commons.lang.exception.ExceptionUtils;

import java.io.IOException;

import static android.media.MediaPlayer.MEDIA_INFO_STARTED_AS_NEXT;
import static com.example.mike.mp3player.commons.Constants.REPEAT_MODE;

public class MyMediaPlayerAdapter implements MediaPlayer.OnErrorListener, MediaPlayer.OnInfoListener {

    private static final float DEFAULT_SPEED = 1.0f;
    private static final float DEFAULT_PITCH = 1.0f;
    private static final int DEFAULT_POSITION = 0;
    private static final float MINIMUM_PLAYBACK_SPEED = 0.25f;
    private static final float MAXIMUM_PLAYBACK_SPEED = 2f;
    private static final String LOG_TAG = "MEDIA_PLAYER_ADAPTER";

    private MediaPlayer currentMediaPlayer;
    private MediaPlayer nextMediaPlayer;
    private AudioFocusManager audioFocusManager;
    @PlaybackStateCompat.RepeatMode
    private int repeatMode;
    private Context context;
    /**
     * initialise to paused so the player doesn't start playing immediately
     */
    @PlaybackStateCompat.State
    private int currentState = PlaybackStateCompat.STATE_PAUSED;;
    private float currentPlaybackSpeed = DEFAULT_SPEED;
    private float currentPitch = DEFAULT_PITCH;
    private boolean isPrepared = true;

    public MyMediaPlayerAdapter(Context context) {
        this.context = context;
    }

    /**
     * TODO: Provide a track that is prepared for when the service starts, to stop the Activities from
     * crashing
     */
    public void reset(Uri firstItemUri, Uri secondItemUri, MediaPlayer.OnCompletionListener onCompletionListener) {
        if (audioFocusManager != null && audioFocusManager.hasFocus) {
            audioFocusManager.abandonAudioFocus();
        }

        if (this.currentMediaPlayer != null) {
            currentMediaPlayer.release();
            currentMediaPlayer = null;
        }

        if (this.getNextMediaPlayer() != null) {
            getNextMediaPlayer().release();
            nextMediaPlayer = null;
        }
        this.currentMediaPlayer = createMediaPlayer(firstItemUri, onCompletionListener);
        this.nextMediaPlayer = secondItemUri == null ? null : createMediaPlayer(secondItemUri, onCompletionListener);
        this.currentMediaPlayer.setNextMediaPlayer(isLooping() ? null : getNextMediaPlayer());
        this.audioFocusManager = new AudioFocusManager(context, this);
        this.audioFocusManager.init();
        this.currentState = PlaybackStateCompat.STATE_PAUSED;
    }

    public synchronized void play() {
        if (!prepare()) {
            return;
        }
        if (audioFocusManager.requestAudioFocus()) {
            try {
                // Set the session active  (and update metadata and state)
                getCurrentMediaPlayer().start();
                PlaybackParams playbackParams = currentMediaPlayer.getPlaybackParams();
                playbackParams.setSpeed(currentPlaybackSpeed);
                Log.i(LOG_TAG, "repeating = " + isLooping());
                currentMediaPlayer.setLooping(isLooping());

                getCurrentMediaPlayer().setPlaybackParams(playbackParams);
                currentState = PlaybackStateCompat.STATE_PLAYING;
            } catch (Exception e) {
               Log.e(LOG_TAG, ExceptionUtils.getFullStackTrace(e));
            }
        }
        Log.i(LOG_TAG, "finished mplayer_adapter onPlay");
    }

    /**
     * 1) complete the current completed MediaPlayer
     * 2) set the currentMediaPlayer to me the next one that is currently playing
     * 3) create the next mediaPlayer and set it.
     * @param nextUriToPrepare next URI that needs to be prepared.
     */
    public void onComplete(Uri nextUriToPrepare, MediaPlayer.OnCompletionListener newOnCompletionListener) {
        this.currentMediaPlayer.release();
//        this.currentMediaPlayer = null;
        this.currentMediaPlayer = this.getNextMediaPlayer();
        // TODO: we might want to make this an asynchronous task in the future
        if (nextUriToPrepare != null) {
            this.nextMediaPlayer = createMediaPlayer(nextUriToPrepare, newOnCompletionListener);
            this.currentMediaPlayer.setNextMediaPlayer(getNextMediaPlayer());
        }
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
        currentState= PlaybackStateCompat.STATE_STOPPED;
        isPrepared = false;
        getCurrentMediaPlayer().stop();
        // Take the service out of the foreground
    }

    public void pause() {
        if (isPaused()) {
            return;
        }
        this.currentPlaybackSpeed = getCurrentMediaPlayer().getPlaybackParams().getSpeed();
        // Update metadata and state
        getCurrentMediaPlayer().pause();
        audioFocusManager.playbackPaused();
        currentState = PlaybackStateCompat.STATE_PAUSED;
        //logPlaybackParams(currentMediaPlayer.getPlaybackParams());
    }

    public void increaseSpeed(float by) {
        float newSpeed = currentPlaybackSpeed + by;
        if (newSpeed <= MAXIMUM_PLAYBACK_SPEED) {
            this.currentPlaybackSpeed = newSpeed;
            Log.i(LOG_TAG, "current speed ¡ " + this.currentPlaybackSpeed);
            if (currentState == PlaybackStateCompat.STATE_PLAYING) {
                PlaybackParams newPlaybackParams = this.currentMediaPlayer.getPlaybackParams();
                newPlaybackParams.setSpeed(newSpeed);
                this.currentMediaPlayer.setPlaybackParams(newPlaybackParams);
            }
        }
    }

    public void decreaseSpeed(float by) {
        float newSpeed = currentPlaybackSpeed - by;
        if (newSpeed >= MINIMUM_PLAYBACK_SPEED) {
            this.currentPlaybackSpeed = newSpeed;
            Log.i(LOG_TAG, "current speed ¡ " + this.currentPlaybackSpeed);
            if (currentState == PlaybackStateCompat.STATE_PLAYING) {
                PlaybackParams newPlaybackParams = this.currentMediaPlayer.getPlaybackParams();
                newPlaybackParams.setSpeed(newSpeed);
                this.currentMediaPlayer.setPlaybackParams(newPlaybackParams);
            }
        }
    }

    public void seekTo(long position) {
        if (!prepare()) {
            return;
        }
        getCurrentMediaPlayer().seekTo((int)position);
    }

    private boolean setCurrentUri(Uri uri) {
        try {
       this.currentMediaPlayer = MediaPlayer.create(context, uri);
       } catch (Exception ex) {
           Log.e(LOG_TAG, ExceptionUtils.getFullStackTrace(ex.fillInStackTrace()));
           return false;
       }
        return true;
    }

    private boolean prepare() {
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

    public int getCurrentPlaybackPosition() {
        return currentMediaPlayer.getCurrentPosition();
    }

    public MediaPlayer getCurrentMediaPlayer() {
        return currentMediaPlayer;
    }

    public PlaybackStateCompat getMediaPlayerState(long actions) {
        Bundle ex = new Bundle();
        ex.putInt(REPEAT_MODE, repeatMode);
        return new PlaybackStateCompat.Builder()
                .setActions(actions)
                .setExtras(ex)
                .setState(getCurrentState(),
                        currentMediaPlayer.getCurrentPosition(),
                        getCurrentPlaybackSpeed())
                .build();
    }

    public MediaMetadataCompat.Builder getCurrentMetaData() {
        MediaMetadataCompat.Builder builder = new MediaMetadataCompat.Builder();
        return builder.putLong(MediaMetadataCompat.METADATA_KEY_DURATION, currentMediaPlayer.getDuration());
    }

    public int getCurrentState() {
        return currentState;
    }

    public boolean isPrepared() {
        return isPrepared;
    }

    public void setVolume(float volume) {
        if (currentMediaPlayer != null) {
            currentMediaPlayer.setVolume(volume, volume);
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

    private boolean validSpeed(float speed) {
        return speed >= MINIMUM_PLAYBACK_SPEED &&
                speed <= MAXIMUM_PLAYBACK_SPEED;
    }

    private void setPlaybackParams(MediaPlayer mediaPlayer) {
        if (null != mediaPlayer) {
            PlaybackParams playbackParams = mediaPlayer.getPlaybackParams();
            playbackParams.setSpeed(currentPlaybackSpeed);
            mediaPlayer.setPlaybackParams(playbackParams);
        }
    }

    private MediaPlayer createMediaPlayer(Uri uri, MediaPlayer.OnCompletionListener onCompletionListener) {
        MediaPlayer mediaPlayer = MediaPlayer.create(context, uri);
        mediaPlayer.setLooping(repeatMode == PlaybackStateCompat.REPEAT_MODE_ONE);
        mediaPlayer.setOnInfoListener(this::onInfo);
        mediaPlayer.setOnErrorListener(this::onError);
        mediaPlayer.setOnCompletionListener(onCompletionListener);
        return mediaPlayer;
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

    public boolean isLooping() {
        return repeatMode == PlaybackStateCompat.REPEAT_MODE_ONE;
    }

    public MediaPlayer getNextMediaPlayer() {
        return nextMediaPlayer;
    }

    public int getRepeatMode() {
        return repeatMode;
    }

    public void updateRepeatMode(int repeatMode) {
        this.repeatMode = repeatMode;

        if (repeatMode == PlaybackStateCompat.REPEAT_MODE_ONE) {
            currentMediaPlayer.setLooping(true);
            currentMediaPlayer.setNextMediaPlayer(null);
        } else {
            currentMediaPlayer.setLooping(false);
            currentMediaPlayer.setNextMediaPlayer(nextMediaPlayer);
        }
    }

    public void setRepeatMode(int repeatMode) {
        this.repeatMode = repeatMode;
    }
}
