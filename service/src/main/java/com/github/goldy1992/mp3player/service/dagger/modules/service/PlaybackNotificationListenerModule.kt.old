package com.github.goldy1992.mp3player.service.dagger.modules.service

import com.github.goldy1992.mp3player.service.MyPlayerNotificationListener
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.scopes.ServiceScoped

@InstallIn(ServiceComponent::class)
@Module
abstract class PlaybackNotificationListenerModule {

    @ServiceScoped
    @Binds
    abstract fun providesPlaybackNotificationListener(myPlayerNotificationListener: MyPlayerNotificationListener) : PlayerNotificationManager.NotificationListener

}