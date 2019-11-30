package com.github.goldy1992.mp3player.service.library.content.observers;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v4.media.MediaBrowserCompat.MediaItem;

import com.github.goldy1992.mp3player.service.library.ContentManager;
import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds;
import com.github.goldy1992.mp3player.service.library.search.managers.FolderDatabaseManager;
import com.github.goldy1992.mp3player.service.library.search.managers.SongDatabaseManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class AudioObserverTest {

    private AudioObserver audioObserver;

    private MediaItemTypeIds mediaItemTypeIds;

    @Mock
    private ContentResolver contentResolver;

    @Mock
    private ContentManager contentManager;

    @Mock
    private SongDatabaseManager songDatabaseManager;

    @Mock
    private FolderDatabaseManager folderDatabaseManager;

    private Handler handler;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.handler = new Handler(Looper.getMainLooper());
        this.mediaItemTypeIds = new MediaItemTypeIds();
        this.audioObserver = new AudioObserver(
                handler,
                contentResolver,
                contentManager,
                songDatabaseManager,
                folderDatabaseManager,
                mediaItemTypeIds);
    }

    @Test
    public void testNullUri() {
        audioObserver.onChange(true);
        verify(contentManager, never()).getItem(anyLong());
    }

    @Test
    public void testOnChangeParsableUriValidIdNoContent() {
        final long expectedId = 2334L;
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        uri = ContentUris.withAppendedId(uri, expectedId);
        when(contentManager.getItem(expectedId)).thenReturn(null);
        this.audioObserver.onChange(true, uri);
        verify(contentManager, times(1)).getItem(expectedId);
        verify(songDatabaseManager, never()).insert(any(MediaItem.class));
        verify(folderDatabaseManager, never()).insert(any(MediaItem.class));
    }

    @Test
    public void testOnChangeParsableUriWithValidId() {
        final long expectedId = 2334L;
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        uri = ContentUris.withAppendedId(uri, expectedId);
        MediaItem result = mock(MediaItem.class);
        when(contentManager.getItem(expectedId)).thenReturn(result);
        this.audioObserver.onChange(true, uri);
        verify(contentManager, times(1)).getItem(expectedId);
        verify(songDatabaseManager, times(1)).insert(result);
        verify(folderDatabaseManager, times(1)).insert(result);
    }
}