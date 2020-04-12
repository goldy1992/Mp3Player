package com.github.goldy1992.mp3player.service.dagger.components

import android.content.Context
import com.github.goldy1992.mp3player.commons.ComponentClassMapper
import com.github.goldy1992.mp3player.commons.dagger.scopes.ComponentScope
import com.github.goldy1992.mp3player.service.MediaPlaybackService
import com.github.goldy1992.mp3player.service.dagger.modules.service.*
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import dagger.BindsInstance
import dagger.Component
import dagger.Subcomponent

@ComponentScope
@Component(modules = [
    ContentManagerModule::class,
    ContentRetrieversModule::class,
    ContentSearchersModule::class,
    ExoPlayerModule::class,
    MediaSessionCompatModule::class,
    MediaSessionConnectorModule::class,
    SearchDatabaseModule::class])
interface ServiceComponent {

    fun inject(mediaPlaybackService: MediaPlaybackService)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context,
                   @BindsInstance notificationListener: PlayerNotificationManager.NotificationListener,
                   @BindsInstance componentClassMapper: ComponentClassMapper)
                : ServiceComponent
    }
}