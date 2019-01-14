package com.example.mike.mp3player.client.view.fragments;

import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.view.MediaPlayerActionListener;
import com.example.mike.mp3player.client.view.PlayPauseButton;
import com.example.mike.mp3player.client.view.SongSearchActionListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class MainActivityRootFragment extends Fragment implements SongSearchActionListener {

    private MainFrameFragment mainFrameFragment;
    private SongFilterFragment songFilterFragment;
    private InputMethodManager inputMethodManager = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        LinearLayout root = new LinearLayout(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        root.setLayoutParams(layoutParams);
        return inflater.inflate(R.layout.fragment_main_activity_root, root);
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        this.mainFrameFragment = (MainFrameFragment) getChildFragmentManager().findFragmentById(R.id.mainFrameFragment);
        this.songFilterFragment = (SongFilterFragment) getChildFragmentManager().findFragmentById(R.id.searchSongViewFragment);
    }

    public void setActionListeners(MediaPlayerActionListener mediaPlayerActionListener) {
        this.getMainFrameFragment().getPlayToolBarFragment().setMediaPlayerActionListener(mediaPlayerActionListener);
        this.getMainFrameFragment().getTitleBarFragment().setSongSearchActionListener(this);
        this.getSongFilterFragment().setSongSearchActionListener(this);
    }

    @Override // SongSearchActionListener
    public void onStartSearch() {
        mainFrameFragment.disable();
        songFilterFragment.getView().bringToFront();
        songFilterFragment.onSearchStart(inputMethodManager);
        songFilterFragment.setActive(true);
    }

    @Override // SongSearchActionListener
    public void onFinishSearch() {
        mainFrameFragment.enable();
        mainFrameFragment.getView().bringToFront();
        songFilterFragment.onSearchFinish(inputMethodManager);
    }

    @Override // SongSearchActionListener
    public void onNewSearchFilter(String filter) {
        mainFrameFragment.getRecyclerView().filter(filter);
    }

    public void initRecyclerView(List<MediaBrowserCompat.MediaItem> songs, MediaPlayerActionListener mediaPlayerActionListener) {
        mainFrameFragment.initRecyclerView(songs, mediaPlayerActionListener);
    }

    public void setPlaybackState(PlaybackStateCompat state) {
        if (state != null) {
            @PlaybackStateCompat.State
            final int newState = state.getState();
            PlayPauseButton playPauseButton = mainFrameFragment.getPlayToolBarFragment().getPlayPauseButton();
            playPauseButton.updateState(newState);
        }
    }

    public MainFrameFragment getMainFrameFragment() {
        return mainFrameFragment;
    }

    public SongFilterFragment getSongFilterFragment() {
        return songFilterFragment;
    }

    public void setInputMethodManager(InputMethodManager inputMethodManager) {
        this.inputMethodManager = inputMethodManager;
    }
}
