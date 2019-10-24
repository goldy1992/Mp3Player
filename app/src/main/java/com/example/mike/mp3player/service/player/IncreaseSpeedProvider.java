package com.example.mike.mp3player.service.player;

import android.os.Handler;
import android.support.v4.media.session.PlaybackStateCompat;

import com.example.mike.mp3player.R;
import com.google.android.exoplayer2.Player;

import static com.example.mike.mp3player.commons.Constants.INCREASE_PLAYBACK_SPEED;

public class IncreaseSpeedProvider extends SpeedChangeActionProviderBase {

    public IncreaseSpeedProvider(Handler handler) {
        super(handler);
    }

    @Override
    public PlaybackStateCompat.CustomAction getCustomAction(Player player) {
        return new PlaybackStateCompat.CustomAction.Builder(INCREASE_PLAYBACK_SPEED, INCREASE_PLAYBACK_SPEED, R.drawable.border).build();

    }
}
