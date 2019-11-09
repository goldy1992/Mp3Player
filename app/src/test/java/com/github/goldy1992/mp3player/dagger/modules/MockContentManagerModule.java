package com.github.goldy1992.mp3player.dagger.modules;

import android.support.v4.media.MediaBrowserCompat;

import com.github.goldy1992.mp3player.commons.MediaItemType;
import com.github.goldy1992.mp3player.service.library.ContentManager;

import java.util.List;
import java.util.Map;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static org.mockito.Mockito.mock;

@Module
public class MockContentManagerModule {

    @Provides
    @Singleton
    public ContentManager provideContentManager() {
        return mock(ContentManager.class);
    }

    @Provides
    @Singleton
    @Named("starting_playlist")
    public List<MediaBrowserCompat.MediaItem> providesInitialPlaylist(ContentManager contentManager, Map<MediaItemType, String> ids) {
        return contentManager.getPlaylist(ids.get(MediaItemType.SONGS));
    }

}
