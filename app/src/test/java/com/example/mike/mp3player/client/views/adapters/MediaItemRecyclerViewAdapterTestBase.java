package com.example.mike.mp3player.client.views.adapters;

import android.content.Context;
import android.os.Handler;
import android.support.v4.media.MediaBrowserCompat;
import android.util.AttributeSet;
import android.view.ViewGroup;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.mike.mp3player.client.AlbumArtPainter;

import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MediaItemRecyclerViewAdapterTestBase {

    @Mock
    Handler handler;
    @Mock
    AlbumArtPainter albumArtPainter;
    Context context;
    ViewGroup viewGroup;
    List<MediaBrowserCompat.MediaItem> mediaItems;

    public void setup() {
        this.context = InstrumentationRegistry.getInstrumentation().getContext();
        this.viewGroup = getMockViewGroup();
        this.mediaItems = new ArrayList<>();
    }

    private ViewGroup getMockViewGroup() {
        ViewGroup viewGroup = mock(ViewGroup.class);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        when(viewGroup.generateLayoutParams(any(AttributeSet.class))).thenReturn(layoutParams);
        when(viewGroup.getContext()).thenReturn(this.context);
        return viewGroup;
    }
}
