package com.example.mike.mp3player.service.player;

import android.os.Bundle;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import com.example.mike.mp3player.R;
import com.google.android.exoplayer2.ControlDispatcher;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector;

import static com.example.mike.mp3player.commons.Constants.DECREASE_PLAYBACK_SPEED;
import static com.example.mike.mp3player.commons.Constants.INCREASE_PLAYBACK_SPEED;

public class MyCustomActionProvider implements MediaSessionConnector.CustomActionProvider {

    private static final float MINIMUM_PLAYBACK_SPEED = 0.25f;
    private static final float MAXIMUM_PLAYBACK_SPEED = 2f;
    private static final float DEFAULT_PLAYBACK_SPEED_CHANGE = 0.05f;
    private static final String LOG_TAG = "ACTN_PRVDR";

    @Override
    public void onCustomAction(Player player, ControlDispatcher controlDispatcher, String action, Bundle extras) {
        Log.i(LOG_TAG, "hit speed change");
       final float currentSpeed = player.getPlaybackParameters().speed;
       float newSpeed = currentSpeed;
        switch (action) {
            case INCREASE_PLAYBACK_SPEED: newSpeed += DEFAULT_PLAYBACK_SPEED_CHANGE;
                break;
            case DECREASE_PLAYBACK_SPEED: newSpeed -= DEFAULT_PLAYBACK_SPEED_CHANGE;
                break;
            default: break;
        }
        changeSpeed(newSpeed, player);
    }

    @Override
    public PlaybackStateCompat.CustomAction getCustomAction(Player player) {
        return new PlaybackStateCompat.CustomAction.Builder(INCREASE_PLAYBACK_SPEED, INCREASE_PLAYBACK_SPEED, R.drawable.border).build();
    }

    /**
     *
     * @param speed the speed
     * @return true if it's a valid speed
     */
    private boolean validSpeed(float speed) {
        return speed >= MINIMUM_PLAYBACK_SPEED &&
                speed <= MAXIMUM_PLAYBACK_SPEED;
    }


    private void changeSpeed(float newSpeed, Player player) {
        if (validSpeed(newSpeed)) {
            PlaybackParameters newPlaybackParameters = new PlaybackParameters(newSpeed);
            player.setPlaybackParameters(newPlaybackParameters);
        }
    }
}
