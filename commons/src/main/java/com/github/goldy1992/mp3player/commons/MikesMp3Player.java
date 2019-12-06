package com.github.goldy1992.mp3player.commons;

import android.app.Application;

import com.github.goldy1992.mp3player.commons.dagger.components.AppComponent;
import com.github.goldy1992.mp3player.commons.dagger.components.DaggerAppComponent;

import javax.inject.Inject;

/**
 * Declared in case need in the future
 */
public class MikesMp3Player extends Application {

    AppComponent appComponent;

    private ComponentClassMapper componentClassMapper;

    @Override
    public void onCreate() {
        super.onCreate();
        this.appComponent = DaggerAppComponent
                .factory()
                .create(getComponentClassMapper());
        this.appComponent.inject(this);
    }


    public ComponentClassMapper getComponentClassMapper() {
        return componentClassMapper;
    }

    @Inject
    public void setComponentClassMapper(ComponentClassMapper componentClassMapper) {
        this.componentClassMapper = componentClassMapper;
    }
}
