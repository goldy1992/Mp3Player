package com.github.goldy1992.mp3player.service.dagger

import android.content.Context
import com.github.goldy1992.mp3player.service.dagger.components.ServiceComponent
import com.google.android.exoplayer2.ui.PlayerNotificationManager

interface ServiceComponentProvider {
    
    fun serviceComponent(context: Context,
                         notificationListener: PlayerNotificationManager.NotificationListener)
        : ServiceComponent
    
}