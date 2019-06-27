package com.example.mike.mp3player;

import android.app.Application;
import android.content.Context;

import com.example.mike.mp3player.client.callbacks.subscription.SubscriptionType;
import com.example.mike.mp3player.dagger.components.DaggerMainActivityComponent;
import com.example.mike.mp3player.dagger.components.DaggerServiceComponent;
import com.example.mike.mp3player.dagger.components.MainActivityComponent;
import com.example.mike.mp3player.dagger.components.ServiceComponent;

public class MikesMp3PlayerBase extends Application {

    private MainActivityComponent splashScreenActivityComponent;
    private MainActivityComponent mainActivityComponent;
    private ServiceComponent serviceComponent;

    protected void setupServiceComponent(Context context) {

        this.splashScreenActivityComponent = DaggerMainActivityComponent
                .factory()
                .create(context,"SPSH_SCRN_ACTVTY_WRKR", SubscriptionType.NOTIFY_ALL);

        this.mainActivityComponent = DaggerMainActivityComponent
                .factory()
                .create(context,"MAIN_ACTVTY_WRKR", SubscriptionType.MEDIA_ID);

        this.serviceComponent = DaggerServiceComponent
                .factory()
                .create(context, "MEDIA_PLYBK_SRVC_WKR");
        getServiceComponent().inject(this);

    }

    public MainActivityComponent getSplashScreenActivityComponent() {
        return splashScreenActivityComponent;
    }

    public MainActivityComponent getMainActivityComponent() {
        return mainActivityComponent;
    }

    public ServiceComponent getServiceComponent() {
        return serviceComponent;
    }
}
