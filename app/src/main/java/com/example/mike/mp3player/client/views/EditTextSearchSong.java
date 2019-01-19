package com.example.mike.mp3player.client.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;

import androidx.appcompat.widget.AppCompatEditText;

public class EditTextSearchSong extends AppCompatEditText {

    private KeyImeChangeListener keyImeChangeListener;

    public EditTextSearchSong(Context context) {
        super(context);
    }

    public EditTextSearchSong(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public EditTextSearchSong(Context context, AttributeSet attrs) {
        super(context, attrs, androidx.appcompat.R.attr.editTextStyle);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (null != keyImeChangeListener) {
            keyImeChangeListener.onKeyIme(keyCode, event);
        }
        super.onKeyPreIme(keyCode, event);
        return true;
    }

    public void setKeyImeChangeListener(KeyImeChangeListener listener) {
        this.keyImeChangeListener = listener;
    }
}
