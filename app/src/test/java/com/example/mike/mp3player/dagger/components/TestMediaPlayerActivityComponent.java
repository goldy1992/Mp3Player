package com.example.mike.mp3player.dagger.components;

import com.example.mike.mp3player.dagger.modules.MediaBrowserAdapterModule;
import com.example.mike.mp3player.dagger.modules.MockMediaControllerAdapterModule;
import com.example.mike.mp3player.dagger.modules.service.HandlerThreadModule;


import dagger.Component;

@Component(modules = {HandlerThreadModule.class,
        MediaBrowserAdapterModule.class,
        MockMediaControllerAdapterModule.class})
public interface TestMediaPlayerActivityComponent extends MediaPlayerActivityComponent {
    @Component.Factory
    interface Factory extends MediaPlayerActivityComponent.Factory{ }
}
