package com.example.mike.mp3player.dagger.components;

import android.content.Context;

import com.example.mike.mp3player.client.MediaBrowserAdapter;
import com.example.mike.mp3player.client.MediaBrowserConnectorCallback;
import com.example.mike.mp3player.client.PermissionGranted;
import com.example.mike.mp3player.client.PermissionsProcessor;
import com.example.mike.mp3player.client.activities.SplashScreenEntryActivity;
import com.example.mike.mp3player.client.callbacks.subscription.SubscriptionType;
import com.example.mike.mp3player.dagger.modules.MediaBrowserAdapterModule;
import com.example.mike.mp3player.dagger.modules.PermissionsModule;
import com.example.mike.mp3player.dagger.modules.service.HandlerThreadModule;
import com.example.mike.mp3player.dagger.scopes.AndroidComponentScope;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@AndroidComponentScope
@Singleton
@Component(modules = {
        HandlerThreadModule.class,
        MediaBrowserAdapterModule.class,
        PermissionsModule.class})
public interface SplashScreenEntryActivityComponent {

    MediaBrowserAdapter provideMediaBrowserAdapter();
    PermissionsProcessor providePermission();

    void inject(SplashScreenEntryActivity splashScreenEntryActivity);

    @Component.Factory
    interface Factory {
        SplashScreenEntryActivityComponent create(@BindsInstance Context context,
                                                  @BindsInstance String workerId,
                                                  @BindsInstance SubscriptionType subscriptionType,
                                                  @BindsInstance MediaBrowserConnectorCallback callback,
                                                  @BindsInstance SplashScreenEntryActivity activity,
                                                  @BindsInstance PermissionGranted permissionGranted);
    }
}
