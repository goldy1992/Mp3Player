package com.example.mike.mp3player.dagger.components;

import android.content.Context;

import com.example.mike.mp3player.MikesMp3PlayerBase;
import com.example.mike.mp3player.client.MediaBrowserAdapter;
import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.activities.MainActivity;
import com.example.mike.mp3player.client.activities.SplashScreenEntryActivity;
import com.example.mike.mp3player.client.callbacks.MyMediaControllerCallback;
import com.example.mike.mp3player.client.callbacks.MyMetaDataCallback;
import com.example.mike.mp3player.client.callbacks.playback.MyPlaybackStateCallback;
import com.example.mike.mp3player.client.callbacks.subscription.SubscriptionType;
import com.example.mike.mp3player.dagger.modules.MediaBrowserAdapterModule;
import com.example.mike.mp3player.dagger.modules.MediaControllerAdapterModule;
import com.example.mike.mp3player.dagger.modules.service.HandlerThreadModule;
import com.example.mike.mp3player.dagger.modules.service.SplashScreenEntryActivityModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;

@Singleton
@Component(modules = {
        AndroidInjectionModule.class,
        HandlerThreadModule.class,
        MediaBrowserAdapterModule.class,
        MediaControllerAdapterModule.class,
SplashScreenEntryActivityModule.class})
public interface MainActivityComponent extends AndroidInjector<MikesMp3PlayerBase> {

    MediaBrowserAdapter provideMediaBrowserAdapter();
    MediaControllerAdapter provideMediaControllerAdapter();
    MyMediaControllerCallback provideMyMediaControllerCallback();
    MyMetaDataCallback provideMyMetaDataCallback();
    MyPlaybackStateCallback provideMyPlaybackStateCallback();
    String workerId();

    void inject(MikesMp3PlayerBase mp3PlayerBase);
    void inject(SplashScreenEntryActivity splashScreenEntryActivity);
    void inject(MainActivity mainActivity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder context(Context context);
        @BindsInstance
        Builder worker(String workerId);
        @BindsInstance
        Builder subscription(SubscriptionType subscriptionType);
        MainActivityComponent build();
    }
}
