package com.github.goldy1992.mp3player.client.dagger.components;

import com.github.goldy1992.mp3player.client.PermissionGranted;
import com.github.goldy1992.mp3player.client.activities.SplashScreenEntryActivity;
import com.github.goldy1992.mp3player.commons.ComponentClassMapper;

import dagger.BindsInstance;
import dagger.Component;

@Component
public interface SplashScreenEntryActivityComponent {

    void inject(SplashScreenEntryActivity splashScreenEntryActivity);

    @Component.Factory
    interface Factory {
        SplashScreenEntryActivityComponent create(@BindsInstance SplashScreenEntryActivity splashScreenEntryActivity,
                                                  @BindsInstance PermissionGranted permissionGranted,
                                                  @BindsInstance ComponentClassMapper componentClassMapper);
    }
}
