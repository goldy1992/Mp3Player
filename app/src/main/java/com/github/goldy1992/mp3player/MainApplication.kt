package com.github.goldy1992.mp3player


import android.app.Application
import com.github.goldy1992.mp3player.commons.ComponentClassMapper
import com.github.goldy1992.mp3player.dagger.components.AppComponent
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
open class MainApplication : Application() {

    protected lateinit var appComponent: AppComponent

    protected lateinit var componentClassMapper: ComponentClassMapper
    override fun onCreate() {
        super.onCreate()
//        initialiseDependencies()
  //      appComponent.inject(this)
    }

//    open fun buildComponentClassMapper() : ComponentClassMapper {
//        return ComponentClassMapper.Builder()
//                .splashActivity(SplashScreenEntryActivity::class.java)
//                .mainActivity(MainActivity::class.java)
//                .folderActivity(FolderActivity::class.java)
//                .service(MediaPlaybackService::class.java)
//                .mediaPlayerActivity(MediaPlayerActivity::class.java)
//                .searchResultActivity(SearchResultActivity::class.java)
//                .build()
//    }
//    override fun splashScreenComponent(splashScreenEntryActivity: SplashScreenEntryActivity,
//                                       permissionGranted: PermissionGranted) : SplashScreenEntryActivityComponent {
//        return DaggerSplashScreenEntryActivityComponent
//                .factory()
//                .create(splashScreenEntryActivity, permissionGranted, componentClassMapper)
//    }

//    override fun mediaActivityComponent(context: Context, callback: MediaBrowserConnectionListener): MediaActivityCompatComponent {
//        return DaggerMediaActivityCompatComponent
//                .factory()
//                .create(context, componentClassMapper)
//    }

//    override fun serviceComponent(context: Context,
//                                  notificationListener: PlayerNotificationManager.NotificationListener): ServiceComponent {
//        return DaggerServiceComponent
//                .factory()
//                .create(context, notificationListener, componentClassMapper)
//    }

//    override fun initialiseDependencies() {
//        this.componentClassMapper = buildComponentClassMapper()
//        this.appComponent = DaggerAppComponent
//                .factory()
//                .create(componentClassMapper)
//    }


}