package com.github.goldy1992.mp3player.commons;

import android.app.Application;

import com.github.goldy1992.mp3player.commons.dagger.components.AppComponent;
import com.github.goldy1992.mp3player.commons.dagger.components.DaggerAppComponent;

/**
 * Declared in case need in the future
 */
public class MikesMp3Player extends Application {

    AppComponent appComponent;

    ComponentClassMapper componentClassMapper;

    @Override
    public void onCreate() {
        super.onCreate();
        this.appComponent = DaggerAppComponent
                .factory()
                .create(componentClassMapper);
        this.appComponent.inject(this);
    }


}
