package com.example.mike.mp3player.service.player;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import com.example.mike.mp3player.service.AudioFocusManager;

import org.apache.commons.lang3.exception.ExceptionUtils;

public class OreoPlayerAdapter extends MediaPlayerAdapter {

    public OreoPlayerAdapter(Context context, AudioFocusManager audioFocusManager) {
        super(context, audioFocusManager);
    }

    @Override
    public synchronized boolean play() {

        boolean canPlay = currentMediaPlayer != null && prepare() && audioFocusManager.requestAudioFocus();
        if (canPlay) {
            try {
                // Set the session active  (and update metadata and state)
                getCurrentMediaPlayer().start();
                getCurrentMediaPlayer().setLooping(isLooping());
                PlaybackParams playbackParams = currentMediaPlayer.getPlaybackParams();
                playbackParams.setSpeed(currentPlaybackSpeed);
                Log.i(LOG_TAG, "repeating = " + isLooping());
                getCurrentMediaPlayer().setPlaybackParams(playbackParams);
                currentState = PlaybackStateCompat.STATE_PLAYING;
            } catch (Exception ex) {
               Log.e(LOG_TAG, ExceptionUtils.getMessage(ex));
               return false;
            }
            return true;
        }
        Log.i(LOG_TAG, "finished mplayer_adapter onPlay");
        return false;
    }

    @Override
    public boolean pause() {
        if (isPaused()) {
            return false;
        }

        try {
            if (null != getCurrentMediaPlayer() && null != getCurrentMediaPlayer().getPlaybackParams()) {
                this.currentPlaybackSpeed = getCurrentMediaPlayer().getPlaybackParams().getSpeed();
            }

            // Update metadata and state
            getCurrentMediaPlayer().pause();
            audioFocusManager.playbackPaused();
            currentState = PlaybackStateCompat.STATE_PAUSED;
        } catch (Exception ex) {
            Log.e(LOG_TAG, ExceptionUtils.getStackTrace(ex));
            return false;
        }
        return true;
    }

    @Override
    public void changeSpeed(float newSpeed) {
        if (validSpeed(newSpeed)) {
            this.currentPlaybackSpeed = newSpeed;
            Log.i(LOG_TAG, "current speed : " + this.currentPlaybackSpeed);
            if (currentState == PlaybackStateCompat.STATE_PLAYING) {
                PlaybackParams newPlaybackParams = this.currentMediaPlayer.getPlaybackParams();
                newPlaybackParams.setSpeed(newSpeed);
                this.currentMediaPlayer.setPlaybackParams(newPlaybackParams);
            }
        }
    }

    @Override
    void setPlaybackParams(MediaPlayer mediaPlayer) {
        if (null != mediaPlayer) {
            PlaybackParams playbackParams = mediaPlayer.getPlaybackParams();
            playbackParams.setSpeed(currentPlaybackSpeed);
            mediaPlayer.setPlaybackParams(playbackParams);
        }
    }
}
