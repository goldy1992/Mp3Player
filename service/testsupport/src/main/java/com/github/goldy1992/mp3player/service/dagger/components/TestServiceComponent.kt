package com.github.goldy1992.mp3player.service.dagger.components

import com.github.goldy1992.mp3player.service.dagger.modules.MockSearchDatabaseModule
import com.github.goldy1992.mp3player.service.dagger.modules.service.ContentManagerModule
import com.github.goldy1992.mp3player.service.dagger.modules.service.ExoPlayerModule
import com.github.goldy1992.mp3player.service.dagger.modules.service.HandlerThreadModule
import com.github.goldy1992.mp3player.service.dagger.modules.service.MediaSessionCompatModule
import com.github.goldy1992.mp3player.service.dagger.modules.service.MediaSessionConnectorModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ContentManagerModule::class, ExoPlayerModule::class, HandlerThreadModule::class, MediaSessionConnectorModule::class, MediaSessionCompatModule::class, MockSearchDatabaseModule::class])
interface TestServiceComponent : ServiceComponent {
    @Component.Factory
    interface Factory : ServiceComponent.Factory
}