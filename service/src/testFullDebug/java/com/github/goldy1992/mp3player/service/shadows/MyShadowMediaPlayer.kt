package com.github.goldy1992.mp3player.service.shadows

import android.content.Context
import android.media.MediaPlayer
import android.media.PlaybackParams
import android.net.Uri
import org.robolectric.annotation.Implementation
import org.robolectric.annotation.Implements
import org.robolectric.shadow.api.Shadow
import org.robolectric.shadows.ShadowMediaPlayer
import org.robolectric.shadows.util.DataSource

@Implements(MediaPlayer::class)
class MyShadowMediaPlayer : ShadowMediaPlayer() {
    @get:Implementation
    @set:Implementation
    var playbackParams: PlaybackParams? = null

    @Implementation
    public override fun __constructor__() {
        super.__constructor__()
    }

    companion object {
        @Implementation
        @Suppress("UNUSED_PARAMETER")
        fun create(context: Context?, uri: Uri): MediaPlayer? {
            val ds = DataSource.toDataSource(uri.path!!.toString())
            addMediaInfo(ds, MediaInfo())
            val mp = MediaPlayer()
            val shadow = Shadow.extract<MyShadowMediaPlayer>(mp)
            try {
                shadow.dataSource = ds
                shadow.state = State.INITIALIZED
                val playbackParams = PlaybackParams()
                playbackParams.pitch = 0f
                playbackParams.speed = 0f
                playbackParams.audioFallbackMode = PlaybackParams.AUDIO_FALLBACK_MODE_DEFAULT
                shadow.playbackParams = playbackParams
                mp.prepare()
            } catch (e: Exception) {
                return null
            }
            return mp
        }
    }
}