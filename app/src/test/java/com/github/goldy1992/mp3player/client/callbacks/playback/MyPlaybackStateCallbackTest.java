package com.github.goldy1992.mp3player.client.callbacks.playback;

import android.os.Handler;
import android.support.v4.media.session.PlaybackStateCompat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.util.HashSet;
import java.util.Set;

import static com.github.goldy1992.mp3player.commons.Constants.ACTION_PLAYBACK_SPEED_CHANGED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class MyPlaybackStateCallbackTest {
    /** a mock PlaybackStateListener */
    @Mock
    private PlaybackStateListener mockPlaybackStateListener1;
    /** a mock PlaybackStateListener */
    @Mock
    private PlaybackStateListener mockPlaybackStateListener2;
    /** the Playback State Callback to test */
    private MyPlaybackStateCallback myPlaybackStateCallback;
    /** Handler for initialisation */
    @Mock
    private Handler handler;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.myPlaybackStateCallback = new MyPlaybackStateCallback(handler);
    }
    /**
     * GIVEN: 1) A playback AND repeat listener, 2) A shuffle listener, 3) a playback speed listener
     * WHEN: the callback is invoked with an action of each type
     * THEN: 1) each listener is invoked once
     */
    @Test
    public void testMultiListener() {
        Set<ListenerType> listenerSet1 = new HashSet<>();
        listenerSet1.add(ListenerType.PLAYBACK);
        listenerSet1.add(ListenerType.REPEAT);
        myPlaybackStateCallback.registerPlaybackStateListener(mockPlaybackStateListener1);
        myPlaybackStateCallback.registerPlaybackStateListener(mockPlaybackStateListener2);

        PlaybackStateListener extraListener = mock(PlaybackStateListener.class);
        myPlaybackStateCallback.registerPlaybackStateListener(extraListener);

        long actions =
                PlaybackStateCompat.ACTION_PAUSE |
                PlaybackStateCompat.ACTION_SET_REPEAT_MODE |
                PlaybackStateCompat.ACTION_SET_SHUFFLE_MODE |
                ACTION_PLAYBACK_SPEED_CHANGED;
        PlaybackStateCompat state = createPlaybackStateCompat(actions);
        myPlaybackStateCallback.processCallback(state);

        verify(mockPlaybackStateListener1, times(1)).onPlaybackStateChanged(state);
        verify(mockPlaybackStateListener2, times(1)).onPlaybackStateChanged(state);
        verify(extraListener, times(1)).onPlaybackStateChanged(state);
    }
    /**
     * GIVEN: a registered PlaybackStateListener 'L'
     * WHEN: removePlaybackStateListener is invoked with 'L' as the parameter
     * THEN: the result is true
     */
    @Test
    public void testRemovePlaybackStateListener() {
        myPlaybackStateCallback.registerPlaybackStateListener(mockPlaybackStateListener1);
        final int originalSize = myPlaybackStateCallback.getListeners().size();
        boolean result = myPlaybackStateCallback.removePlaybackStateListener(mockPlaybackStateListener1);
        assertTrue(result);
        final int expectedNewSize = originalSize - 1;
        assertEquals(expectedNewSize, myPlaybackStateCallback.getListeners().size());
    }
    /**
     * GIVEN: an empty PlaybackStateListener set
     * WHEN: registerPlaybackStateListener is invoked
     * THEN: the result is false because nothing was removed
     */
    @Test
    public void testRemovePlaybackStateListenerOnEmptySet() {
       boolean result = myPlaybackStateCallback.removePlaybackStateListener(null);
       assertFalse(result);
    }
    /**
     * Util method to create a PlaybackStateCompat object
     * @param actions the actions
     * @return a PlaybackStateCompat with am actions long variable appended
     */
    private PlaybackStateCompat createPlaybackStateCompat(long actions) {
       return new PlaybackStateCompat.Builder().setActions(actions).build();
    }


}