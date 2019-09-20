package com.example.mike.mp3player.commons;

import android.os.Bundle;
import android.support.v4.media.session.PlaybackStateCompat;

import static com.example.mike.mp3player.commons.Constants.REPEAT_MODE;

public final class PlaybackStateUtil {

    @Deprecated
    public static Integer getRepeatModeFromPlaybackStateCompat(PlaybackStateCompat playbackStateCompat) {
        if (playbackStateCompat == null || playbackStateCompat.getExtras() == null) {
            return null;
        }
        Bundle extras = playbackStateCompat.getExtras();
        return extras.getInt(REPEAT_MODE);
    }
}
