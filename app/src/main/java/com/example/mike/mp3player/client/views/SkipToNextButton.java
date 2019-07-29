package com.example.mike.mp3player.client.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.MediaControllerAdapter;

import javax.inject.Inject;

public class SkipToNextButton extends LinearLayoutWithImageView {

    private MediaControllerAdapter mediaControllerAdapter;

    public SkipToNextButton(Context context) { super(context); }

    public SkipToNextButton(Context context, AttributeSet attrs) { this(context, attrs, 0); }

    public SkipToNextButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Inject
    public SkipToNextButton(Context context, AttributeSet attrs, int defStyleAttr, MediaControllerAdapter mediaControllerAdapter) {
        super(context, attrs, defStyleAttr, 0, 2, R.drawable.ic_baseline_skip_next_24px);
        this.mediaControllerAdapter = mediaControllerAdapter;
        getView().setOnClickListener(this::skipToNext);
    }

    public void skipToNext(View view) {
        mediaControllerAdapter.skipToNext();
    }
}
