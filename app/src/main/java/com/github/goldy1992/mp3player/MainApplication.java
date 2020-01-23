package com.github.goldy1992.mp3player;

import com.github.goldy1992.mp3player.client.activities.FolderActivityInjector;
import com.github.goldy1992.mp3player.client.activities.MainActivityInjector;
import com.github.goldy1992.mp3player.client.activities.MediaPlayerActivity;
import com.github.goldy1992.mp3player.client.activities.MediaPlayerActivityInjector;
import com.github.goldy1992.mp3player.client.activities.SearchResultActivityInjector;
import com.github.goldy1992.mp3player.client.activities.SplashScreenEntryActivityInjector;
import com.github.goldy1992.mp3player.commons.ComponentClassMapper;
import com.github.goldy1992.mp3player.commons.MikesMp3Player;
import com.github.goldy1992.mp3player.service.MediaPlaybackServiceInjector;

public class MainApplication extends MikesMp3Player {

    @Override
    public ComponentClassMapper getComponentClassMapper() {
        return new ComponentClassMapper.Builder()
                .splashActivity(SplashScreenEntryActivityInjector.class)
                .mainActivity(MainActivityInjector.class)
                .folderActivity(FolderActivityInjector.class)
                .service(MediaPlaybackServiceInjector.class)
                .mediaPlayerActivity(MediaPlayerActivityInjector.class)
                .searchResultActivity(SearchResultActivityInjector.class)
                .build();
    }
}
