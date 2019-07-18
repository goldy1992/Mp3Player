package com.example.mike.mp3player.service.player;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.net.Uri;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import com.example.mike.mp3player.service.AudioFocusManager;

import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.IOException;

import javax.inject.Singleton;

import static android.media.MediaPlayer.MEDIA_INFO_STARTED_AS_NEXT;
import static android.support.v4.media.session.PlaybackStateCompat.REPEAT_MODE_ONE;
import static com.example.mike.mp3player.commons.Constants.DEFAULT_PITCH;
import static com.example.mike.mp3player.commons.Constants.DEFAULT_POSITION;
import static com.example.mike.mp3player.commons.Constants.DEFAULT_SPEED;

@Singleton
public abstract class MediaPlayerAdapter implements MediaPlayer.OnErrorListener, MediaPlayer.OnInfoListener  {

    static final int NOT_IN_USE = -1;

    static final float MINIMUM_PLAYBACK_SPEED = 0.25f;
    static final float MAXIMUM_PLAYBACK_SPEED = 2f;
    static final String LOG_TAG = "MEDIA_PLAYER_ADAPTER";
    final Context context;
    int position = DEFAULT_POSITION;
    float currentPlaybackSpeed = DEFAULT_SPEED;
    float currentPitch = DEFAULT_PITCH;
    boolean isPrepared = true;
    MediaPlayer nextMediaPlayer;
    MediaPlayer currentMediaPlayer;
    AudioFocusManager audioFocusManager;
    private OnCompletionListener onCompletionListener;
    private OnSeekCompleteListener onSeekCompleteListener;

    @PlaybackStateCompat.RepeatMode
    int repeatMode;

    /**
     * initialise to paused so the player doesn't start playing immediately
     */
    @PlaybackStateCompat.State
    int currentState = PlaybackStateCompat.STATE_PAUSED;

    public MediaPlayerAdapter(Context context, AudioFocusManager audioFocusManager) {
        this.context = context;
        this.audioFocusManager = audioFocusManager;
        this.audioFocusManager.setPlayer(this);
    }

    public abstract boolean play();

    public boolean pause() {
        if (!isPrepared() || isPaused()) {
            return false;
        }

        try {
            if (null != getCurrentMediaPlayer()) {
                this.currentPlaybackSpeed = getCurrentMediaPlayer().getPlaybackParams().getSpeed();
            }

            // Update metadata and state
            currentMediaPlayer.pause();
            audioFocusManager.playbackPaused();
            currentState = PlaybackStateCompat.STATE_PAUSED;
        } catch (Exception ex) {
            Log.e(LOG_TAG, ExceptionUtils.getStackTrace(ex));
            return false;
        }
        return true;
    }
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
        if (audioFocusManager != null && audioFocusManager.hasFocus()) {
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

        this.currentState = PlaybackStateCompat.STATE_PAUSED;
    }

    MediaPlayer createMediaPlayer(Uri uri) {
        if (uri == null) {
            return null;
        }
        MediaPlayer mediaPlayer = MediaPlayer.create(context, uri);
        return setListeners(mediaPlayer);
    }

    public void seekTo(long position) {
        if (isInitialised()) {
            if (!prepare()) {
                return;
            }
            currentMediaPlayer.seekTo((int) position);
        }
    }

    /**
     * @return true if the MediaPlayerAdapter is initialised
     */
    public boolean isInitialised() {
       return audioFocusManager.isInitialised() && null != currentMediaPlayer
                && onCompletionListener != null && onSeekCompleteListener != null;
    }

    /**
     * @return the current position of the track prepared on the current media player, 0 if
     * currentMediaPlayer is null
     */
    public int getCurrentPosition() {
        return currentMediaPlayer != null ? currentMediaPlayer.getCurrentPosition() : 0;
    }
    /**
     * @return the duration of the track prepared on the current media player, 0 if currentMediaPlayer
     * is null
     */
    public int getCurrentDuration() {
        return currentMediaPlayer != null ? currentMediaPlayer.getDuration() : 0;
    }

    /**
     * @return true is the current state is set to STATE_PLAYING
     */
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
     * @param speed the speed
     * @return true if it's a valid speed
     */
    boolean validSpeed(float speed) {
        return speed >= MINIMUM_PLAYBACK_SPEED &&
                speed <= MAXIMUM_PLAYBACK_SPEED;
    }
    /**
     *
     * @param repeatMode the repeat mode
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
        mediaPlayer.setOnSeekCompleteListener(getOnSeekCompleteListener());
        mediaPlayer.setOnCompletionListener(getOnCompletionListener());
        return mediaPlayer;
    }

    public OnCompletionListener getOnCompletionListener() {
        return onCompletionListener;
    }

    public void setOnCompletionListener(OnCompletionListener onCompletionListener) {
        this.onCompletionListener = onCompletionListener;
        if (null != currentMediaPlayer) {
            currentMediaPlayer.setOnCompletionListener(onCompletionListener);
        }
    }

    public OnSeekCompleteListener getOnSeekCompleteListener() {
        return onSeekCompleteListener;
    }

    public void setOnSeekCompleteListener(OnSeekCompleteListener onSeekCompleteListener) {
        this.onSeekCompleteListener = onSeekCompleteListener;
        if (null != currentMediaPlayer) {
            currentMediaPlayer.setOnSeekCompleteListener(onSeekCompleteListener);
        }
    }
}
