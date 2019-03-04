package com.example.mike.mp3player.service.player;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.net.Uri;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import com.example.mike.mp3player.service.AudioFocusManager;

import org.apache.commons.lang.exception.ExceptionUtils;

import java.io.IOException;

import static com.example.mike.mp3player.commons.LoggingUtils.logPlaybackParams;

public class MarshmallowMediaPlayerAdapter extends MyMediaPlayerAdapter {
    private Uri currentUri;
    private boolean dataSourceSet = false;
    private boolean useBufferedPosition = false;
    private int position = DEFAULT_POSITION;
    private int bufferedPosition = DEFAULT_POSITION;
    private static final String LOG_TAG = "MSHMLW_PLY_ADPR";

    public MarshmallowMediaPlayerAdapter(Context context) {
        super(context);
        this.currentPlaybackSpeed = 1.25f;
    }
    @Override
    public void reset(Uri firstItemUri, Uri secondItemUri, MediaPlayer.OnCompletionListener onCompletionListener) {
        Log.i(LOG_TAG, "resetting marshmallow player adapter");
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
        this.currentMediaPlayer.setNextMediaPlayer(getNextMediaPlayer());
        this.audioFocusManager = new AudioFocusManager(context, this);
        this.audioFocusManager.init();
        this.currentState = PlaybackStateCompat.STATE_PAUSED;
    }

    @Override
    public synchronized void play() {
        if (!prepare()) {
            return;
        }

        if (audioFocusManager.requestAudioFocus()) {
            try {
                // Set the session active  (and update metadata and state)
                currentMediaPlayer.start();
                currentState = PlaybackStateCompat.STATE_PLAYING;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    MediaPlayer createMediaPlayer(Uri uri, MediaPlayer.OnCompletionListener onCompletionListener) {
        MediaPlayer mediaPlayer = MediaPlayer.create(context, uri);
        setPlaybackParamsAndPosition(mediaPlayer);
        return  mediaPlayer;
    }

        private void setPlaybackParamsAndPosition(MediaPlayer currentMediaPlayer) {
        PlaybackParams playbackParams = new PlaybackParams();
        playbackParams = playbackParams.allowDefaults()
                .setPitch(currentPitch)
                .setSpeed(currentPlaybackSpeed)
                .setAudioFallbackMode(PlaybackParams.AUDIO_FALLBACK_MODE_DEFAULT);
        currentMediaPlayer.setPlaybackParams(playbackParams);
        if (useBufferedPosition) {
            currentMediaPlayer.seekTo(bufferedPosition);
            useBufferedPosition = false;
        } else {
           currentMediaPlayer.seekTo(getMediaPlayerPosition(currentMediaPlayer));
        }
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
  //          resetPlayer();
    //        setCurrentUri(currentUri);
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

    public int getMediaPlayerPosition(MediaPlayer mediaPlayer) {
        if (useBufferedPosition) {
            return bufferedPosition;
        }
        return mediaPlayer.getCurrentPosition();
    }
}
