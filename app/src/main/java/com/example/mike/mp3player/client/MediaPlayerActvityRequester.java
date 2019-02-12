package com.example.mike.mp3player.client;

/**
 * use of this interface put too much load on the parent activity.
 * The logic of creating the intents has moved to {@link com.example.mike.mp3player.client.utils.IntentUtils IntentUtils}
 * and
 */
@Deprecated
public interface MediaPlayerActvityRequester {
    void goToMediaPlayerActivity();
    void playSelectedSong(String songId);
    // TODO: play selected MediaItem e.g. an album or a folder
}
