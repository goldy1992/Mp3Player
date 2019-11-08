package com.github.goldy1992.mp3player.client.views.buttons;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.github.goldy1992.mp3player.R;
import com.github.goldy1992.mp3player.client.MediaControllerAdapter;

import javax.inject.Inject;
import javax.inject.Named;

public class SkipToNextButton extends MediaButton{

    @Inject
    public SkipToNextButton(Context context, MediaControllerAdapter mediaControllerAdapter, @Named("main") Handler mainUpdater) {
        super(context, mediaControllerAdapter, mainUpdater);
    }

    @Override
    public void init(ImageView view) {
        super.init(view);
        setImage(R.drawable.ic_baseline_skip_next_24px);
    }

    @Override
    public void onClick(View view) {
        mediaControllerAdapter.skipToNext();
    }


}
