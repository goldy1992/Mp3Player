package com.example.mike.mp3player.client.activities;

import android.support.v4.media.MediaBrowserCompat.MediaItem;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.callbacks.subscription.SubscriptionType;
import com.example.mike.mp3player.client.views.fragments.PlayToolBarFragment;
import com.example.mike.mp3player.client.views.fragments.SimpleTitleBarFragment;
import com.example.mike.mp3player.client.views.fragments.viewpager.ChildViewPagerFragment;
import com.example.mike.mp3player.client.views.fragments.viewpager.SongViewPagerFragment;
import com.example.mike.mp3player.commons.MediaItemType;
import com.example.mike.mp3player.commons.MediaItemUtils;

import org.apache.commons.lang3.exception.ExceptionUtils;

import java.lang.reflect.Field;

import static com.example.mike.mp3player.commons.Constants.MEDIA_ITEM;

public abstract class FolderActivity extends MediaActivityCompat {

    private static final String LOG_TAG = "FOLDER_ACTIVITY";
    private ChildViewPagerFragment viewPageFragment;
    private PlayToolBarFragment playToolBarFragment;
    private SimpleTitleBarFragment simpleTitleBarFragment;

    @Override
    SubscriptionType getSubscriptionType() {
        return SubscriptionType.MEDIA_ID;
    }

    @Override
    String getWorkerId() {
        return "FLDER_ACTVY_WKR";
    }

    @Override
    boolean initialiseView(int layoutId) {
        setContentView(layoutId);
        this.simpleTitleBarFragment = (SimpleTitleBarFragment) getSupportFragmentManager().findFragmentById(R.id.simpleTitleBarFragment);
        this.playToolBarFragment = (PlayToolBarFragment) getSupportFragmentManager().findFragmentById(R.id.playToolbarFragment);
        return true;
    }

    @Override
    public void onConnected() {
        super.onConnected();
        MediaItem mediaItem = getIntent().getParcelableExtra(MEDIA_ITEM);
        String itemLibraryId = MediaItemUtils.getLibraryId(mediaItem);
        this.viewPageFragment = new SongViewPagerFragment();
        this.viewPageFragment.init(MediaItemType.FOLDER, itemLibraryId);
        initialiseView(R.layout.activity_folder);
        getSupportFragmentManager().beginTransaction().add(R.id.songListFragment, viewPageFragment).commit();
        getSupportActionBar().setTitle(MediaItemUtils.getDirectoryName(mediaItem));

        TextView titleTextView = null;
        try{
        Field f = simpleTitleBarFragment.getToolbar().getClass().getDeclaredField("mTitleTextView");
        f.setAccessible(true);
        titleTextView = (TextView) f.get(simpleTitleBarFragment.getToolbar());

        titleTextView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        titleTextView.setFocusable(true);
        titleTextView.setFocusableInTouchMode(true);
        titleTextView.requestFocus();
        titleTextView.setSingleLine(true);
        titleTextView.setSelected(true);
        titleTextView.setMarqueeRepeatLimit(-1);
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            Log.e(LOG_TAG, ExceptionUtils.getMessage(ex));
        }
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

}
