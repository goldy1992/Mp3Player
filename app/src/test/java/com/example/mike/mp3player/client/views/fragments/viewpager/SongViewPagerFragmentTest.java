package com.example.mike.mp3player.client.views.fragments.viewpager;

import android.support.v4.media.MediaBrowserCompat;

import androidx.fragment.app.testing.FragmentScenario;

import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.commons.MediaItemBuilder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class SongViewPagerFragmentTest extends ChildViewPagerFragmentTestBase<SongViewPagerFragment> {

    @Before
    public void setup() {
        super.setup(SongViewPagerFragment.class);
    }

    @Test
    public void testItemSelected() {
        FragmentScenario.FragmentAction<SongViewPagerFragment> action = this::itemSelected;
        performAction(action);
    }

    private void itemSelected(ChildViewPagerFragment fragment) {
        MediaControllerAdapter mediaControllerAdapter = mock(MediaControllerAdapter.class);
        fragment.setMediaControllerAdapter(mediaControllerAdapter);
        final String expectedLibraryId = "ID";
        MediaBrowserCompat.MediaItem mediaItem = new MediaItemBuilder("id")
            .setLibraryId(expectedLibraryId)
            .build();
        fragment.itemSelected(mediaItem);
        verify(mediaControllerAdapter).playFromMediaId(expectedLibraryId, null);

    }
}
