package com.github.goldy1992.mp3player.service.dagger.components

import android.content.Context
import com.github.goldy1992.mp3player.commons.ComponentClassMapper
import com.github.goldy1992.mp3player.service.MediaPlaybackService
import com.github.goldy1992.mp3player.service.dagger.modules.service.ContentManagerModule
import com.github.goldy1992.mp3player.service.dagger.modules.service.ExoPlayerModule
import com.github.goldy1992.mp3player.service.dagger.modules.service.HandlerThreadModule
import com.github.goldy1992.mp3player.service.dagger.modules.service.MediaSessionCompatModule
import com.github.goldy1992.mp3player.service.dagger.modules.service.MediaSessionConnectorModule
import com.github.goldy1992.mp3player.service.dagger.modules.service.SearchDatabaseModule
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ContentManagerModule::class, ExoPlayerModule::class, HandlerThreadModule::class, MediaSessionCompatModule::class, MediaSessionConnectorModule::class, SearchDatabaseModule::class])
interface ServiceComponent {
    fun inject(mediaPlaybackService: MediaPlaybackService?)
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context,
                   @BindsInstance notificationListener: PlayerNotificationManager.NotificationListener,
                   @BindsInstance workerId: String,
                   @BindsInstance componentClassMapper: ComponentClassMapper): ServiceComponent
    }
}