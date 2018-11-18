package com.example.mike.mp3player.commons;

import android.support.v4.media.session.PlaybackStateCompat;

import java.util.HashMap;
import java.util.Map;

public final class Constants {

    public static final String MEDIA_SESSION = "mediaSession";
    public static final String MEDIA_ID = "mediaId";

    public static final Map<Integer, String> playbackStateDebugMap = new HashMap<>();
    static {
        playbackStateDebugMap.put(PlaybackStateCompat.STATE_NONE, "STATE_NONE");
        playbackStateDebugMap.put(PlaybackStateCompat.STATE_STOPPED, "STATE_STOPPED");
        playbackStateDebugMap.put(PlaybackStateCompat.STATE_PAUSED, "STATE_PAUSED");
        playbackStateDebugMap.put(PlaybackStateCompat.STATE_PLAYING, "STATE_PLAYING");
        playbackStateDebugMap.put(PlaybackStateCompat.STATE_FAST_FORWARDING, "STATE_FAST_FORWARDING");
        playbackStateDebugMap.put(PlaybackStateCompat.STATE_REWINDING, "STATE_REWINDING");
        playbackStateDebugMap.put(PlaybackStateCompat.STATE_BUFFERING, "STATE_BUFFERING");
        playbackStateDebugMap.put(PlaybackStateCompat.STATE_ERROR, "STATE_ERROR");
        playbackStateDebugMap.put(PlaybackStateCompat.STATE_CONNECTING, "STATE_CONNECTING");
        playbackStateDebugMap.put(PlaybackStateCompat.STATE_SKIPPING_TO_PREVIOUS, "STATE_SKIPPING_TO_PREVIOUS");
        playbackStateDebugMap.put(PlaybackStateCompat.STATE_SKIPPING_TO_NEXT, "STATE_SKIPPING_TO_NEXT");
        playbackStateDebugMap.put(PlaybackStateCompat.STATE_SKIPPING_TO_QUEUE_ITEM, "STATE_SKIPPING_TO_QUEUE_ITEM");
    };
}
