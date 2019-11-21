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
    
    @Singleton
    @Provides
    public MediaSessionConnector providesMediaSessionConnector(
            MediaSessionCompat mediaSessionCompat,
            ExoPlayer exoPlayer,
            MyPlaybackPreparer myPlaybackPreparer,
            MyControlDispatcher myControlDispatcher,
            MyMetadataProvider myMetadataProvider,
            MyTimelineQueueNavigator myTimelineQueueNavigator,
            IncreaseSpeedProvider increaseSpeedProvider,
            DecreaseSpeedProvider decreaseSpeedProvider,
            MyMediaButtonEventHandler myMediaButtonEventHandler) {
        MediaSessionConnector mediaSessionConnector = new MediaSessionConnector(mediaSessionCompat);
        mediaSessionConnector.setPlayer(exoPlayer);
        mediaSessionConnector.setPlaybackPreparer(myPlaybackPreparer);
        mediaSessionConnector.setControlDispatcher(myControlDispatcher);
        mediaSessionConnector.setMediaMetadataProvider(myMetadataProvider);
        mediaSessionConnector.setQueueNavigator(myTimelineQueueNavigator);
        mediaSessionConnector.setCustomActionProviders(increaseSpeedProvider, decreaseSpeedProvider);
        mediaSessionConnector.setMediaButtonEventHandler(myMediaButtonEventHandler);
        mediaSessionConnector.setEnabledPlaybackActions(SUPPORTED_PLAYBACK_ACTIONS);
        return mediaSessionConnector;
    }

//    @Provides
//    @Singleton
//    public MyPlaybackPreparer provideMyPlaybackPreparer(ExoPlayer exoPlayer,
//                                                        ContentManager contentManager,
//                                                        @Named("starting_playlist") List<MediaBrowserCompat.MediaItem> items,
//                                                        MyControlDispatcher myControlDispatcher,
//                                                        MediaSourceFactory mediaSourceFactory,
//                                                        PlaylistManager playlistManager) {
//        return new MyPlaybackPreparer(exoPlayer, contentManager, items, mediaSourceFactory, myControlDispatcher, playlistManager);
//    }

    @Provides
    @Singleton
    public MediaSourceFactory providesMediaSourceFactory(Context context) {
        return new MediaSourceFactory(new FileDataSource(), new ContentDataSource(context));
    }

    @Provides
    @Singleton
    public ContentDataSource providesContentDataSource(Context context) {
        return new ContentDataSource(context);
    }

    @Provides
    @Singleton
    public MyMetadataProvider providesMyMetadataProvider(PlaylistManager playlistManager) {
        return new MyMetadataProvider(playlistManager);
    }

    @Provides
    @Singleton
    public MyTimelineQueueNavigator providesMyTimelineQueueNavigator(MediaSessionCompat mediaSessionCompat,
                                                                     PlaylistManager playlistManager) {
        return new MyTimelineQueueNavigator(mediaSessionCompat, playlistManager);
    }

    @Provides
    public MyControlDispatcher providesMyControlDispatcher(AudioBecomingNoisyBroadcastReceiver audioBecomingNoisyBroadcastReceiver) {
        return new MyControlDispatcher(audioBecomingNoisyBroadcastReceiver);
    }


}
