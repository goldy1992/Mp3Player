package com.github.goldy1992.mp3player

import android.app.Application
import android.app.Service
import android.content.Context
import com.github.goldy1992.mp3player.client.MediaBrowserConnectionListener
import com.github.goldy1992.mp3player.client.PermissionGranted
import com.github.goldy1992.mp3player.client.activities.*
import com.github.goldy1992.mp3player.client.dagger.ClientComponentsProvider
import com.github.goldy1992.mp3player.client.dagger.components.DaggerUnitTestAppComponent
import com.github.goldy1992.mp3player.client.dagger.components.DaggerUnitTestMediaActivityCompatComponent
import com.github.goldy1992.mp3player.client.dagger.components.UnitTestAppComponent
import com.github.goldy1992.mp3player.client.dagger.subcomponents.DaggerSplashScreenEntryActivityComponent
import com.github.goldy1992.mp3player.client.dagger.subcomponents.MediaActivityCompatComponent
import com.github.goldy1992.mp3player.client.dagger.subcomponents.SplashScreenEntryActivityComponent
import com.github.goldy1992.mp3player.commons.ComponentClassMapper

class UnitTestApplication: Application(), ClientComponentsProvider {

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
                    .splashActivity(SplashScreenEntryActivity::class.java)
                    .mainActivity(MainActivity::class.java)
                    .folderActivity(FolderActivity::class.java)
                    .mediaPlayerActivity(MediaPlayerActivity::class.java)
                    .service(Service::class.java)
                    .searchResultActivity(SearchResultActivity::class.java)
                    .build()

    override fun splashScreenComponent(splashScreenEntryActivity: SplashScreenEntryActivity, permissionGranted: PermissionGranted): SplashScreenEntryActivityComponent {
        return DaggerSplashScreenEntryActivityComponent
                .factory()
                .create(splashScreenEntryActivity, permissionGranted, componentClassMapper)

    }

    override fun mediaActivityComponent(context: Context, callback: MediaBrowserConnectionListener): MediaActivityCompatComponent {
        return DaggerUnitTestMediaActivityCompatComponent
                .factory()
                .create(context, componentClassMapper)
    }
}