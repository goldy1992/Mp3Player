package com.github.goldy1992.mp3player.shadows;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;

import static org.mockito.Mockito.mock;

@Implements(Glide.class)
public class ShadowGlide {

    @Implementation
    @NonNull
    public static RequestManager with(@NonNull Context context) {
        return mock(RequestManager.class);
    }

    @Implementation
    @NonNull
    public static RequestManager with(@NonNull Fragment fragment) {
        return mock(RequestManager.class);
    }
}
