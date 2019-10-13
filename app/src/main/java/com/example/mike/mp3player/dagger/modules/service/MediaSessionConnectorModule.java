package com.example.mike.mp3player.dagger.modules.service;

import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaSessionCompat;

import com.example.mike.mp3player.service.MyControlDispatcher;
import com.example.mike.mp3player.service.PlaybackManager;
import com.example.mike.mp3player.service.library.ContentManager;
import com.example.mike.mp3player.service.player.AudioBecomingNoisyBroadcastReceiver;
import com.example.mike.mp3player.service.player.DecreaseSpeedProvider;
import com.example.mike.mp3player.service.player.IncreaseSpeedProvider;
import com.example.mike.mp3player.service.player.MyMediaButtonEventHandler;
import com.example.mike.mp3player.service.player.MyMetadataProvider;
import com.example.mike.mp3player.service.player.MyPlaybackPreparer;
import com.example.mike.mp3player.service.player.MyTimelineQueueNavigator;
import com.google.android.exoplayer2.DefaultControlDispatcher;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector;
import com.google.android.exoplayer2.upstream.FileDataSource;

import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static com.example.mike.mp3player.commons.Constants.SUPPORTED_PLAYBACK_ACTIONS;

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

    @Provides
    @Singleton
    public MyPlaybackPreparer provideMyPlaybackPreparer(ExoPlayer exoPlayer,
                                                        ContentManager contentManager,
                                                        @Named("starting_playlist") List<MediaBrowserCompat.MediaItem> items,
                                                        MyControlDispatcher myControlDispatcher) {
        return new MyPlaybackPreparer(exoPlayer, contentManager, items, new FileDataSource(), myControlDispatcher);
    }

    @Provides
    @Singleton
    public MyMetadataProvider providesMyMetadataProvider(PlaybackManager playbackManager) {
        return new MyMetadataProvider(playbackManager);
    }

    @Provides
    @Singleton
    public MyTimelineQueueNavigator providesMyTimelineQueueNavigator(MediaSessionCompat mediaSessionCompat,
                                                                     PlaybackManager playbackManager) {
        return new MyTimelineQueueNavigator(mediaSessionCompat, playbackManager);
    }

    @Provides
    public MyControlDispatcher providesMyControlDispatcher(AudioBecomingNoisyBroadcastReceiver audioBecomingNoisyBroadcastReceiver) {
        return new MyControlDispatcher(audioBecomingNoisyBroadcastReceiver);
    }


}
