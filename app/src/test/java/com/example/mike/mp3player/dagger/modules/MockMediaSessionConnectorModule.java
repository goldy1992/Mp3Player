package com.example.mike.mp3player.dagger.modules;

import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static org.mockito.Mockito.mock;

@Module
public class MockMediaSessionConnectorModule {

    @Provides
    @Singleton
    public MediaSessionConnector providesMediaSessionConnector() {
        return mock(MediaSessionConnector.class);
    }
}
