package com.github.goldy1992.mp3player.client.dagger.components

import com.github.goldy1992.mp3player.client.dagger.modules.*
import com.github.goldy1992.mp3player.client.dagger.subcomponents.MediaActivityCompatComponent
import com.github.goldy1992.mp3player.commons.dagger.scopes.ComponentScope
import dagger.Component

@ComponentScope
@Component(modules = [
    AutomationMediaControllerCallbackModule::class,
    GlideModule::class,
    MediaBrowserAdapterModule::class,
    MediaBrowserCompatModule::class,
    MediaControllerAdapterModule::class
])
interface AutomationMediaActivityCompatComponent : MediaActivityCompatComponent {

    @Component.Factory
    interface Factory : MediaActivityCompatComponent.Factory
}