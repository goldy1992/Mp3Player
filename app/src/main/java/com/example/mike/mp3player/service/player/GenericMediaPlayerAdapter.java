package com.example.mike.mp3player.service.player;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.media.session.PlaybackStateCompat;
import com.example.mike.mp3player.service.AudioFocusManager;

import static android.support.v4.media.session.PlaybackStateCompat.REPEAT_MODE_ONE;

public abstract class GenericMediaPlayerAdapter implements MediaPlayer.OnErrorListener, MediaPlayer.OnInfoListener  {

    static final float DEFAULT_SPEED = 1.0f;
    static final float DEFAULT_PITCH = 1.0f;
    static final int DEFAULT_POSITION = 0;
    static final float MINIMUM_PLAYBACK_SPEED = 0.25f;
    static final float MAXIMUM_PLAYBACK_SPEED = 2f;
    static final String LOG_TAG = "MEDIA_PLAYER_ADAPTER";
    AudioFocusManager audioFocusManager;
    final Context context;
    float currentPlaybackSpeed = DEFAULT_SPEED;
    float currentPitch = DEFAULT_PITCH;
    boolean isPrepared = true;

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

    public abstract void reset(Uri firstItemUri, Uri secondItemUri, MediaPlayer.OnCompletionListener onCompletionListener);
    public abstract void play();
    public abstract void pause();
    public abstract void onComplete(Uri nextUriToPrepare, MediaPlayer.OnCompletionListener newOnCompletionListener);
    public abstract void increaseSpeed(float by);
    public abstract void decreaseSpeed(float by);
    public abstract void seekTo(long position);
    abstract MediaPlayer createMediaPlayer(Uri uri, MediaPlayer.OnCompletionListener onCompletionListener);
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
}
