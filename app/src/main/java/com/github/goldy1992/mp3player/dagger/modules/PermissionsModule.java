package com.github.goldy1992.mp3player.dagger.modules;

import com.github.goldy1992.mp3player.client.PermissionGranted;
import com.github.goldy1992.mp3player.client.PermissionsProcessor;
import com.github.goldy1992.mp3player.client.activities.SplashScreenEntryActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class PermissionsModule {

    @Provides
    public PermissionsProcessor providePermission(SplashScreenEntryActivity activity,
                                                  PermissionGranted permissionGranted) {
        return new PermissionsProcessor(activity, permissionGranted);
    }
}
