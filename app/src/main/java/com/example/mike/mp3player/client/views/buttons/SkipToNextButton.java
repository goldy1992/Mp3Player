package com.example.mike.mp3player.client.views.buttons;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.MediaControllerAdapter;

import javax.inject.Inject;
import javax.inject.Named;

public class SkipToNextButton extends MediaButton{

    @Inject
    public SkipToNextButton(Context context, MediaControllerAdapter mediaControllerAdapter, @Named("main") Handler mainUpdater) {
        super(context, mediaControllerAdapter, mainUpdater);
    }

    public void init(ImageView view) {
        this.view = view;
        Drawable drawable = context.getDrawable(R.drawable.ic_baseline_skip_next_24px);
        this.view.setImageDrawable(drawable);
        this.view.setOnClickListener(this::skipToNext);
    }


    public void skipToNext(View view) {
        mediaControllerAdapter.skipToNext();
    }


}
