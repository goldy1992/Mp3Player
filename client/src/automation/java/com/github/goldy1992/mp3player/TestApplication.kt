package com.github.goldy1992.mp3player

import android.app.Application
import android.content.Context
import com.github.goldy1992.mp3player.client.MediaBrowserConnectorCallback
import com.github.goldy1992.mp3player.client.PermissionGranted
import com.github.goldy1992.mp3player.client.activities.*
import com.github.goldy1992.mp3player.client.dagger.ClientComponentsProvider
import com.github.goldy1992.mp3player.client.dagger.components.DaggerTestAppComponent
import com.github.goldy1992.mp3player.client.dagger.components.TestAppComponent
import com.github.goldy1992.mp3player.client.dagger.subcomponents.MediaActivityCompatComponent
import com.github.goldy1992.mp3player.client.dagger.subcomponents.SplashScreenEntryActivityComponent
import com.github.goldy1992.mp3player.commons.ComponentClassMapper

class TestApplication : Application(),
        ClientComponentsProvider {

    private lateinit var appComponent: TestAppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerTestAppComponent
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
                    .searchResultActivity(SearchResultActivity::class.java)
                    .build()

    override fun splashScreenComponent(splashScreenEntryActivity: SplashScreenEntryActivity, permissionGranted: PermissionGranted): SplashScreenEntryActivityComponent {
       return appComponent.splashScreen().create(splashScreenEntryActivity, permissionGranted)
    }

    override fun mediaActivityComponent(context: Context, callback: MediaBrowserConnectorCallback): MediaActivityCompatComponent {
        return appComponent.mediaActivity().create(context, callback)
    }
}