package com.example.mike.mp3player.client.views.buttons;

import android.content.Context;
import android.os.Handler;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.mike.mp3player.client.MediaControllerAdapter;

import javax.inject.Named;

public abstract class MediaButton {


    protected final Context context;
    protected final MediaControllerAdapter mediaControllerAdapter;
    protected final Handler mainUpdater;
    protected ImageView view;

    protected MediaButton(Context context, @NonNull MediaControllerAdapter mediaControllerAdapter,
                           @Named("main") Handler mainUpdater) {
        this.context = context;
        this.mediaControllerAdapter = mediaControllerAdapter;
        this.mainUpdater = mainUpdater;
    }
}
