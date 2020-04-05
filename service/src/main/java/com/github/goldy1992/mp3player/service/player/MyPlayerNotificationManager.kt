package com.github.goldy1992.mp3player.service.player

import android.content.Context
import android.graphics.Color
import androidx.annotation.VisibleForTesting
import androidx.core.app.NotificationCompat
import com.github.goldy1992.mp3player.commons.LogTagger
import com.github.goldy1992.mp3player.commons.dagger.scopes.ComponentScope
import com.github.goldy1992.mp3player.service.MyDescriptionAdapter
import com.github.goldy1992.mp3player.service.R
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import javax.inject.Inject
import javax.inject.Singleton

@ComponentScope
class MyPlayerNotificationManager @Inject constructor(private val context: Context, private val myDescriptionAdapter: MyDescriptionAdapter,
                                                      private val exoPlayer: ExoPlayer,
                                                      private val notificationListener: PlayerNotificationManager.NotificationListener) : LogTagger {
    @get:VisibleForTesting
    var playbackNotificationManager: PlayerNotificationManager? = null
        private set
    var isActive = false
        private set

    fun create(): PlayerNotificationManager? {
        if (null == playbackNotificationManager) {
            playbackNotificationManager = PlayerNotificationManager.createWithNotificationChannel(
                    context,
                    CHANNEL_ID,
                    R.string.notification_channel_name,
                    R.string.channel_description,
                    NOTIFICATION_ID,
                    myDescriptionAdapter,
                    notificationListener)
            playbackNotificationManager?.setPlayer(null)
            playbackNotificationManager?.setColor(Color.BLACK)
            playbackNotificationManager?.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            playbackNotificationManager?.setPriority(NotificationCompat.PRIORITY_LOW)
            playbackNotificationManager?.setColorized(true)
            playbackNotificationManager?.setUseChronometer(true)
            playbackNotificationManager?.setSmallIcon(R.drawable.exo_notification_small_icon)
            playbackNotificationManager?.setBadgeIconType(NotificationCompat.BADGE_ICON_NONE)
            playbackNotificationManager?.setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
        }
        return playbackNotificationManager
    }

    fun activate() {
        playbackNotificationManager!!.setPlayer(exoPlayer)
        isActive = true
    }

    fun deactivate() {
        playbackNotificationManager!!.setPlayer(null)
        isActive = false
    }

    override fun logTag(): String {
        return "MEDIA_PLAYBACK_SERVICE"
    }

    @VisibleForTesting
    fun setPlayerNotificationManager(playerNotificationManager: PlayerNotificationManager?) {
        playbackNotificationManager = playerNotificationManager
    }

    companion object {
        private const val NOTIFICATION_ID = 512
        private const val CHANNEL_ID = "com.github.goldy1992.mp3player.context"
    }

    init {
        create()
    }
}