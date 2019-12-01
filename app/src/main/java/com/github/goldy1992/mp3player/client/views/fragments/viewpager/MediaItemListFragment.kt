package com.github.goldy1992.mp3player.client.views.fragments.viewpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.goldy1992.mp3player.R
import com.github.goldy1992.mp3player.client.*
import com.github.goldy1992.mp3player.client.MyGenericItemTouchListener.ItemSelectedListener
import com.github.goldy1992.mp3player.client.views.adapters.MyGenericRecycleViewAdapter
import com.github.goldy1992.mp3player.client.views.adapters.RecyclerViewAdapters
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.dagger.components.MediaActivityCompatComponent
import com.l4digital.fastscroll.FastScrollRecyclerView
import javax.inject.Inject

/**
 * Fragment to show a list of media items, has MediaBrowserAdapter injected into it using Dagger2
 * NOTE: MediaBrowserAdapter is not annotated with inject because:
 *
 * 1) we don't want to override the constructor for compatibility purposes
 * 2) This ChildViewFragment will be provided after the main injection is done
 */
abstract class MediaItemListFragment : Fragment(), ItemSelectedListener {
    /**
     * The parent for all the media items in this view; if null, the fragment represent a list of all available songs.
     */
    private var recyclerView: FastScrollRecyclerView? = null
    protected var intentMapper: IntentMapper? = null
    private val linearLayoutManager = LinearLayoutManager(context)
    var parentItemType: MediaItemType? = null
    private var parentItemTypeId: String? = null
    protected var mediaBrowserAdapter: MediaBrowserAdapter? = null
    protected var mediaControllerAdapter: MediaControllerAdapter? = null
    private var recyclerViewAdapters: RecyclerViewAdapters? = null
    private var myViewAdapter: MyGenericRecycleViewAdapter? = null
    private var albumArtPainter: AlbumArtPainter? = null
    private var myGenericItemTouchListener: MyGenericItemTouchListener? = null
    fun init(mediaItemType: MediaItemType?, id: String?, component: MediaActivityCompatComponent) {
        parentItemType = mediaItemType
        parentItemTypeId = id
        injectDependencies(component)
        myViewAdapter = recyclerViewAdapters!!.getAdapter(mediaItemType)
        mediaBrowserAdapter!!.registerListener(parentItemTypeId, myViewAdapter)
        mediaBrowserAdapter!!.subscribe(parentItemTypeId)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_view_page, container, false)
    }

    override fun onViewCreated(view: View, bundle: Bundle?) {
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.setAdapter(myViewAdapter)
        recyclerView.addOnItemTouchListener(myGenericItemTouchListener!!)
        myGenericItemTouchListener!!.setParentView(recyclerView)
        recyclerView.setItemAnimator(DefaultItemAnimator())
        recyclerView.setLayoutManager(linearLayoutManager)
        val preloader = albumArtPainter!!.createPreloader(myViewAdapter)
        if (null != preloader) {
            recyclerView.addOnScrollListener(albumArtPainter!!.createPreloader(myViewAdapter))
        }
        recyclerView.setHideScrollbar(true)
    }

    @Inject
    fun setMediaBrowserAdapter(mediaBrowserAdapter: MediaBrowserAdapter?) {
        this.mediaBrowserAdapter = mediaBrowserAdapter
    }

    @Inject
    fun setAlbumArtPainter(albumArtPainter: AlbumArtPainter?) {
        this.albumArtPainter = albumArtPainter
    }

    @Inject
    fun setMediaControllerAdapter(mediaControllerAdapter: MediaControllerAdapter?) {
        this.mediaControllerAdapter = mediaControllerAdapter
    }

    @Inject
    fun setRecyclerViewAdapters(adapters: RecyclerViewAdapters?) {
        recyclerViewAdapters = adapters
    }

    @Inject
    fun setMyGenericItemTouchListener(listener: MyGenericItemTouchListener?) {
        myGenericItemTouchListener = listener
    }

    @Inject
    fun setIntentMapper(intentMapper: IntentMapper?) {
        this.intentMapper = intentMapper
    }

    private fun injectDependencies(component: MediaActivityCompatComponent) {
        component.childViewPagerFragmentSubcomponentFactory()
                .create(parentItemType, parentItemTypeId, this).inject(this)
    }

    companion object {
        private const val LOG_TAG = "GENRC_VIW_PGE_FRGMNT"
    }
}