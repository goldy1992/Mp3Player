package com.example.mike.mp3player;

import android.app.Application;
import android.content.Context;

import com.example.mike.mp3player.dagger.components.DaggerServiceComponent;
import com.example.mike.mp3player.dagger.components.MainActivityComponent;
import com.example.mike.mp3player.dagger.components.ServiceComponent;
import com.example.mike.mp3player.dagger.components.SplashScreenEntryActivityComponent;

public class MikesMp3PlayerBase extends Application {

    private ServiceComponent serviceComponent;

    protected void setupServiceComponent(Context context) {

        this.serviceComponent = DaggerServiceComponent
                .factory()
                .create(context, "MEDIA_PLYBK_SRVC_WKR");
        getServiceComponent().inject(this);

    }

    public ServiceComponent getServiceComponent() {
        return serviceComponent;
    }
}
