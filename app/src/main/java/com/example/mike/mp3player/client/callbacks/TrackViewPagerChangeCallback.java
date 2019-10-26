package com.example.mike.mp3player.client.callbacks;

import androidx.viewpager2.widget.ViewPager2;

import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.views.adapters.TrackViewAdapter;

public class TrackViewPagerChangeCallback extends ViewPager2.OnPageChangeCallback {

    /** */
    private final TrackViewAdapter trackViewAdapter;
    /** */
    private final MediaControllerAdapter mediaControllerAdapter;

    private int currentPosition = 0;

    /** Constructor */
    public TrackViewPagerChangeCallback(TrackViewAdapter trackViewAdapter,
                                        MediaControllerAdapter mediaControllerAdapter) {
        this.trackViewAdapter = trackViewAdapter;
        this.mediaControllerAdapter = mediaControllerAdapter;
    }
    /**
     *
     * @param position
     */
    @Override
    public void onPageSelected(int position) {

        if (this.currentPosition == position) {
            return;
        }
        if (isSkipToNext(position)) {
            mediaControllerAdapter.skipToNext();
        }
        else if (isSkipToPrevious(position)) {
            mediaControllerAdapter.seekTo(0);
            mediaControllerAdapter.skipToPrevious();
        }

        this.currentPosition = position;
    }

    private boolean isSkipToNext(int position) {
        return position == (currentPosition + 1);
    }

    private boolean isSkipToPrevious(int position) {
        return position == (currentPosition - 1);
    }
}
