import com.github.goldy1992.mp3player.client.activities.*
import com.github.goldy1992.mp3player.commons.ComponentClassMapper
import com.github.goldy1992.mp3player.commons.MikesMp3Player

class AndroidTestMainApplication : MikesMp3Player() {
    override fun getComponentClassMapper(): ComponentClassMapper {
        return ComponentClassMapper.Builder()
                .splashActivity(SplashScreenEntryActivityAndroidTestImpl::class.java)
                .mainActivity(MainActivityInjectorAndroidTestImpl::class.java)
//                .folderActivity(FolderActivityInjector::class.java)
//                .service(MediaPlaybackServiceInjector::class.java)
//                .mediaPlayerActivity(MediaPlayerActivityInjector::class.java)
//                .searchResultActivity(SearchResultActivityInjector::class.java)
                .build()
    }

}