package com.example.mike.mp3player;

import android.app.Application;
import android.content.Context;

import com.example.mike.mp3player.dagger.components.ApplicationComponent;
import com.example.mike.mp3player.dagger.components.DaggerApplicationComponent;
import com.example.mike.mp3player.dagger.modules.ApplicationContextModule;

public class MikesMp3PlayerBase extends Application {

  private ApplicationComponent applicationComponent;

    protected void setupServiceComponent(Context context) {
        ApplicationContextModule applicationContextModule = new ApplicationContextModule(context);

        this.applicationComponent = DaggerApplicationComponent.builder()
                .applicationContextModule(applicationContextModule)
                .build();
    }


    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
