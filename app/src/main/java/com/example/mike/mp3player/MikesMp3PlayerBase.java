package com.example.mike.mp3player;

import android.app.Application;
import android.app.Service;
import android.content.Context;

import com.example.mike.mp3player.dagger.components.DaggerServiceComponent;
import com.example.mike.mp3player.dagger.components.ServiceComponent;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasServiceInjector;

public class MikesMp3PlayerBase extends Application implements HasServiceInjector {

    @Inject
    DispatchingAndroidInjector<Service> dispatchingAndroidServiceInjector;

    protected void setupServiceComponent(Context context) {
        ServiceComponent serviceComponent = DaggerServiceComponent.builder().context(context).build();
        serviceComponent.inject(this);

    }

    @Override
    public AndroidInjector<Service> serviceInjector() {
        return dispatchingAndroidServiceInjector;
    }
}
