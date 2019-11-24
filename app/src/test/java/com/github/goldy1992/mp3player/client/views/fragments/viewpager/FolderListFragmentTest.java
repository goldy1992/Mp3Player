package com.github.goldy1992.mp3player.client.views.fragments.viewpager;

import android.os.Looper;
import android.support.v4.media.MediaBrowserCompat;

import androidx.fragment.app.testing.FragmentScenario;

import com.github.goldy1992.mp3player.client.activities.TestMainActivity;
import com.github.goldy1992.mp3player.client.views.fragments.FragmentTestBase;
import com.github.goldy1992.mp3player.commons.MediaItemType;
import com.github.goldy1992.mp3player.dagger.components.MediaActivityCompatComponent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.LooperMode;

import static com.github.goldy1992.mp3player.TestUtils.createMediaItem;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
@LooperMode(LooperMode.Mode.PAUSED)
public class FolderListFragmentTest extends FragmentTestBase<FolderListFragment> {
    private static final String FRAGMENT_TAG = "FragmentScenario_Fragment_Tag";

    private FolderListFragment folderListFragment;
    private ActivityController<TestMainActivity> activityScenario;
    @Before
    public void setup() {
        activityScenario = Robolectric.buildActivity(TestMainActivity.class).setup();
        TestMainActivity testMainActivity = activityScenario.get();
        MediaActivityCompatComponent component = testMainActivity.getMediaActivityCompatComponent();
        folderListFragment = FolderListFragment.newInstance(MediaItemType.FOLDERS, "id", component);
        super.setup(folderListFragment, FolderListFragment.class);
    }

    @Test
    public void testItemSelected() {
        FragmentScenario.FragmentAction<FolderListFragment> action = this::itemSelected;
        performAction(action);
    }

    private void itemSelected(MediaItemListFragment fragment) {
        MediaItemListFragment spiedFragment = spy(fragment);
        final String id = "ID";
        final String title = "TITLE";
        final String description = "description";

        MediaBrowserCompat.MediaItem mediaItem =
                createMediaItem(id, title, description, MediaItemType.ROOT);
        spiedFragment.itemSelected(mediaItem);
        shadowOf(Looper.getMainLooper()).idle();
        verify(spiedFragment).startActivity(any());

    }
}
