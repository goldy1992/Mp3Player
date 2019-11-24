package com.github.goldy1992.mp3player.client.views.adapters;

import android.os.Handler;
import android.support.v4.media.MediaBrowserCompat;

import com.github.goldy1992.mp3player.client.views.viewholders.MyFolderViewHolder;
import com.github.goldy1992.mp3player.commons.MediaItemBuilder;
import com.github.goldy1992.mp3player.commons.MediaItemUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class MyFolderViewAdapterTest extends MediaItemRecyclerViewAdapterTestBase {

    private MyFolderViewAdapter myFolderViewAdapter;

    @Mock
    private MyFolderViewHolder myFolderViewHolder;
    @Mock
    private Handler handler;
    @Captor
    private ArgumentCaptor<MediaBrowserCompat.MediaItem> captor;
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        super.setup();
        this.myFolderViewAdapter = new MyFolderViewAdapter(albumArtPainter, handler);
    }

    @Test
    public void testOnCreateViewHolder() {
        MyFolderViewHolder result = (MyFolderViewHolder) myFolderViewAdapter.onCreateViewHolder(this.viewGroup, 0);
        assertNotNull(result);
    }

    @Test
    public void testOnBindViewHolder() {
        final String directoryPath= "/a/b/c";
        final String directoryName = "c";
        this.mediaItems.add(new MediaItemBuilder(directoryPath)
                .setTitle(directoryName)
                .setDescription(directoryPath)
                .build()
        );
        this.myFolderViewAdapter.notifyDataSetChanged();
        this.myFolderViewAdapter.setItems(mediaItems);
        this.myFolderViewAdapter.onBindViewHolder(myFolderViewHolder, 0);
        verify(myFolderViewHolder, times(1)).bindMediaItem(captor.capture());
        MediaBrowserCompat.MediaItem result = captor.getValue();
        assertEquals(directoryName, MediaItemUtils.getTitle(result));
        assertEquals(directoryPath, MediaItemUtils.getMediaId(result));
        assertEquals(directoryPath, MediaItemUtils.getDescription(result));
    }
}