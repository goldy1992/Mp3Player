package com.example.mike.mp3player.daggertest;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class BackEndServiceModule {

    @Provides
    @Singleton
    BackendService provideBackendService(@Named("serverUrl") String serverUrl, @Named("anotherUrl") String anotherUrl) {
        return new BackendService(serverUrl, anotherUrl);
    }

    @Provides
    @Named("serverUrl")
    String provideServerUrl() {
        return "https://www.vogella.com/";
    }

    @Provides
    @Named("anotherUrl")
    String provideAnotherUrl() {
        return "http://www.google.com";
    }

}

