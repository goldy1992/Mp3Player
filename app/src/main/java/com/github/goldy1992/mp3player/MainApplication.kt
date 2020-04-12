package com.github.goldy1992.mp3player

import android.app.Application
import android.content.Context
import com.github.goldy1992.mp3player.client.MediaBrowserConnectorCallback
import com.github.goldy1992.mp3player.client.PermissionGranted
import com.github.goldy1992.mp3player.client.activities.*
import com.github.goldy1992.mp3player.client.dagger.ClientComponentsProvider
import com.github.goldy1992.mp3player.client.dagger.subcomponents.DaggerMediaActivityCompatComponent
import com.github.goldy1992.mp3player.client.dagger.subcomponents.DaggerSplashScreenEntryActivityComponent
import com.github.goldy1992.mp3player.client.dagger.subcomponents.MediaActivityCompatComponent
import com.github.goldy1992.mp3player.client.dagger.subcomponents.SplashScreenEntryActivityComponent
import com.github.goldy1992.mp3player.commons.ComponentClassMapper
import com.github.goldy1992.mp3player.commons.DependencyInitialiser
import com.github.goldy1992.mp3player.dagger.components.AppComponent
import com.github.goldy1992.mp3player.dagger.components.DaggerAppComponent
import com.github.goldy1992.mp3player.service.MediaPlaybackService
import com.github.goldy1992.mp3player.service.dagger.ServiceComponentProvider
import com.github.goldy1992.mp3player.service.dagger.components.DaggerServiceComponent
import com.github.goldy1992.mp3player.service.dagger.components.ServiceComponent
import com.google.android.exoplayer2.ui.PlayerNotificationManager


open class MainApplication : Application(),
        ClientComponentsProvider,
        DependencyInitialiser,
        ServiceComponentProvider {

    protected lateinit var appComponent: AppComponent

    protected lateinit var componentClassMapper: ComponentClassMapper
    override fun onCreate() {
        super.onCreate()
        initialiseDependencies()
        appComponent.inject(this)
    }

    open fun buildComponentClassMapper() : ComponentClassMapper {
        return ComponentClassMapper.Builder()
                .splashActivity(SplashScreenEntryActivity::class.java)
                .mainActivity(MainActivity::class.java)
                .folderActivity(FolderActivity::class.java)
                .service(MediaPlaybackService::class.java)
                .mediaPlayerActivity(MediaPlayerActivity::class.java)
                .searchResultActivity(SearchResultActivity::class.java)
                .build()
    }
    override fun splashScreenComponent(splashScreenEntryActivity: SplashScreenEntryActivity,
                                       permissionGranted: PermissionGranted) : SplashScreenEntryActivityComponent {
        return DaggerSplashScreenEntryActivityComponent
                .factory()
                .create(splashScreenEntryActivity, permissionGranted, componentClassMapper)
    }

    override fun mediaActivityComponent(context: Context, callback: MediaBrowserConnectorCallback): MediaActivityCompatComponent {
        return DaggerMediaActivityCompatComponent
                .factory()
                .create(context, callback, componentClassMapper)
    }

    override fun serviceComponent(context: Context,
                                  notificationListener: PlayerNotificationManager.NotificationListener): ServiceComponent {
        return DaggerServiceComponent
                .factory()
                .create(context, notificationListener, componentClassMapper)
    }

    override fun initialiseDependencies() {
        this.componentClassMapper = buildComponentClassMapper()
        this.appComponent = DaggerAppComponent
                .factory()
                .create(componentClassMapper)
    }


}