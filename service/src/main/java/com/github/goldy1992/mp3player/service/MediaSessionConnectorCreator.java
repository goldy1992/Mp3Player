package com.github.goldy1992.mp3player.service;

import android.support.v4.media.session.MediaSessionCompat;

import com.github.goldy1992.mp3player.service.player.DecreaseSpeedProvider;
import com.github.goldy1992.mp3player.service.player.IncreaseSpeedProvider;
import com.github.goldy1992.mp3player.service.player.MyMediaButtonEventHandler;
import com.github.goldy1992.mp3player.service.player.MyMetadataProvider;
import com.github.goldy1992.mp3player.service.player.MyPlaybackPreparer;
import com.github.goldy1992.mp3player.service.player.MyTimelineQueueNavigator;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector;

import javax.inject.Inject;
import javax.inject.Singleton;

import static android.support.v4.media.session.PlaybackStateCompat.ACTION_PAUSE;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_PLAY;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_SEEK_TO;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_SET_REPEAT_MODE;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_SET_SHUFFLE_MODE;
import static android.support.v4.media.session.PlaybackStateCompat.ACTION_STOP;


@Singleton
public final class MediaSessionConnectorCreator {

    @MediaSessionConnector.PlaybackActions
    public static final long SUPPORTED_PLAYBACK_ACTIONS = ACTION_STOP | ACTION_PAUSE | ACTION_PLAY |
            ACTION_SET_REPEAT_MODE | ACTION_SET_SHUFFLE_MODE | ACTION_SEEK_TO;



    private MediaSessionConnector mediaSessionConnector = null;
    private final MediaSessionCompat mediaSessionCompat;
    private final ExoPlayer exoPlayer;
    private final MyPlaybackPreparer myPlaybackPreparer;
    private final MyControlDispatcher myControlDispatcher;
    private final MyMetadataProvider myMetadataProvider;
    private final MyTimelineQueueNavigator myTimelineQueueNavigator;
    private final IncreaseSpeedProvider increaseSpeedProvider;
    private final DecreaseSpeedProvider decreaseSpeedProvider;
    private final MyMediaButtonEventHandler myMediaButtonEventHandler;

    @Inject
    public MediaSessionConnectorCreator( MediaSessionCompat mediaSessionCompat,
                                         ExoPlayer exoPlayer,
                                         MyPlaybackPreparer myPlaybackPreparer,
                                         MyControlDispatcher myControlDispatcher,
                                         MyMetadataProvider myMetadataProvider,
                                         MyTimelineQueueNavigator myTimelineQueueNavigator,
                                         IncreaseSpeedProvider increaseSpeedProvider,
                                         DecreaseSpeedProvider decreaseSpeedProvider,
                                         MyMediaButtonEventHandler myMediaButtonEventHandler) {
        this.mediaSessionCompat = mediaSessionCompat;
        this. exoPlayer = exoPlayer;
        this.myPlaybackPreparer = myPlaybackPreparer;
        this.myControlDispatcher = myControlDispatcher;
        this.myMetadataProvider = myMetadataProvider;
        this.myTimelineQueueNavigator = myTimelineQueueNavigator;
        this.increaseSpeedProvider = increaseSpeedProvider;
        this.decreaseSpeedProvider = decreaseSpeedProvider;
        this.myMediaButtonEventHandler = myMediaButtonEventHandler;
    }

    public MediaSessionConnector create() {
        if (null == this.mediaSessionConnector) {
            MediaSessionConnector newMediaSessionConnector = new MediaSessionConnector(mediaSessionCompat);
            newMediaSessionConnector.setPlayer(exoPlayer);
            newMediaSessionConnector.setPlaybackPreparer(myPlaybackPreparer);
            newMediaSessionConnector.setControlDispatcher(myControlDispatcher);
            newMediaSessionConnector.setMediaMetadataProvider(myMetadataProvider);
            newMediaSessionConnector.setQueueNavigator(myTimelineQueueNavigator);
            newMediaSessionConnector.setCustomActionProviders(increaseSpeedProvider, decreaseSpeedProvider);
            newMediaSessionConnector.setMediaButtonEventHandler(myMediaButtonEventHandler);
            newMediaSessionConnector.setEnabledPlaybackActions(SUPPORTED_PLAYBACK_ACTIONS);
            this.mediaSessionConnector = newMediaSessionConnector;
        }
        return this.mediaSessionConnector;
    }
}
