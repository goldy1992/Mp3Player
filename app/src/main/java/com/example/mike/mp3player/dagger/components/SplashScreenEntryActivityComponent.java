package com.example.mike.mp3player.dagger.components;

import android.content.Context;

import com.example.mike.mp3player.client.MediaBrowserAdapter;
import com.example.mike.mp3player.client.activities.SplashScreenEntryActivity;
import com.example.mike.mp3player.client.callbacks.subscription.SubscriptionType;
import com.example.mike.mp3player.dagger.modules.MediaBrowserAdapterModule;
import com.example.mike.mp3player.dagger.modules.service.HandlerThreadModule;

import dagger.BindsInstance;
import dagger.Component;

@Component(modules = {
        HandlerThreadModule.class,
        MediaBrowserAdapterModule.class})
public interface SplashScreenEntryActivityComponent {

    MediaBrowserAdapter provideMediaBrowserAdapter();

    void inject(SplashScreenEntryActivity splashScreenEntryActivity);

    @Component.Factory
    interface Factory {
        SplashScreenEntryActivityComponent create(@BindsInstance Context context,
                                         @BindsInstance String workerId,
                                         @BindsInstance SubscriptionType subscriptionType);
    }
}
