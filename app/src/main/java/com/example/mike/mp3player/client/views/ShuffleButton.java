package com.example.mike.mp3player.client.views;

import android.content.Context;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.AttributeSet;

import com.example.mike.mp3player.R;

import static android.support.v4.media.session.PlaybackStateCompat.SHUFFLE_MODE_ALL;
import static com.example.mike.mp3player.commons.Constants.OPAQUE;
import static com.example.mike.mp3player.commons.Constants.TRANSLUCENT;

public class ShuffleButton extends LinearLayoutWithImageView {
    private static final String LOG_TAG = "SHUFFLE_BTN";
    private Context context;
    @PlaybackStateCompat.ShuffleMode
    private int shuffleMode;

    public ShuffleButton(Context context) {
        this(context, null);
    }

    public ShuffleButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShuffleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public void updateState(@PlaybackStateCompat.ShuffleMode int newState) {
        switch (newState) {
            case SHUFFLE_MODE_ALL:
                setShuffleOn(); break;
            default:
                setShuffleOff(); break;
        }
    }

    private void setShuffleOn() {
        setViewImage(R.drawable.ic_baseline_shuffle_24px);
        getView().setImageAlpha(OPAQUE);
    }
    private void setShuffleOff() {
        setViewImage(R.drawable.ic_baseline_shuffle_24px);
        getView().setImageAlpha(TRANSLUCENT);
    }
}