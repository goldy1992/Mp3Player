package com.example.mike.mp3player.service.session;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.media.MediaBrowserCompat.MediaItem;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.KeyEvent;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.mike.mp3player.client.Category;
import com.example.mike.mp3player.commons.library.LibraryObject;
import com.example.mike.mp3player.service.MediaPlaybackService;
import com.example.mike.mp3player.service.PlaybackManager;
import com.example.mike.mp3player.service.ServiceManager;
import com.example.mike.mp3player.service.library.MediaLibrary;
import com.example.mike.mp3player.service.player.MediaPlayerAdapter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.util.Collections;
import java.util.List;

import static android.support.v4.media.session.PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS;
import static android.support.v4.media.session.PlaybackStateCompat.REPEAT_MODE_ALL;
import static android.support.v4.media.session.PlaybackStateCompat.REPEAT_MODE_NONE;
import static android.support.v4.media.session.PlaybackStateCompat.STATE_PLAYING;
import static com.example.mike.mp3player.TestUtils.createMediaItem;
import static com.example.mike.mp3player.commons.Constants.DECREASE_PLAYBACK_SPEED;
import static com.example.mike.mp3player.commons.Constants.INCREASE_PLAYBACK_SPEED;
import static com.example.mike.mp3player.commons.Constants.PARENT_ID;
import static com.example.mike.mp3player.service.session.MediaSessionCallback.DEFAULT_PLAYBACK_SPEED_CHANGE;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class MediaSessionCallbackTest {
    /** object to test */
    private MediaSessionCallback mediaSessionCallback;
    @Mock
    private MediaPlaybackService mediaPlaybackService;
    @Mock
    private MediaSessionAdapter mediaSessionAdapter;
    @Mock
    private MediaLibrary mediaLibrary;
    @Mock
    private PlaybackManager playbackManager;
    @Mock
    private MediaPlayerAdapter mediaPlayerAdapter;
    @Mock
    private ServiceManager serviceManager;
    @Mock
    private AudioBecomingNoisyBroadcastReceiver broadcastReceiver;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        when(mediaPlaybackService.getApplicationContext()).thenReturn(context);
        this.mediaSessionCallback = new MediaSessionCallback(mediaLibrary,
                playbackManager, mediaPlayerAdapter, mediaSessionAdapter, serviceManager, broadcastReceiver, new Handler(Looper.myLooper()));
        reset(mediaSessionAdapter);
        reset(mediaPlayerAdapter);
    }

    @Test
    public void testPlayReturnTrue() {
        when(mediaPlayerAdapter.play()).thenReturn(true);
        mediaSessionCallback.onPlay();
        verify(mediaPlayerAdapter, times(1)).play();
        verify(broadcastReceiver, times(1)).registerAudioNoisyReceiver();
        verify(serviceManager, times(1)).startService();
    }

    @Test
    public void testPlayReturnFalse() {
        when(mediaPlayerAdapter.play()).thenReturn(false);
        mediaSessionCallback.onPlay();
        verify(mediaPlayerAdapter, times(1)).play();
        verify(broadcastReceiver, never()).registerAudioNoisyReceiver();
        verify(serviceManager, never()).startService();
    }

    @Test
    public void testPauseReturnTrue() {
        when(mediaPlayerAdapter.pause()).thenReturn(true);
        mediaSessionCallback.onPause();
        verify(mediaPlayerAdapter, times(1)).pause();
        verify(broadcastReceiver, times(1)).unregisterAudioNoisyReceiver();
        verify(serviceManager, times(1)).pauseService();
    }

    @Test
    public void testPauseReturnFalse() {
        when(mediaPlayerAdapter.pause()).thenReturn(false);
        mediaSessionCallback.onPause();
        verify(mediaPlayerAdapter, times(1)).pause();
        verify(broadcastReceiver, never()).registerAudioNoisyReceiver();
        verify(serviceManager, never()).startService();
    }
    @Test
    public void testSkipToNext() {
        setUpSkipToNextTest();
        mediaSessionCallback.onSkipToNext();
        verify(mediaPlayerAdapter, times(1)).reset(any(), any());
        verify(serviceManager, times(1)).notifyService();
    }

    private void setUpSkipToNextTest() {
        final String newMediaId = "newMediaID";
        when(playbackManager.skipToNext()).thenReturn(newMediaId);
        when(mediaSessionAdapter.getCurrentPlaybackState(anyLong())).thenReturn(createState(STATE_PLAYING));
    }

    @Test
    public void testSkipToPreviousPositionGreaterThanOneSecond() {
        // position greater than 1000 ms
        final int position = 1001;
        when(mediaPlayerAdapter.getCurrentPlaybackPosition()).thenReturn(position);
        mediaSessionCallback.onSkipToPrevious();
        verify(mediaPlayerAdapter, times(1)).seekTo(1);
        verify(playbackManager, never()).skipToPrevious();
        verify(serviceManager, never()).notifyService();
        verify(mediaSessionAdapter, never()).updateAll(ACTION_SKIP_TO_PREVIOUS);
    }
    @Test
    public void testSkipToPreviousPositionLessThanOneSecond() {
        // position less than 1000 ms
        final int position = 5;
        when(mediaPlayerAdapter.getCurrentPlaybackPosition()).thenReturn(position);
        when(mediaSessionAdapter.getCurrentPlaybackState(anyLong())).thenReturn(createState(STATE_PLAYING));
        mediaSessionCallback.onSkipToPrevious();
        verify(mediaPlayerAdapter, never()).seekTo(anyLong());
        verify(playbackManager, times(1)).skipToPrevious();
        verify(serviceManager, times(1)).notifyService();
        verify(mediaSessionAdapter, times(1)).updateAll(ACTION_SKIP_TO_PREVIOUS);
    }
    @Test
    public void testMediaButtonEventNullIntent() {
        Intent intent = null;
        assertFalse(mediaSessionCallback.onMediaButtonEvent(intent));
    }

    @Test
    public void testMediaButtonEventPlay() {
        Intent intent = createKeyEventIntent(KeyEvent.KEYCODE_MEDIA_PLAY);
        boolean result = mediaSessionCallback.onMediaButtonEvent(intent);
        assertTrue(result);
        verify(mediaPlayerAdapter, times(1)).play();
    }
    @Test
    public void testMediaButtonEventPause() {
        Intent intent = createKeyEventIntent(KeyEvent.KEYCODE_MEDIA_PAUSE);
        boolean result = mediaSessionCallback.onMediaButtonEvent(intent);
        assertTrue(result);
        verify(mediaPlayerAdapter, times(1)).pause();
    }
    @Test
    public void testMediaButtonEventSkipToPrevious() {
        Intent intent = createKeyEventIntent(KeyEvent.KEYCODE_MEDIA_PREVIOUS);
        final int position = 1001;
        when(mediaPlayerAdapter.getCurrentPlaybackPosition()).thenReturn(position);
        boolean result = mediaSessionCallback.onMediaButtonEvent(intent);
        assertTrue(result);
        verify(mediaPlayerAdapter, times(1)).seekTo(anyLong());
    }
    @Test
    public void testMediaButtonEventSkipToNext() {
        Intent intent = createKeyEventIntent(KeyEvent.KEYCODE_MEDIA_NEXT);
        setUpSkipToNextTest();
        boolean result = mediaSessionCallback.onMediaButtonEvent(intent);
        assertTrue(result);
        verify(mediaPlayerAdapter, times(1)).reset(any(), any());
        verify(serviceManager, times(1)).notifyService();
    }

    @Test
    public void testPrepareFromMediaId() {
        final String mediaId = "5452213";
        final Bundle bundle = new Bundle();
        LibraryObject parent = new LibraryObject(Category.ARTISTS, mediaId);
        bundle.putParcelable(PARENT_ID, parent);
        List<MediaItem> mediaItems = Collections.singletonList(createMediaItem(mediaId, null, null));
        when(mediaLibrary.getPlaylist(parent)).thenReturn(mediaItems);
        mediaSessionCallback.onPrepareFromMediaId(mediaId, bundle);
        verify(mediaPlayerAdapter, times(1)).reset(any(), any());
    }

    @Test
    public void testSetShuffleMode() {
        mediaSessionCallback.onSetShuffleMode(PlaybackStateCompat.SHUFFLE_MODE_ALL);
        verify(mediaPlayerAdapter, times(1)).setNextMediaPlayer(any());
    }

    @Test
    public void testSetRepeatModeAll() {
        mediaSessionCallback.onSetRepeatMode(REPEAT_MODE_ALL);
        verify(mediaPlayerAdapter, times(1)).updateRepeatMode(REPEAT_MODE_ALL);
        verify(playbackManager, times(1)).setRepeating(true);
    }

    @Test
    public void testSetRepeatModeNone() {
        mediaSessionCallback.onSetRepeatMode(REPEAT_MODE_NONE);
        verify(mediaPlayerAdapter, times(1)).updateRepeatMode(REPEAT_MODE_NONE);
        verify(playbackManager, times(1)).setRepeating(false);
    }

    @Test
    public void testCustomActionIncreasePlaybackSpeed() {
        mediaSessionCallback.onCustomAction(INCREASE_PLAYBACK_SPEED, null);
        verify(mediaPlayerAdapter, times(1)).increaseSpeed(DEFAULT_PLAYBACK_SPEED_CHANGE);
    }

    @Test
    public void testCustomActionDecreasePlaybackSpeed() {
        mediaSessionCallback.onCustomAction(DECREASE_PLAYBACK_SPEED, null);
        verify(mediaPlayerAdapter, times(1)).decreaseSpeed(DEFAULT_PLAYBACK_SPEED_CHANGE);
    }

    @Test
    public void testCustomActionInvalidAction() {
        mediaSessionCallback.onCustomAction("invalid action", null);
        verify(mediaPlayerAdapter, never()).decreaseSpeed(anyFloat());
        verify(mediaPlayerAdapter, never()).increaseSpeed(anyFloat());
    }

    @Test
    public void testOnCompletion() {
        final String mediaId = "344234";
        MediaPlayer mediaPlayer = mock(MediaPlayer.class);
        Uri uri = mock(Uri.class);
        when(mediaLibrary.getMediaUriFromMediaId(mediaId)).thenReturn(uri);
        when(mediaPlayerAdapter.getCurrentMediaPlayer()).thenReturn(mediaPlayer);
        when(mediaPlayer.isLooping()).thenReturn(false);
        when(playbackManager.getNext()).thenReturn(mediaId);
        mediaSessionCallback.onCompletion(mediaPlayer);
        verify(mediaPlayerAdapter, times(1)).onComplete(uri);
    }

    @Test
    public void testAddQueueItem() {
        MediaDescriptionCompat description = new MediaDescriptionCompat.Builder().build();
        mediaSessionCallback.onAddQueueItem(description);
        verify(mediaSessionAdapter, times(1)).setQueue(any());
    }

    @Test
    public void testRemoveQueueItem() {
        MediaDescriptionCompat description = new MediaDescriptionCompat.Builder().build();
        mediaSessionCallback.onRemoveQueueItem(description);
        verify(mediaSessionAdapter, times(1)).setQueue(any());
    }

    @Test
    public void testOnSeekTo() {
        final long position = 98;
        mediaSessionCallback.onSeekTo(position);
        verify(mediaPlayerAdapter, times(1)).seekTo(position);
    }

    private PlaybackStateCompat createState(@PlaybackStateCompat.State int playbackstate) {
        return new PlaybackStateCompat.Builder().setState(playbackstate, 0L, 0f).build();
    }

    private Intent createKeyEventIntent(final int keyCode) {
        Intent intent = new Intent();
        KeyEvent keyEvent = new KeyEvent(0, keyCode);
        intent.putExtra(Intent.EXTRA_KEY_EVENT, keyEvent);
        return intent;
    }
}