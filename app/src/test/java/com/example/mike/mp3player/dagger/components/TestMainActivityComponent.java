package com.example.mike.mp3player.dagger.components;



import com.example.mike.mp3player.dagger.modules.MediaBrowserAdapterModule;
import com.example.mike.mp3player.dagger.modules.MediaControllerCallbackModule;
import com.example.mike.mp3player.dagger.modules.MockMediaControllerAdapterModule;
import com.example.mike.mp3player.dagger.modules.service.HandlerThreadModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component(modules = {HandlerThreadModule.class,
        MediaBrowserAdapterModule.class,
        MediaControllerCallbackModule.class,
        MockMediaControllerAdapterModule.class})
public interface TestMainActivityComponent extends MainActivityComponent {

    @Component.Factory
    interface Factory extends MainActivityComponent.Factory {}

}
