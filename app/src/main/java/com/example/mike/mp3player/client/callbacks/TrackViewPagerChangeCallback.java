package com.example.mike.mp3player.client.callbacks;

import androidx.viewpager2.widget.ViewPager2;

import com.example.mike.mp3player.client.MediaControllerAdapter;

public class TrackViewPagerChangeCallback extends ViewPager2.OnPageChangeCallback {

    /** */
    private final MediaControllerAdapter mediaControllerAdapter;

    private int currentPosition;

    /** Constructor */
    public TrackViewPagerChangeCallback(MediaControllerAdapter mediaControllerAdapter) {
        this.mediaControllerAdapter = mediaControllerAdapter;
        this.currentPosition = mediaControllerAdapter.getCurrentQueuePosition();
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

    /** */
    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }
}
