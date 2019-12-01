package com.github.goldy1992.mp3player.client.callbacks;

import com.github.goldy1992.mp3player.client.MediaControllerAdapter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class TrackViewPagerChangeCallbackTest {

    private TrackViewPagerChangeCallback trackViewPagerChangeCallback;

    @Mock
    private MediaControllerAdapter mediaControllerAdapter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(mediaControllerAdapter.getCurrentQueuePosition()).thenReturn(0);
        this.trackViewPagerChangeCallback = new TrackViewPagerChangeCallback(mediaControllerAdapter);
    }

    @Test
    public void testSamePosition() {
        final int initialPosition = 0;
        this.trackViewPagerChangeCallback.currentPosition = initialPosition;
        this.trackViewPagerChangeCallback.onPageSelected(initialPosition);
        verify(mediaControllerAdapter, never()).seekTo(anyLong());
        verify(mediaControllerAdapter, never()).skipToPrevious();
        verify(mediaControllerAdapter, never()).skipToNext();
        assertEquals(initialPosition, trackViewPagerChangeCallback.currentPosition);
    }

    @Test
    public void testSkipToNext() {
        final int initialPosition = 0;
        this.trackViewPagerChangeCallback.currentPosition = initialPosition;

        final int skipToNextPosition = initialPosition + 1;
        trackViewPagerChangeCallback.onPageSelected(skipToNextPosition);

        assertEquals(skipToNextPosition, trackViewPagerChangeCallback.currentPosition);
        verify(mediaControllerAdapter, times(1)).skipToNext();
    }

    @Test
    public void testSkipToPrevious() {
        final int initialPosition = 2;
        this.trackViewPagerChangeCallback.currentPosition = initialPosition;

        final int skipToPreviousPosition = initialPosition - 1;
        trackViewPagerChangeCallback.onPageSelected(skipToPreviousPosition);

        assertEquals(skipToPreviousPosition, trackViewPagerChangeCallback.currentPosition);
        verify(mediaControllerAdapter, times(1)).seekTo(0);
        verify(mediaControllerAdapter, times(1)).skipToPrevious();
    }

    @Test
    public void testSkipMoreThanOnePosition() {
        final int initialPosition = 2;
        this.trackViewPagerChangeCallback.currentPosition = initialPosition;

        final int skipTwoPositions = initialPosition + 2;

        trackViewPagerChangeCallback.onPageSelected(skipTwoPositions);

        assertEquals(skipTwoPositions, trackViewPagerChangeCallback.currentPosition);
        verify(mediaControllerAdapter, never()).skipToPrevious();
        verify(mediaControllerAdapter, never()).skipToNext();
        verify(mediaControllerAdapter, never()).seekTo(anyLong());
    }
}