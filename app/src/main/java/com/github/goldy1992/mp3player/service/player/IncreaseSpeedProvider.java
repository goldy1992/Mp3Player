package com.github.goldy1992.mp3player.service.player;

import android.support.v4.media.session.PlaybackStateCompat;

import com.github.goldy1992.mp3player.R;
import com.google.android.exoplayer2.Player;

import javax.inject.Inject;

import static com.github.goldy1992.mp3player.commons.Constants.INCREASE_PLAYBACK_SPEED;

public class IncreaseSpeedProvider extends SpeedChangeActionProviderBase {

    @Inject
    public IncreaseSpeedProvider() {
        super();
    }

    @Override
    public PlaybackStateCompat.CustomAction getCustomAction(Player player) {
        return new PlaybackStateCompat.CustomAction.Builder(INCREASE_PLAYBACK_SPEED, INCREASE_PLAYBACK_SPEED, R.drawable.border).build();

    }
}
