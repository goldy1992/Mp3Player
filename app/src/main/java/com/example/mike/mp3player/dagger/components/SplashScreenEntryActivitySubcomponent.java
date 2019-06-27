package com.example.mike.mp3player.dagger.components;

import com.example.mike.mp3player.client.activities.SplashScreenEntryActivity;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@Subcomponent
public interface SplashScreenEntryActivitySubcomponent extends AndroidInjector<SplashScreenEntryActivity> {


    @Subcomponent.Factory
    public interface Factory extends AndroidInjector.Factory<SplashScreenEntryActivity> {}
}
