package com.github.goldy1992.mp3player.client.views.viewholders;

import android.view.View;

import com.github.goldy1992.mp3player.client.AlbumArtPainter;

import org.mockito.Mock;

public abstract class MediaItemViewHolderTestBase<V extends MediaItemViewHolder> {

    @Mock
    View view;
    @Mock
    AlbumArtPainter albumArtPainter;

    V mediaItemViewHolder;


}
