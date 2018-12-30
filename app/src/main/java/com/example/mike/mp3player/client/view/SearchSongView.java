package com.example.mike.mp3player.client.view;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mike.mp3player.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SearchSongView extends Fragment implements TextWatcher, KeyImeChangeListener {

    private EditTextSearchSong searchText;
    private LinearLayout scrim;
    private boolean isActive;
    private NewSearchFilterListener newSearchFilterListener;
    private OnSearchExitListener onSearchExitListener;
    private static final String LOG_TAG = "SEARCH_SONG_VIEW";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.search_textbox_layout, null);
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        this.searchText = view.findViewById(R.id.searchText);
        this.scrim = view.findViewById(R.id.mainActivityScrim);
        searchText.addTextChangedListener(this);
        searchText.setOnEditorActionListener((TextView v, int actionId, KeyEvent event) -> onEditorAction(v , actionId, event));
        searchText.setKeyImeChangeListener(this);

    }

    public SearchSongView() {

    }

    public void onSearchStart(InputMethodManager inputMethodManager) {
        searchText.setFocusableInTouchMode(true);
        searchText.requestFocus();
        inputMethodManager.showSoftInput(searchText, InputMethodManager.SHOW_IMPLICIT);
        setActive(true);
    }

    public void onSearchFinish(InputMethodManager inputMethodManager) {
        inputMethodManager.hideSoftInputFromWindow(searchText.getWindowToken(), InputMethodManager.RESULT_HIDDEN);
        setActive(false);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        Log.i(LOG_TAG, "beforetextChanged");
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Log.i(LOG_TAG, "ontextChanged");
    }

    @Override
    public void afterTextChanged(Editable s) {
        this.newSearchFilterListener.onNewSearchFilter(s.toString());
    }

    @Override
    public void onKeyIme(int keyCode, KeyEvent event) {
        if (isActive) {
            if (KeyEvent.KEYCODE_BACK == event.getKeyCode()) {
              //  exitSearchMode();
            }
        }
    }

    private boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(actionId== EditorInfo.IME_ACTION_DONE||actionId==EditorInfo.IME_ACTION_NEXT) {
            //exitSearchMode();
            return true;
        }
        return false;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public interface NewSearchFilterListener {
        void onNewSearchFilter(String filter);
    }

    public interface OnSearchExitListener {
        void onSearchExit();
    }
}
