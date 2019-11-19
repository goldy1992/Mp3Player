package com.github.goldy1992.mp3player.client.views.fragments.viewpager;

import android.os.Looper;
import android.support.v4.media.MediaBrowserCompat;

import androidx.fragment.app.testing.FragmentScenario;

import com.github.goldy1992.mp3player.client.MediaControllerAdapter;
import com.github.goldy1992.mp3player.client.activities.TestMainActivity;
import com.github.goldy1992.mp3player.client.views.fragments.FragmentTestBase;
import com.github.goldy1992.mp3player.commons.MediaItemBuilder;
import com.github.goldy1992.mp3player.commons.MediaItemType;
import com.github.goldy1992.mp3player.dagger.components.MediaActivityCompatComponent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.LooperMode;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
@LooperMode(LooperMode.Mode.PAUSED)
public class SongListFragmentTest extends FragmentTestBase<SongListFragment> {

    private SongListFragment folderListFragment;
    private ActivityController<TestMainActivity> activityScenario;
    @Before
    public void setup() {
        activityScenario = Robolectric.buildActivity(TestMainActivity.class).setup();
        TestMainActivity testMainActivity = activityScenario.get();
        MediaActivityCompatComponent component = testMainActivity.getMediaActivityCompatComponent();
        folderListFragment = new SongListFragment(MediaItemType.SONGS, "id", component);
        super.setup(folderListFragment, SongListFragment.class);
    }
    @Test
    public void testItemSelected() {
        FragmentScenario.FragmentAction<SongListFragment> action = this::itemSelected;
        performAction(action);
    }

    private void itemSelected(MediaItemListFragment fragment) {
        MediaControllerAdapter mediaControllerAdapter = mock(MediaControllerAdapter.class);
        fragment.setMediaControllerAdapter(mediaControllerAdapter);
        final String expectedLibraryId = "ID";
        MediaBrowserCompat.MediaItem mediaItem = new MediaItemBuilder("id")
            .setLibraryId(expectedLibraryId)
            .build();
        fragment.itemSelected(mediaItem);
        shadowOf(Looper.getMainLooper()).idle();
        verify(mediaControllerAdapter).playFromMediaId(expectedLibraryId, null);

    }
}
