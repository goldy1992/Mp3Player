package com.example.mike.mp3player.client.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.MediaControllerAdapter;

import javax.inject.Inject;
import javax.inject.Named;

public class SkipToPreviousButton {

    private final Context context;
    private final MediaControllerAdapter mediaControllerAdapter;
    private final Handler mainUpdater;
    private ImageView view;

    @Inject
    public SkipToPreviousButton(Context context, MediaControllerAdapter mediaControllerAdapter, @Named("main") Handler mainUpdater) {
        this.context = context;
        this.mediaControllerAdapter = mediaControllerAdapter;
        this.mainUpdater = mainUpdater;
    }

    public void init(ImageView view) {
        this.view = view;
        Drawable drawable = context.getDrawable(R.drawable.ic_baseline_skip_previous_24px);
        this.view.setImageDrawable(drawable);
//        this.view.init(R.drawable.ic_baseline_skip_previous_24px, 2);
        this.view.setOnClickListener(this::skipToPrevious);
    }

    public void skipToPrevious(View view) {
        mediaControllerAdapter.skipToPrevious();
    }

}
