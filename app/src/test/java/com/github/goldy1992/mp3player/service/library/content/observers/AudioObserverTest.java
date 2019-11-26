package com.github.goldy1992.mp3player.service.library.content.observers;

import android.content.ContentResolver;
import android.os.Handler;
import android.os.Looper;

import com.github.goldy1992.mp3player.service.library.ContentManager;
import com.github.goldy1992.mp3player.service.library.search.managers.FolderDatabaseManager;
import com.github.goldy1992.mp3player.service.library.search.managers.SongDatabaseManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static org.hamcrest.Matchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class AudioObserverTest {

    private AudioObserver audioObserver;

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
        this.audioObserver = new AudioObserver(
                handler,
                contentResolver,
                contentManager,
                songDatabaseManager,
                folderDatabaseManager);
    }

    @Test
    public void testNullUri() {
        audioObserver.onChange(true);
        verify(contentManager, never()).getItem(anyLong());
    }
}