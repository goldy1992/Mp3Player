package com.github.goldy1992.mp3player

import android.app.Application
import android.content.Context
import com.github.goldy1992.mp3player.commons.ComponentClassMapper
import com.github.goldy1992.mp3player.service.MediaPlaybackService
import com.github.goldy1992.mp3player.service.dagger.ServiceComponentProvider
import com.github.goldy1992.mp3player.service.dagger.components.DaggerUnitTestServiceComponent
import com.github.goldy1992.mp3player.service.dagger.components.ServiceComponent
import com.github.goldy1992.mp3player.service.dagger.components.UnitTestAppComponent
import com.github.goldy1992.mp3player.service.dagger.components.DaggerUnitTestAppComponent
import com.google.android.exoplayer2.ui.PlayerNotificationManager

class UnitTestApplication: Application(), ServiceComponentProvider {

    private lateinit var appComponent: UnitTestAppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerUnitTestAppComponent
                .factory()
                .create(componentClassMapper)
        appComponent.inject(this)
    }

    private val componentClassMapper: ComponentClassMapper =
            ComponentClassMapper.Builder()
                .service(MediaPlaybackService::class.java)
                .build()

    override fun serviceComponent(context: Context, notificationListener: PlayerNotificationManager.NotificationListener): ServiceComponent {
        return DaggerUnitTestServiceComponent
               .factory()
               .create(context, notificationListener, componentClassMapper)
    }


}