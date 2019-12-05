package com.github.goldy1992.mp3player.client.dagger.components;

import com.github.goldy1992.mp3player.client.PermissionGranted;
import com.github.goldy1992.mp3player.client.activities.SplashScreenEntryActivity;

import dagger.BindsInstance;
import dagger.Subcomponent;

@Subcomponent
public interface SplashScreenEntryActivityComponent {

    void inject(SplashScreenEntryActivity splashScreenEntryActivity);

    @Subcomponent.Factory
    interface Factory {
        SplashScreenEntryActivityComponent create(@BindsInstance SplashScreenEntryActivity splashScreenEntryActivity,
                                                  @BindsInstance PermissionGranted permissionGranted);
    }
}
