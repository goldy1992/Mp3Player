package com.example.mike.mp3player.client.views.buttons;

import android.os.Bundle;
import android.support.v4.media.session.PlaybackStateCompat;
import android.widget.ImageView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static android.support.v4.media.session.PlaybackStateCompat.REPEAT_MODE_ALL;
import static android.support.v4.media.session.PlaybackStateCompat.REPEAT_MODE_INVALID;
import static android.support.v4.media.session.PlaybackStateCompat.REPEAT_MODE_NONE;
import static android.support.v4.media.session.PlaybackStateCompat.REPEAT_MODE_ONE;
import static com.example.mike.mp3player.commons.Constants.REPEAT_MODE;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class RepeatOneRepeatAllButtonTest extends MediaButtonTestBase {

    private RepeatOneRepeatAllButton repeatOneRepeatAllButton;


    @Before
    public void setup() {
        super.setup();
        this.repeatOneRepeatAllButton = new RepeatOneRepeatAllButton(context, mediaControllerAdapter, handler);
    }

    @Test
    public void testInit() {
        ImageView imageView = mock(ImageView.class);
        final @PlaybackStateCompat.RepeatMode int expectedState = REPEAT_MODE_NONE;
        when(mediaControllerAdapter.getRepeatMode()).thenReturn(expectedState);
        repeatOneRepeatAllButton.init(imageView);
        assertEquals(expectedState, repeatOneRepeatAllButton.getState());
    }

    @Test
    public void testOnClickWhenRepeatNone() {
        runOnClick(REPEAT_MODE_NONE, REPEAT_MODE_ONE);
    }

    @Test
    public void testOnClickWhenRepeatOne() {
        runOnClick(REPEAT_MODE_ONE, REPEAT_MODE_ALL);
    }

    @Test
    public void testOnClickWhenRepeatAll() {
        runOnClick(REPEAT_MODE_ALL, REPEAT_MODE_NONE);
    }

    @Test
    public void testOnClickWhenRepeatInvalid() {
        runOnClick(REPEAT_MODE_INVALID, REPEAT_MODE_NONE);
    }

    @Test
    public void testOnPlaybackStateChangedWithNoRepeatMode() {
        int expectedState = mediaControllerAdapter.getPlaybackState();
        PlaybackStateCompat state = new PlaybackStateCompat.Builder().build();
        repeatOneRepeatAllButton.onPlaybackStateChanged(state);
        assertEquals(expectedState, repeatOneRepeatAllButton.getState());
    }

    @Test
    public void testOnPlaybackStateChangedWithValidRepeatMode() {
        final int expectedState = REPEAT_MODE_ONE;
        repeatOneRepeatAllButton.setRepeatMode(REPEAT_MODE_NONE);
        Bundle extras = new Bundle();
        extras.putInt(REPEAT_MODE, expectedState);
        PlaybackStateCompat state = new PlaybackStateCompat.Builder()
                .setExtras(extras)
                .build();
        when(mediaControllerAdapter.getRepeatMode()).thenReturn(expectedState);
        repeatOneRepeatAllButton.onPlaybackStateChanged(state);

        assertEquals(expectedState, repeatOneRepeatAllButton.getState());
    }

    private void runOnClick(final @PlaybackStateCompat.RepeatMode int originalRepeatMode,
                            final @PlaybackStateCompat.RepeatMode int expectedRepeatMode) {
        ImageView imageView = mock(ImageView.class);
        repeatOneRepeatAllButton.setRepeatMode(originalRepeatMode);
        repeatOneRepeatAllButton.onClick(imageView);
        verify(mediaControllerAdapter, times(1)).setRepeatMode(expectedRepeatMode);
    }


}