package com.example.mike.mp3player.dagger.modules;

import android.os.Handler;

import com.example.mike.mp3player.client.views.TimeCounter;
import com.example.mike.mp3player.dagger.scopes.ComponentScope;
import com.example.mike.mp3player.dagger.scopes.FragmentScope;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class TimeCounterModule {

    @FragmentScope
    @Provides
    TimeCounter provideTimeCounter(@Named("main") Handler handler) {
        return new TimeCounter(handler);
    }
}
