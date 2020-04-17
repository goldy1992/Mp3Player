package com.github.goldy1992.mp3player.client.dagger.components

import com.github.goldy1992.mp3player.client.dagger.integration.modules.MockMediaBrowserAdapterModule
import com.github.goldy1992.mp3player.client.dagger.integration.modules.MockMediaControllerAdapterModule
import com.github.goldy1992.mp3player.client.dagger.modules.GlideModule
import com.github.goldy1992.mp3player.client.dagger.subcomponents.MediaActivityCompatComponent
import com.github.goldy1992.mp3player.commons.dagger.scopes.ComponentScope
import dagger.Component

@ComponentScope
@Component(modules = [
    GlideModule::class,
    MockMediaBrowserAdapterModule::class,
    MockMediaControllerAdapterModule::class
])
interface IntegrationMediaActivityCompatComponent : MediaActivityCompatComponent {

    @Component.Factory
    interface Factory : MediaActivityCompatComponent.Factory
}