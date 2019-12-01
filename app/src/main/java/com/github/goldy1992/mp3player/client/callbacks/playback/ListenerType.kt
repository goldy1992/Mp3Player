package com.github.goldy1992.mp3player.client.callbacks.playback

import android.support.v4.media.session.PlaybackStateCompat
import com.github.goldy1992.mp3player.commons.Constants

/**
 * Enum to describe the listener types for the PlaybackState callbacks and the definitions of the
 * playback actions that they're listening for
 */
enum class ListenerType
/**
 * constructor
 * @param actions the playback actions using PlaybackStateCompat
 */(
        /** actions  */
        val actions: Long) {
    PLAYBACK(playbackActions()), PLAYBACK_SPEED(playbackSpeedActions), REPEAT(repeatActions()), SHUFFLE(shuffleActions());

    /** @return the actions for listener type
     */

    companion object {
        /**
         * @return the repeat actions
         */
        private fun repeatActions(): Long {
            return PlaybackStateCompat.ACTION_SET_REPEAT_MODE
        }

        /**
         * @return the shuffle actions
         */
        private fun shuffleActions(): Long {
            return PlaybackStateCompat.ACTION_SET_SHUFFLE_MODE
        }

        /**
         * @return the playback actions
         */
        private fun playbackActions(): Long {
            return PlaybackStateCompat.ACTION_STOP or
                    PlaybackStateCompat.ACTION_PAUSE or
                    PlaybackStateCompat.ACTION_PLAY or
                    PlaybackStateCompat.ACTION_REWIND or
                    PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS or
                    PlaybackStateCompat.ACTION_SKIP_TO_NEXT or
                    PlaybackStateCompat.ACTION_FAST_FORWARD or
                    PlaybackStateCompat.ACTION_SET_RATING or
                    PlaybackStateCompat.ACTION_SEEK_TO or
                    PlaybackStateCompat.ACTION_PLAY_PAUSE or
                    PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID or
                    PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH or
                    PlaybackStateCompat.ACTION_SKIP_TO_QUEUE_ITEM or
                    PlaybackStateCompat.ACTION_PLAY_FROM_URI or
                    PlaybackStateCompat.ACTION_PREPARE or
                    PlaybackStateCompat.ACTION_PREPARE_FROM_MEDIA_ID or
                    PlaybackStateCompat.ACTION_PREPARE_FROM_SEARCH or
                    PlaybackStateCompat.ACTION_PREPARE_FROM_URI or
                    Constants.NO_ACTION.toLong()
        }

        /** @return the playback speed actions
         */
        private val playbackSpeedActions: Long
            private get() = Constants.ACTION_PLAYBACK_SPEED_CHANGED
    }

}