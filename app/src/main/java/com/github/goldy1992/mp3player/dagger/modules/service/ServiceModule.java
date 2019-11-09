package com.github.goldy1992.mp3player.dagger.modules.service;

import com.github.goldy1992.mp3player.service.RootAuthenticator;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ServiceModule {

    @Singleton
    @Provides
    public RootAuthenticator provideRootAuthenticator(@Named("rootId") String rootId) {
        return new RootAuthenticator(rootId);
    }


}
