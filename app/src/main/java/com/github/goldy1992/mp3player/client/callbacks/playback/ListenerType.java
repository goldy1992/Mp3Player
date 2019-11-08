package com.github.goldy1992.mp3player.client.callbacks.playback;

import static android.support.v4.media.session.PlaybackStateCompat.ACTION_FAST_FORWARD;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_PAUSE;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_PLAY;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_PLAY_FROM_URI;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_PLAY_PAUSE;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_PREPARE;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_PREPARE_FROM_MEDIA_ID;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_PREPARE_FROM_SEARCH;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_PREPARE_FROM_URI;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_REWIND;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_SEEK_TO;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_SET_RATING;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_SET_REPEAT_MODE;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_SET_SHUFFLE_MODE;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_SKIP_TO_NEXT;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_SKIP_TO_QUEUE_ITEM;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_STOP;
import static com.github.goldy1992.mp3player.commons.Constants.ACTION_PLAYBACK_SPEED_CHANGED;
import static com.github.goldy1992.mp3player.commons.Constants.NO_ACTION;

/**
 * Enum to describe the listener types for the PlaybackState callbacks and the definitions of the
 * playback actions that they're listening for
 */
public enum ListenerType {
    PLAYBACK(playbackActions()),
    PLAYBACK_SPEED(getPlaybackSpeedActions()),
    REPEAT(repeatActions()),
    SHUFFLE(shuffleActions());
    /** actions */
    private final long actions;
    /**
     * constructor
     * @param actions the playback actions using PlaybackStateCompat
     */
    ListenerType(long actions) {
        this.actions = actions;
    }
    /**
     * @return the repeat actions
     */
    private static long repeatActions() {
        return ACTION_SET_REPEAT_MODE;
    }
    /**
     * @return the shuffle actions
     */
    private static long shuffleActions() {
        return ACTION_SET_SHUFFLE_MODE;
    }
    /**
     * @return the playback actions
     */
    private static long playbackActions() {
        return  ACTION_STOP |
                ACTION_PAUSE |
                ACTION_PLAY |
                ACTION_REWIND |
                ACTION_SKIP_TO_PREVIOUS |
                ACTION_SKIP_TO_NEXT |
                ACTION_FAST_FORWARD |
                ACTION_SET_RATING |
                ACTION_SEEK_TO |
                ACTION_PLAY_PAUSE |
                ACTION_PLAY_FROM_MEDIA_ID |
                ACTION_PLAY_FROM_SEARCH |
                ACTION_SKIP_TO_QUEUE_ITEM |
                ACTION_PLAY_FROM_URI |
                ACTION_PREPARE |
                ACTION_PREPARE_FROM_MEDIA_ID |
                ACTION_PREPARE_FROM_SEARCH |
                ACTION_PREPARE_FROM_URI |
                NO_ACTION;
    }
    /** @return the playback speed actions */
    private static final long getPlaybackSpeedActions() {
        return ACTION_PLAYBACK_SPEED_CHANGED;
    }

    /** @return the actions for listener type */
    public long getActions() {
        return actions;
    }
}
