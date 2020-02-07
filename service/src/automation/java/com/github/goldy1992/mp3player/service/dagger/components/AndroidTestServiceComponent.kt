package com.github.goldy1992.mp3player.dagger.components


import android.content.Context
import com.github.goldy1992.mp3player.commons.ComponentClassMapper
import com.github.goldy1992.mp3player.service.MediaPlaybackService
import com.github.goldy1992.mp3player.service.dagger.components.ServiceComponent
import com.github.goldy1992.mp3player.service.dagger.modules.service.*
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ContentManagerModule::class,
    ExoPlayerModule::class,
    MediaSessionCompatModule::class,
    MediaSessionConnectorModule::class,
    SearchDatabaseModule::class])
interface AndroidTestServiceComponent : ServiceComponent {

    @Component.Factory
    interface Factory : ServiceComponent.Factory {
       override fun create(@BindsInstance context: Context,
                   @BindsInstance notificationListener: PlayerNotificationManager.NotificationListener,
                   @BindsInstance workerId: String,
                   @BindsInstance componentClassMapper: ComponentClassMapper): ServiceComponent
    }
}