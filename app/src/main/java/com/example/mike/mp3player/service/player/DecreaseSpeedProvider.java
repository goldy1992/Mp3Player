package com.example.mike.mp3player.service.player;

import android.support.v4.media.session.PlaybackStateCompat;

import com.example.mike.mp3player.R;
import com.google.android.exoplayer2.Player;

import static com.example.mike.mp3player.commons.Constants.DECREASE_PLAYBACK_SPEED;

public class DecreaseSpeedProvider extends SpeedChangeActionProviderBase {

    @Override
    public PlaybackStateCompat.CustomAction getCustomAction(Player player) {
        return new PlaybackStateCompat.CustomAction.Builder(DECREASE_PLAYBACK_SPEED, DECREASE_PLAYBACK_SPEED, R.drawable.border).build();
    }
}
