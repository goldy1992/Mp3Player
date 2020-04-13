package com.github.goldy1992.mp3player.service.dagger.components

import com.github.goldy1992.mp3player.commons.dagger.scopes.ComponentScope
import com.github.goldy1992.mp3player.service.dagger.modules.MockSearchDatabaseModule
import com.github.goldy1992.mp3player.service.dagger.modules.service.*
import dagger.Component
import javax.inject.Singleton

@ComponentScope
@Component(modules = [
    ContentManagerModule::class,
    ContentRetrieversModule::class,
    ContentSearchersModule::class,
    ExoPlayerModule::class,
    MediaSessionConnectorModule::class,
    MediaSessionCompatModule::class,
    MockSearchDatabaseModule::class])
interface UnitTestServiceComponent : ServiceComponent {

    @Component.Factory
    interface Factory : ServiceComponent.Factory
}