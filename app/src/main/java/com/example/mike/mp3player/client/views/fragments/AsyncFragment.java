package com.example.mike.mp3player.client.views.fragments;

import android.os.Handler;
import android.os.Looper;

import androidx.fragment.app.Fragment;

import javax.inject.Inject;
import javax.inject.Named;

public abstract class AsyncFragment extends Fragment {

    Handler mainUpdater;

    @Inject
    public void setMainUpdater(@Named("main") Handler mainUpdater) {
        this.mainUpdater = mainUpdater;
    }

    public AsyncFragment() {
        this.mainUpdater = new Handler(Looper.getMainLooper());
    }
}
