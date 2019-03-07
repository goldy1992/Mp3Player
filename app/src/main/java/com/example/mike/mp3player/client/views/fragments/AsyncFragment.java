package com.example.mike.mp3player.client.views.fragments;

import android.os.Handler;
import android.os.Looper;

import androidx.fragment.app.Fragment;

public abstract class AsyncFragment extends Fragment {

    Handler mainUpdater;

    public AsyncFragment() {
        this.mainUpdater = new Handler(Looper.getMainLooper());
    }
}
