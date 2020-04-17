package com.github.goldy1992.mp3player.client.dagger.endtoend.components

import com.github.goldy1992.mp3player.client.dagger.endtoend.modules.AndroidTestMediaControllerModule
import com.github.goldy1992.mp3player.client.dagger.modules.*
import com.github.goldy1992.mp3player.client.dagger.subcomponents.MediaActivityCompatComponent
import com.github.goldy1992.mp3player.commons.dagger.scopes.ComponentScope
import dagger.Component

@ComponentScope
@Component(modules = [
    AndroidTestMediaControllerModule::class,
    GlideModule::class,
    MediaBrowserAdapterModule::class,
    MediaBrowserCompatModule::class,
    MediaControllerAdapterModule::class
])
interface AutomationMediaActivityCompatComponent : MediaActivityCompatComponent {

    @Component.Factory
    interface Factory : MediaActivityCompatComponent.Factory
}