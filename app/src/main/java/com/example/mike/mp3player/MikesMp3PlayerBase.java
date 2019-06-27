package com.example.mike.mp3player;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;

import com.example.mike.mp3player.client.callbacks.subscription.SubscriptionType;
import com.example.mike.mp3player.dagger.components.DaggerMainActivityComponent;
import com.example.mike.mp3player.dagger.components.DaggerServiceComponent;
import com.example.mike.mp3player.dagger.components.MainActivityComponent;
import com.example.mike.mp3player.dagger.components.ServiceComponent;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.HasServiceInjector;

public class MikesMp3PlayerBase extends Application implements HasServiceInjector, HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Service> dispatchingAndroidServiceInjector;

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidActivityInjector;

    protected void setupServiceComponent(Context context) {



        MainActivityComponent mainActivityComponent = DaggerMainActivityComponent
                .factory()
                .create(context,"MAIN_ACTVTY_WRKR", SubscriptionType.MEDIA_ID);
        mainActivityComponent.inject(this);

        ServiceComponent serviceComponent = DaggerServiceComponent
                .factory()
                .create(context, "MEDIA_PLYBK_SRVC_WKR");
        serviceComponent.inject(this);

    }

    @Override
    public AndroidInjector<Service> serviceInjector() {
        return dispatchingAndroidServiceInjector;
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidActivityInjector;
    }
}
