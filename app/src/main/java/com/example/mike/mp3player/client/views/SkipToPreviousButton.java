package com.example.mike.mp3player.client.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.MediaControllerAdapter;

import javax.inject.Inject;

public class SkipToPreviousButton extends LinearLayoutWithImageView {

    private MediaControllerAdapter mediaControllerAdapter;

    public SkipToPreviousButton(Context context) { super(context); }

    public SkipToPreviousButton(Context context, AttributeSet attrs) { this(context, attrs, 0); }

    public SkipToPreviousButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Inject
    public SkipToPreviousButton(Context context, AttributeSet attrs, int defStyleAttr, MediaControllerAdapter mediaControllerAdapter) {
        super(context, attrs, defStyleAttr, 0,2, R.drawable.ic_baseline_skip_previous_24px);
        this.mediaControllerAdapter = mediaControllerAdapter;
        setViewImage(R.drawable.ic_baseline_skip_previous_24px);
        getView().setOnClickListener(this::skipToPrevious);
    }

    public void skipToPrevious(View view) {
        mediaControllerAdapter.skipToPrevious();
    }
}
