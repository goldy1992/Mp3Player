package com.github.goldy1992.mp3player.client.views.fragments.viewpager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.github.goldy1992.mp3player.R;
import com.github.goldy1992.mp3player.client.AlbumArtPainter;
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter;
import com.github.goldy1992.mp3player.client.MediaControllerAdapter;
import com.github.goldy1992.mp3player.client.MyGenericItemTouchListener;
import com.github.goldy1992.mp3player.client.activities.MediaActivityCompat;
import com.github.goldy1992.mp3player.client.views.adapters.MyGenericRecycleViewAdapter;
import com.github.goldy1992.mp3player.commons.MediaItemType;
import com.github.goldy1992.mp3player.dagger.components.MediaActivityCompatComponent;
import com.l4digital.fastscroll.FastScrollRecyclerView;

import javax.inject.Inject;

/**
 * Fragment to show a list of media items, has MediaBrowserAdapter injected into it using Dagger2
 * NOTE: MediaBrowserAdapter is not annotated with inject because:
 *
 * 1) we don't want to override the constructor for compatibility purposes
 * 2) This ChildViewFragment will be provided after the main injection is done
 */
public abstract class MediaItemListFragment extends Fragment implements MyGenericItemTouchListener.ItemSelectedListener {

    private static final String LOG_TAG = "GENRC_VIW_PGE_FRGMNT";

    /**
     * The parent for all the media items in this view; if null, the fragment represent a list of all available songs.
     */
    private FastScrollRecyclerView recyclerView;
    protected Class<? extends MediaActivityCompat> intentClass;
    private LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
    private MediaItemType parentItemType;
    private String parentItemTypeId;
    protected MediaBrowserAdapter mediaBrowserAdapter;
    protected MediaControllerAdapter mediaControllerAdapter;
    private MyGenericRecycleViewAdapter myViewAdapter;
    private AlbumArtPainter albumArtPainter;
    private MyGenericItemTouchListener myGenericItemTouchListener;

    public MediaItemListFragment(MediaItemType mediaItemType, String id, MediaActivityCompatComponent component) {
        this.parentItemType = mediaItemType;
        this.parentItemTypeId = id;
        injectDependencies(component);
        this.mediaBrowserAdapter.registerListener(parentItemTypeId, myViewAdapter);
        this.mediaBrowserAdapter.subscribe(parentItemTypeId);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_view_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle bundle) {
        this.recyclerView = view.findViewById(R.id.recycler_view);
        this.recyclerView.setAdapter(myViewAdapter);
        this.recyclerView.addOnItemTouchListener(myGenericItemTouchListener);
        this.myGenericItemTouchListener.setParentView(recyclerView);
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
        this.recyclerView.setLayoutManager(linearLayoutManager);
        this.recyclerView.addOnScrollListener(albumArtPainter.createPreloader(myViewAdapter));
        this.recyclerView.setHideScrollbar(true);
    }

    @Inject
    public void setMediaBrowserAdapter(MediaBrowserAdapter mediaBrowserAdapter) {
        this.mediaBrowserAdapter = mediaBrowserAdapter;
    }

    @Inject
    public void setAlbumArtPainter(AlbumArtPainter albumArtPainter) {
        this.albumArtPainter = albumArtPainter;
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

    private void injectDependencies(MediaActivityCompatComponent component) {
        component.childViewPagerFragmentSubcomponentFactory()
            .create(parentItemType, parentItemTypeId, this).
            inject(this);
    }
}
