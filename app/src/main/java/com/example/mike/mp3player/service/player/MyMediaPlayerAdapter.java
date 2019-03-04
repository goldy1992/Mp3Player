package com.example.mike.mp3player.service.player;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import com.example.mike.mp3player.service.AudioFocusManager;

import org.apache.commons.lang.exception.ExceptionUtils;

import java.io.IOException;

import static android.media.MediaPlayer.MEDIA_INFO_STARTED_AS_NEXT;
import static android.support.v4.media.session.PlaybackStateCompat.REPEAT_MODE_ONE;
import static com.example.mike.mp3player.commons.Constants.REPEAT_MODE;

public class MyMediaPlayerAdapter extends GenericMediaPlayerAdapter {

    MediaPlayer currentMediaPlayer;
    MediaPlayer nextMediaPlayer;

    public MyMediaPlayerAdapter(Context context) {
        super(context);
    }

    /**
     * TODO: Provide a track that is prepared for when the service starts, to stop the Activities from
     * crashing
     */
    @Override
    public void reset(Uri firstItemUri, Uri secondItemUri, MediaPlayer.OnCompletionListener onCompletionListener) {
        Log.i(LOG_TAG, "reset");
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
                getCurrentMediaPlayer().start();
                getCurrentMediaPlayer().setLooping(isLooping());
                PlaybackParams playbackParams = currentMediaPlayer.getPlaybackParams();
                playbackParams.setSpeed(currentPlaybackSpeed);
                Log.i(LOG_TAG, "repeating = " + isLooping());

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
    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
    public void seekTo(long position) {
        if (!prepare()) {
            return;
        }
        getCurrentMediaPlayer().seekTo((int)position);
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
        return getMediaPlayerState(actions, false);
    }

    public PlaybackStateCompat getMediaPlayerState(long actions, boolean startOfSong) {
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

    public void setVolume(float volume) {
        if (currentMediaPlayer != null) {
            currentMediaPlayer.setVolume(volume, volume);
        }
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

    @Override
    MediaPlayer createMediaPlayer(Uri uri, MediaPlayer.OnCompletionListener onCompletionListener) {
        MediaPlayer mediaPlayer = MediaPlayer.create(context, uri);
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

    public MediaPlayer getNextMediaPlayer() {
        return nextMediaPlayer;
    }

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
}
