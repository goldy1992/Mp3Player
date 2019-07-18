package com.example.mike.mp3player.dagger.modules;

import android.content.Context;

import com.example.mike.mp3player.service.library.mediaretriever.MediaRetriever;
import com.example.mike.mp3player.service.library.mediaretriever.MockMediaRetriever;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MockMediaRetrieverModule {
    @Provides
    @Singleton
    public MediaRetriever provideMediaRetriever(Context context) {
        return new MockMediaRetriever(context);
    }
}
