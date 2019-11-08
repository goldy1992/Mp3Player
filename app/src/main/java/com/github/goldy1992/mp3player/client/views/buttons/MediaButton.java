package com.github.goldy1992.mp3player.client.views.buttons;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import com.github.goldy1992.mp3player.client.MediaControllerAdapter;

import javax.inject.Named;

import static com.github.goldy1992.mp3player.commons.Constants.OPAQUE;

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

    public void init(ImageView imageView) {
        this.view = imageView;
        this.view.setOnClickListener(this::onClick);
    }

    public abstract void onClick(View view);

    protected void setImage(@DrawableRes int drawableRes) {
        this.setImage(drawableRes, OPAQUE);
    }

    protected void setImage(@DrawableRes int drawableRes, int alpha) {
        Drawable drawable = context.getDrawable(drawableRes);
        this.view.setImageDrawable(drawable);
        view.setImageAlpha(alpha);
    }
}
