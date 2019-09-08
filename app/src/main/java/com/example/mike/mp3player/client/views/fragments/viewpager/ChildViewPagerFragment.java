package com.example.mike.mp3player.client.views.fragments.viewpager;

import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat.MediaItem;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.MediaBrowserAdapter;
import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.MyGenericItemTouchListener;
import com.example.mike.mp3player.client.activities.MediaActivityCompat;
import com.example.mike.mp3player.client.views.adapters.MyGenericRecycleViewAdapter;
import com.example.mike.mp3player.commons.MediaItemType;
import com.example.mike.mp3player.dagger.components.MediaActivityCompatComponent;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import javax.inject.Inject;

/**
 * Fragment to show a list of media items, has MediaBrowserAdapter injected into it using Dagger2
 * NOTE: MediaBrowserAdapter is not annotated with inject because:
 *
 * 1) we don't want to override the constructor for compatibility purposes
 * 2) This ChildViewFragment will be provided after the main injection is done
 */
public abstract class ChildViewPagerFragment extends Fragment implements MyGenericItemTouchListener.ItemSelectedListener {
    private static final String LOG_TAG = "GENRC_VIW_PGE_FRGMNT";

    /**
     * The parent for all the media items in this view; if null, the fragment represent a list of all available songs.
     */
    private FastScrollRecyclerView recyclerView;
    protected Class<? extends MediaActivityCompat> intentClass;
    private MediaItemType parentItemType;
    private String parentItemTypeId;
    protected MediaBrowserAdapter mediaBrowserAdapter;
    protected MediaControllerAdapter mediaControllerAdapter;
    private MyGenericRecycleViewAdapter myViewAdapter;
    private MyGenericItemTouchListener myGenericItemTouchListener;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        injectDependencies();
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_view_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle bundle) {
        this.recyclerView = view.findViewById(R.id.myRecyclerView);
        this.recyclerView.setAdapter(myViewAdapter);
        this.recyclerView.addOnItemTouchListener(myGenericItemTouchListener);
        this.myGenericItemTouchListener.setParentView(recyclerView);
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.mediaBrowserAdapter.registerListener(parentItemTypeId, myViewAdapter);
        this.mediaBrowserAdapter.subscribe(parentItemTypeId);
    }

    public void init(MediaItemType mediaItemType, String id) {
        this.parentItemType = mediaItemType;
        this.parentItemTypeId = id;
    }

    @Inject
    public void setMediaBrowserAdapter(MediaBrowserAdapter mediaBrowserAdapter) {
        this.mediaBrowserAdapter = mediaBrowserAdapter;
    }

    @Inject
    public void setMediaControllerAdapter(MediaControllerAdapter mediaControllerAdapter) {
        this.mediaControllerAdapter = mediaControllerAdapter;
    }

    @Inject
    public void setMyGenericRecycleViewAdapter(MyGenericRecycleViewAdapter adapter) {
        this.myViewAdapter = adapter;
    }

    @Inject
    public void setMyGenericItemTouchListener(MyGenericItemTouchListener listener) {
        this.myGenericItemTouchListener = listener;
    }

    @Inject
    public void setIntentClass(Class<? extends MediaActivityCompat> intentClass) {
        this.intentClass = intentClass;
    }

    private void injectDependencies() {
        FragmentActivity activity = getActivity();
        if (activity instanceof MediaActivityCompat) {
            MediaActivityCompat mediaPlayerActivity = (MediaActivityCompat) getActivity();
            MediaActivityCompatComponent component = mediaPlayerActivity.getMediaActivityCompatComponent();
            component.childViewPagerFragmentSubcomponentFactory()
                .create(parentItemType, parentItemTypeId, this).
                inject(this);
        }
    }
}
