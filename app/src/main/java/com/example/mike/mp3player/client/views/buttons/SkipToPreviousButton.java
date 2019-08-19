package com.example.mike.mp3player.client.views.buttons;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.MediaControllerAdapter;

import javax.inject.Inject;
import javax.inject.Named;

public class SkipToPreviousButton extends MediaButton {

    @Inject
    public SkipToPreviousButton(Context context, MediaControllerAdapter mediaControllerAdapter, @Named("main") Handler mainUpdater) {
        super(context, mediaControllerAdapter, mainUpdater);
    }

    @Override
    public void init(ImageView view) {
        super.init(view);
        setImage(R.drawable.ic_baseline_skip_previous_24px);
    }

    @Override
    public void onClick(View view) {
        mediaControllerAdapter.skipToPrevious();
    }

}