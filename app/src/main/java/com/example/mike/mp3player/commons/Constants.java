package com.example.mike.mp3player.commons;

import android.support.v4.media.session.PlaybackStateCompat;

import java.util.HashMap;
import java.util.Map;

public final class Constants {

    public static final String MEDIA_SESSION = "mediaSession";
    public static final String MEDIA_ID = "mediaId";

    public static final String PLAYLIST = "PLAYLiST";
    public static final String PLAY_ALL = "PLAY_ALL";
    public static final String PLAYBACK_STATE = "PLAYBACK_STATE";
    public static final long ONE_SECOND = 1000L;
    public static final String UNKNOWN = "Unknown";
    public static final String TIMESTAMP = "TIMESTAMP";
    public static final String INCREASE_PLAYBACK_SPEED = "INCREASE_PLAYBACK_SPEED";
    public static final String DECREASE_PLAYBACK_SPEED = "DECREASE_PLAYBACK_SPEED";

    public static final Map<Integer, String> playbackStateDebugMap = new HashMap<>();
    static {
        playbackStateDebugMap.put(PlaybackStateCompat.STATE_NONE, "STATE_NONE"); // 0
        playbackStateDebugMap.put(PlaybackStateCompat.STATE_STOPPED, "STATE_STOPPED"); // 1
        playbackStateDebugMap.put(PlaybackStateCompat.STATE_PAUSED, "STATE_PAUSED"); // 2
        playbackStateDebugMap.put(PlaybackStateCompat.STATE_PLAYING, "STATE_PLAYING"); // 3
        playbackStateDebugMap.put(PlaybackStateCompat.STATE_FAST_FORWARDING, "STATE_FAST_FORWARDING"); // 4
        playbackStateDebugMap.put(PlaybackStateCompat.STATE_REWINDING, "STATE_REWINDING"); // 5
        playbackStateDebugMap.put(PlaybackStateCompat.STATE_BUFFERING, "STATE_BUFFERING"); // 6
        playbackStateDebugMap.put(PlaybackStateCompat.STATE_ERROR, "STATE_ERROR"); // 7
        playbackStateDebugMap.put(PlaybackStateCompat.STATE_CONNECTING, "STATE_CONNECTING"); // 8
        playbackStateDebugMap.put(PlaybackStateCompat.STATE_SKIPPING_TO_PREVIOUS, "STATE_SKIPPING_TO_PREVIOUS"); // 9
        playbackStateDebugMap.put(PlaybackStateCompat.STATE_SKIPPING_TO_NEXT, "STATE_SKIPPING_TO_NEXT"); // 10
        playbackStateDebugMap.put(PlaybackStateCompat.STATE_SKIPPING_TO_QUEUE_ITEM, "STATE_SKIPPING_TO_QUEUE_ITEM"); // 11
    };
}
