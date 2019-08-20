package com.example.mike.mp3player.dagger.modules;

import com.example.mike.mp3player.service.library.ContentManager;

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

}
