package com.example.mike.mp3player.client.view;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

public class PlayPauseButton extends AppCompatButton {

    private final String PLAY = "Play";
    private final String PAUSE = "Pause";

    public PlayPauseButton(Context context) {
        super(context);
    }

    public PlayPauseButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PlayPauseButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setTextPlay() {
        this.setText(PLAY);
    }

    public void setTextPause() {
        this.setText(PAUSE);
    }
}
