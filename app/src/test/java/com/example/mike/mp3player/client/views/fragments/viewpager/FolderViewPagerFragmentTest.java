package com.example.mike.mp3player.client.views.fragments.viewpager;

import android.support.v4.media.MediaBrowserCompat;

import androidx.fragment.app.testing.FragmentScenario;

import com.example.mike.mp3player.commons.MediaItemType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static com.example.mike.mp3player.TestUtils.createMediaItem;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class FolderViewPagerFragmentTest extends ChildViewPagerFragmentTestBase<FolderViewPagerFragment> {

    @Before
    public void setup() {
        super.setup(FolderViewPagerFragment.class);
    }

    @Test
    public void testItemSelected() {
        FragmentScenario.FragmentAction<FolderViewPagerFragment> action = this::itemSelected;
        performAction(action);
    }

    private void itemSelected(ChildViewPagerFragment fragment) {
        ChildViewPagerFragment spiedFragment = spy(fragment);
        final String id = "ID";
        final String title = "TITLE";
        final String description = "description";

        MediaBrowserCompat.MediaItem mediaItem =
                createMediaItem(id, title, description, MediaItemType.ROOT);
        spiedFragment.itemSelected(mediaItem);
        verify(spiedFragment).startActivity(any());

    }
}
