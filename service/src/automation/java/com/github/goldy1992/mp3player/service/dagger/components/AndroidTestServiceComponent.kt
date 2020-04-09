package com.github.goldy1992.mp3player.service.dagger.components

import com.github.goldy1992.mp3player.commons.dagger.scopes.ComponentScope
import com.github.goldy1992.mp3player.service.dagger.modules.AndroidTestContentRetrieversModule
import com.github.goldy1992.mp3player.service.dagger.modules.AndroidTestContentSearchersModule
import com.github.goldy1992.mp3player.service.dagger.modules.service.*
import dagger.Subcomponent

@ComponentScope
@Subcomponent(modules = [AndroidTestContentRetrieversModule::class,
    AndroidTestContentSearchersModule::class,
    ContentManagerModule::class,
    ExoPlayerModule::class,
    MediaSessionCompatModule::class,
    MediaSessionConnectorModule::class,
    SearchDatabaseModule::class])
interface AndroidTestServiceComponent : ServiceComponent {

    @Subcomponent.Factory
    interface Factory : ServiceComponent.Factory
}