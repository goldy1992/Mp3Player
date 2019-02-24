package com.example.mike.mp3player.client.views;

import android.content.Context;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.AttributeSet;
import android.util.Log;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.PlaybackStateListener;

import static android.support.v4.media.session.PlaybackStateCompat.REPEAT_MODE_ALL;
import static android.support.v4.media.session.PlaybackStateCompat.REPEAT_MODE_NONE;
import static android.support.v4.media.session.PlaybackStateCompat.REPEAT_MODE_ONE;


public class RepeatOneRepeatAllButton extends LinearLayoutWithImageView implements PlaybackStateListener {

    private static final String LOG_TAG = "RPT1_RPT_ALL_BTN";
    private static final int OPAQUE = 255;
    private static final int TRANSLUCENT = 100;
    private Context context;
    @PlaybackStateCompat.RepeatMode
    private int repeatMode;

    public RepeatOneRepeatAllButton(Context context) {
        this(context, null);
    }

    public RepeatOneRepeatAllButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RepeatOneRepeatAllButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public void updateState(@PlaybackStateCompat.RepeatMode int newState) {
        switch (newState) {
            case REPEAT_MODE_ALL: setRepeatAllIcon();
                this.repeatMode = REPEAT_MODE_ALL;
                break;
            case REPEAT_MODE_ONE: setRepeatOneIcon();
                this.repeatMode = REPEAT_MODE_ONE;
                break;
            case REPEAT_MODE_NONE: setRepeatNoneIcon();
                this.repeatMode = REPEAT_MODE_NONE;
                break;
            default:
                StringBuilder sb = new StringBuilder().append("Invalid RepeatMode param: ").append(newState);
                Log.e(LOG_TAG, sb.toString());
        }
    }

    public void setRepeatAllIcon() {
        setViewImage(R.drawable.ic_baseline_repeat_24px);
        getView().setImageAlpha(OPAQUE);
    }
    public void setRepeatOneIcon() {
        setViewImage(R.drawable.ic_baseline_repeat_one_24px);
        getView().setImageAlpha(OPAQUE);
    }
    public void setRepeatNoneIcon() {
        setViewImage(R.drawable.ic_baseline_repeat_24px);
        this.getView().setImageAlpha(TRANSLUCENT);
    }

    public int getState() {
        return repeatMode;
    }

    public int getNextState() {
        switch (repeatMode) {
            case REPEAT_MODE_ALL:
                return REPEAT_MODE_NONE;
            case REPEAT_MODE_NONE:
                return REPEAT_MODE_ONE;
            case REPEAT_MODE_ONE:
                return REPEAT_MODE_ALL;
        }
        return -1;
    }

    public static RepeatOneRepeatAllButton create(Context context) {
        RepeatOneRepeatAllButton toReturn = new RepeatOneRepeatAllButton(context);
        toReturn.updateState(REPEAT_MODE_ALL);
        return toReturn;
    }

    @Override
    public void onPlaybackStateChanged(PlaybackStateCompat state) {
       // state.
    }
}
