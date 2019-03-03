package com.example.mike.mp3player.service;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.net.Uri;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import org.apache.commons.lang.exception.ExceptionUtils;

import java.io.IOException;

import static com.example.mike.mp3player.commons.LoggingUtils.logPlaybackParams;

public class MarshmallowMediaPlayerAdapter extends MyMediaPlayerAdapter {
    private Uri currentUri;
    private boolean dataSourceSet = false;
    private boolean useBufferedPosition = false;
    private int position = DEFAULT_POSITION;
    private int bufferedPosition = DEFAULT_POSITION;

    public MarshmallowMediaPlayerAdapter(Context context) {
        super(context);
    }

    @Override
    public synchronized void play() {
        if (!prepare()) {
            return;
        }

        if (audioFocusManager.requestAudioFocus()) {
            try {
                // Set the session active  (and update metadata and state)
                setPlaybackParamsAndPosition();
                currentMediaPlayer.start();
                currentState = PlaybackStateCompat.STATE_PLAYING;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setPlaybackParamsAndPosition() {
        PlaybackParams playbackParams = new PlaybackParams();
        playbackParams = playbackParams.allowDefaults()
                .setPitch(currentPitch)
                .setSpeed(currentPlaybackSpeed)
                .setAudioFallbackMode(PlaybackParams.AUDIO_FALLBACK_MODE_DEFAULT);
        if (useBufferedPosition) {
            currentMediaPlayer.seekTo(bufferedPosition);
            useBufferedPosition = false;
        } else {
            currentMediaPlayer.seekTo(getMediaPlayerPosition());
        }
        currentMediaPlayer.setPlaybackParams(playbackParams);
    }

    @Override
    public void pause() {
        if (!isPrepared() || isPaused()) {
            return;
        }
        this.currentPlaybackSpeed = currentMediaPlayer.getPlaybackParams().getSpeed();
        // Update metadata and state
        currentMediaPlayer.pause();
        audioFocusManager.playbackPaused();
        currentState = PlaybackStateCompat.STATE_PAUSED;
        logPlaybackParams(currentMediaPlayer.getPlaybackParams(), LOG_TAG);
    }

    public void increaseSpeed(float by) {
        float newSpeed = currentPlaybackSpeed + by;
        if (newSpeed < MAXIMUM_PLAYBACK_SPEED) {
            this.currentPlaybackSpeed = newSpeed;
            int originalState = currentState;
            setBufferedPosition(currentMediaPlayer.getCurrentPosition());
            this.isPrepared = false;
            resetPlayer();
            setCurrentUri(currentUri);
            if (originalState == PlaybackStateCompat.STATE_PLAYING) {
                play();
            } else {
                prepare();
            }
        }
    }

    private void resetPlayer() {
        if (null != currentMediaPlayer) {
            currentMediaPlayer.reset();
        } else {
            this.currentMediaPlayer = new MediaPlayer();
        }
        this.isPrepared = false;
        this.currentState = PlaybackStateCompat.STATE_NONE;
        this.dataSourceSet = false;
    }

    public void decreaseSpeed(float by) {
        float newSpeed = currentPlaybackSpeed - by;
        if (newSpeed > MINIMUM_PLAYBACK_SPEED) {
            this.currentPlaybackSpeed = newSpeed;
            int originalState = currentState;
            setBufferedPosition(currentMediaPlayer.getCurrentPosition());
            this.isPrepared = false;
            resetPlayer();
            setCurrentUri(currentUri);
            if (originalState == PlaybackStateCompat.STATE_PLAYING) {
                play();
            } else {
                prepare();
            }
        }
    }

    private boolean setCurrentUri(Uri uri) {
        try {
            this.currentMediaPlayer.setDataSource(context, uri);
        } catch (IOException ex) {
            Log.e(LOG_TAG, ExceptionUtils.getFullStackTrace(ex.fillInStackTrace()));
            return false;
        }
        this.dataSourceSet = true;
        this.position = DEFAULT_POSITION;
        this.currentUri = uri;
        return true;
    }

    private boolean prepare() {
        if (!isPrepared()) {
            try {
                this.currentMediaPlayer.prepare();
                currentState = PlaybackStateCompat.STATE_PAUSED;
                isPrepared = true;
            } catch (IOException ex) {
                Log.e(LOG_TAG, ex.getMessage());
            }
        }
        return isPrepared();
    }

    public void setBufferedPosition(int position) {
        this.bufferedPosition = position;
        this.useBufferedPosition = true;
    }

    public int getMediaPlayerPosition() {
        if (useBufferedPosition) {
            return bufferedPosition;
        }
        return currentMediaPlayer.getCurrentPosition();
    }
}
