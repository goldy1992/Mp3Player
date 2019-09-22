package com.example.mike.mp3player.dagger.components;

import android.content.Context;

import com.example.mike.mp3player.client.MediaBrowserConnectorCallback;
import com.example.mike.mp3player.client.activities.EmptyMediaActivityCompatFragmentActivity;
import com.example.mike.mp3player.client.callbacks.subscription.SubscriptionType;
import com.example.mike.mp3player.dagger.modules.AlbumArtPainterModule;
import com.example.mike.mp3player.dagger.modules.MainHandlerModule;
import com.example.mike.mp3player.dagger.modules.MediaBrowserAdapterModule;
import com.example.mike.mp3player.dagger.modules.MediaControllerCallbackModule;
import com.example.mike.mp3player.dagger.modules.MockMediaControllerAdapterModule;
import com.example.mike.mp3player.dagger.modules.service.HandlerThreadModule;
import com.example.mike.mp3player.dagger.scopes.ComponentScope;

import dagger.BindsInstance;
import dagger.Component;

@ComponentScope
@Component(modules = {
        AlbumArtPainterModule.class,
        HandlerThreadModule.class,
        MainHandlerModule.class,
        MediaBrowserAdapterModule.class,
        MediaControllerCallbackModule.class,
        MockMediaControllerAdapterModule.class})
public interface TestMediaActivityCompatComponent extends MediaActivityCompatComponent {

    void inject(EmptyMediaActivityCompatFragmentActivity emptyMediaActivityCompatFragmentActivity);

    @Component.Factory
    interface Factory extends MediaActivityCompatComponent.Factory{
        TestMediaActivityCompatComponent create(@BindsInstance Context context,
                                            @BindsInstance String workerId,
                                            @BindsInstance SubscriptionType subscriptionType,
                                            @BindsInstance MediaBrowserConnectorCallback callback);

    }
}
