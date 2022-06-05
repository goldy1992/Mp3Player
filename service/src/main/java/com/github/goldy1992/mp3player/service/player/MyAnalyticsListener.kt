package com.github.goldy1992.mp3player.service.player

import android.os.Bundle
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import com.github.goldy1992.mp3player.commons.Constants.AUDIO_SESSION_ID
import com.github.goldy1992.mp3player.commons.LogTagger
import com.google.android.exoplayer2.analytics.AnalyticsListener
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Inject

@ServiceScoped
class MyAnalyticsListener
    @Inject
    constructor(private val mediaSession : MediaSessionCompat) : AnalyticsListener, LogTagger {


    override fun onAudioSessionIdChanged(
        eventTime: AnalyticsListener.EventTime,
        audioSessionId: Int
    ) {
        Log.i(logTag(), "new AudioSessionId: $audioSessionId")
        super.onAudioSessionIdChanged(eventTime, audioSessionId)
        val toSend = Bundle()
        toSend.putInt(AUDIO_SESSION_ID, audioSessionId)
        mediaSession.sendSessionEvent(AUDIO_SESSION_ID, toSend)
    }

    override fun logTag(): String {
        return "MyAnalyticsListener"
    }

    init {
        Log.d(logTag(), "initialised")
    }
}