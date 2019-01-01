package com.example.mike.mp3player.client.view;

import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.PlaybackStateWrapper;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MainActivityRootFragment extends Fragment implements SongSearchActionListener {

    private MainFrameFragment mainFrameFragment;
    private SongFilterFragment songFilterFragment;
    private InputMethodManager inputMethodManager = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_main_activity_root, null);
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

    public void setPlaybackState(PlaybackStateWrapper state) {
        final int newState = state.getPlaybackState().getState();
        PlayPauseButton playPauseButton = mainFrameFragment.getPlayToolBarFragment().getPlayPauseButton();
        playPauseButton.updateState(newState);
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
