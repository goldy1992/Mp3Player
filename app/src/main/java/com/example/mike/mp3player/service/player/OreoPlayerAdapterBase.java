package com.example.mike.mp3player.service.player;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import org.apache.commons.lang.exception.ExceptionUtils;

public class OreoPlayerAdapterBase extends MediaPlayerAdapterBase {

    public OreoPlayerAdapterBase(Context context, MediaPlayer.OnCompletionListener onCompletionListener,
                                 MediaPlayer.OnSeekCompleteListener onSeekCompleteListener) {
        super(context, onCompletionListener, onSeekCompleteListener);
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
            } catch (Exception e) {
               Log.e(LOG_TAG, ExceptionUtils.getFullStackTrace(e));
               return false;
            }
            return true;
        }
        Log.i(LOG_TAG, "finished mplayer_adapter onPlay");
        return false;
    }

    @Override
    public void pause() {
        if (isPaused()) {
            return;
        }
        if (null != getCurrentMediaPlayer() && null != getCurrentMediaPlayer().getPlaybackParams()) {
            this.currentPlaybackSpeed = getCurrentMediaPlayer().getPlaybackParams().getSpeed();
        }
        // Update metadata and state
        getCurrentMediaPlayer().pause();
        audioFocusManager.playbackPaused();
        currentState = PlaybackStateCompat.STATE_PAUSED;
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
