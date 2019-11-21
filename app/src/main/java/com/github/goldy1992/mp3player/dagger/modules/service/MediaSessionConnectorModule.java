package com.github.goldy1992.mp3player.dagger.modules.service;

import android.content.Context;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaSessionCompat;

import com.github.goldy1992.mp3player.service.MyControlDispatcher;
import com.github.goldy1992.mp3player.service.PlaylistManager;
import com.github.goldy1992.mp3player.service.library.ContentManager;
import com.github.goldy1992.mp3player.service.player.AudioBecomingNoisyBroadcastReceiver;
import com.github.goldy1992.mp3player.service.player.DecreaseSpeedProvider;
import com.github.goldy1992.mp3player.service.player.IncreaseSpeedProvider;
import com.github.goldy1992.mp3player.service.player.MediaSourceFactory;
import com.github.goldy1992.mp3player.service.player.MyMediaButtonEventHandler;
import com.github.goldy1992.mp3player.service.player.MyMetadataProvider;
import com.github.goldy1992.mp3player.service.player.MyPlaybackPreparer;
import com.github.goldy1992.mp3player.service.player.MyTimelineQueueNavigator;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector;
import com.google.android.exoplayer2.upstream.ContentDataSource;
import com.google.android.exoplayer2.upstream.FileDataSource;

import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static com.github.goldy1992.mp3player.commons.Constants.SUPPORTED_PLAYBACK_ACTIONS;

@Module
public class MediaSessionConnectorModule {

    @Provides
    @Singleton
    public ContentDataSource providesContentDataSource(Context context) {
        return new ContentDataSource(context);
    }

    @Provides
    @Singleton
    public FileDataSource provideFileDataSource() {
        return new FileDataSource();
    }

}
