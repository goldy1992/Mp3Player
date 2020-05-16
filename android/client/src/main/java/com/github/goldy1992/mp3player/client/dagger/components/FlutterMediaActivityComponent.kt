package com.github.goldy1992.mp3player.client.dagger.components

import android.content.Context
import com.github.goldy1992.mp3player.client.activities.FlutterMainActivity
import com.github.goldy1992.mp3player.client.dagger.modules.FlutterModule
import com.github.goldy1992.mp3player.client.dagger.modules.MediaBrowserAdapterModule
import com.github.goldy1992.mp3player.client.dagger.modules.MediaBrowserCompatModule
import com.github.goldy1992.mp3player.client.dagger.modules.MediaControllerAdapterModule
import com.github.goldy1992.mp3player.commons.ComponentClassMapper
import com.github.goldy1992.mp3player.commons.dagger.scopes.ComponentScope
import dagger.BindsInstance
import dagger.Component
import io.flutter.embedding.engine.FlutterEngine

@ComponentScope
@Component(modules = [
    FlutterModule::class,
    MediaBrowserAdapterModule::class,
    MediaBrowserCompatModule::class,
    MediaControllerAdapterModule::class
])
interface FlutterMediaActivityComponent {

    fun inject(flutterMainActivity: FlutterMainActivity)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context,
                   @BindsInstance componentClassMapper: ComponentClassMapper,
                   @BindsInstance flutterEngine: FlutterEngine) : FlutterMediaActivityComponent
    }
}