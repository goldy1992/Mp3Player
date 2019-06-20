package com.example.mike.mp3player.dagger.modules;

import com.example.mike.mp3player.client.callbacks.subscription.SubscriptionType;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class SubscriptionTypeModule {

    private final SubscriptionType subscriptionType;

    public SubscriptionTypeModule(SubscriptionType subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    @Provides
    @Singleton
    public SubscriptionType provideSubscriptionType() {
        return subscriptionType;
    }
}
