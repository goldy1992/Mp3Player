package com.github.goldy1992.mp3player

import android.app.Application
import android.content.Context
import com.github.goldy1992.mp3player.client.MediaBrowserConnectorCallback
import com.github.goldy1992.mp3player.client.PermissionGranted
import com.github.goldy1992.mp3player.client.activities.*
import com.github.goldy1992.mp3player.client.dagger.ClientComponentsProvider
import com.github.goldy1992.mp3player.client.dagger.subcomponents.MediaActivityCompatComponent
import com.github.goldy1992.mp3player.client.dagger.subcomponents.SplashScreenEntryActivityComponent
import com.github.goldy1992.mp3player.commons.ComponentClassMapper
import com.github.goldy1992.mp3player.dagger.components.AppComponent
import com.github.goldy1992.mp3player.dagger.components.DaggerAppComponent
import com.github.goldy1992.mp3player.service.MediaPlaybackService
import com.github.goldy1992.mp3player.service.dagger.ServiceComponentProvider
import com.github.goldy1992.mp3player.service.dagger.components.ServiceComponent
import com.google.android.exoplayer2.ui.PlayerNotificationManager


class MainApplication : Application(),
        ClientComponentsProvider,
        ServiceComponentProvider {

    private lateinit var appComponent: AppComponent

     private val componentClassMapper: ComponentClassMapper =
            ComponentClassMapper.Builder()
                .splashActivity(SplashScreenEntryActivity::class.java)
                .mainActivity(MainActivity::class.java)
                .folderActivity(FolderActivity::class.java)
                .service(MediaPlaybackService::class.java)
                .mediaPlayerActivity(MediaPlayerActivity::class.java)
                .searchResultActivity(SearchResultActivity::class.java)
                .build()

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent
                .factory()
                .create(componentClassMapper)
        appComponent.inject(this)
    }

    override fun splashScreenComponent(splashScreenEntryActivity: SplashScreenEntryActivity,
                                       permissionGranted: PermissionGranted) : SplashScreenEntryActivityComponent {
        return appComponent
                .splashScreen()
                .create(splashScreenEntryActivity, permissionGranted)
    }

    override fun mediaActivityComponent(context: Context, callback: MediaBrowserConnectorCallback): MediaActivityCompatComponent {
        return appComponent
                .mediaActivity()
                .create(context, callback)
    }

    override fun serviceComponent(context: Context,
                                  notificationListener: PlayerNotificationManager.NotificationListener,
                                  workerId: String): ServiceComponent {
        return appComponent
                .mediaPlaybackService()
                .create(context, notificationListener, workerId)
    }


}