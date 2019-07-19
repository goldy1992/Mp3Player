package com.example.mike.mp3player.client.views.fragments.viewpager;

import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat.MediaItem;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerTabStrip;
import androidx.viewpager.widget.ViewPager;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.MediaBrowserAdapter;
import com.example.mike.mp3player.client.MediaBrowserResponseListener;
import com.example.mike.mp3player.client.activities.MediaActivityCompat;
import com.example.mike.mp3player.commons.MediaItemUtils;
import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.library.LibraryObject;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.inject.Inject;

import static com.example.mike.mp3player.client.views.fragments.viewpager.ChildViewPagerFragment.createViewPageFragment;
import static com.example.mike.mp3player.commons.ComparatorUtils.compareRootMediaItemsByCategory;

@Deprecated
public class ViewPagerFragment{}
//extends Fragment implements MediaBrowserResponseListener {
//
//    private ViewPager rootMenuItemsPager;
//    private PagerTabStrip pagerTabStrip;
//    private MediaBrowserAdapter mediaBrowserAdapter;
//
//    private static final String LOG_TAG = "VIEW_PAGER_FRAGMENT";
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
//                             @Nullable Bundle savedInstanceState) {
//        initialiseDependencies();
//        super.onCreateView(inflater, container, savedInstanceState);
//        return inflater.inflate(R.layout.fragment_view_pager, container, true);
//    }
//
//    @Override
//    public void onViewCreated(View view, Bundle bundle) {
//        this.rootMenuItemsPager = view.findViewById(R.id.rootItemsPager);
//        this.pagerTabStrip = view.findViewById(R.id.pagerTabStrip);
//        this.adapter = new MyPagerAdapter(getFragmentManager());
//        this.rootMenuItemsPager.setAdapter(adapter);
//        if (null != mediaBrowserAdapter) {
//            this.mediaBrowserAdapter.registerListener(Category.ROOT.name(), this);
//        }
//    }
//
//    public void enable() { /* TODO: implement enable */}
//    public void disable() {/* TODO: implement disable */}
//
//    @Override
//    public void onChildrenLoaded(@NonNull String parentId, @NonNull ArrayList<MediaItem> children, @NonNull Bundle options) {
//
//
//    }
//
//    @VisibleForTesting
//    public void setMyPageAdapter(MyPagerAdapter myPageAdapter) {
//        this.adapter = myPageAdapter;
//    }
//
//
//
//    @Inject
//    public void setMediaBrowserAdapter(MediaBrowserAdapter mediaBrowserAdapter) {
//        this.mediaBrowserAdapter = mediaBrowserAdapter;
//    }
//
//
//    private void initialiseDependencies() {
//        FragmentActivity activity = getActivity();
//        if (null != activity && activity instanceof MediaActivityCompat) {
//            MediaActivityCompat mediaPlayerActivity = (MediaActivityCompat) getActivity();
//            mediaPlayerActivity.getMediaActivityCompatComponent()
//                    .inject(this);
//        }
//    }
//}
