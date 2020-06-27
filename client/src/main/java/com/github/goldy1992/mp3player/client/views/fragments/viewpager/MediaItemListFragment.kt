package com.github.goldy1992.mp3player.client.views.fragments.viewpager

import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.ListPreloader
import com.github.goldy1992.mp3player.client.AlbumArtPainter
import com.github.goldy1992.mp3player.client.IntentMapper
import com.github.goldy1992.mp3player.client.activities.MediaActivityCompat
import com.github.goldy1992.mp3player.client.callbacks.Listener
import com.github.goldy1992.mp3player.client.dagger.subcomponents.MediaItemListFragmentSubcomponent
import com.github.goldy1992.mp3player.client.databinding.FragmentViewPageBinding
import com.github.goldy1992.mp3player.client.listeners.MyGenericItemTouchListener
import com.github.goldy1992.mp3player.client.listeners.MyGenericItemTouchListener.ItemSelectedListener
import com.github.goldy1992.mp3player.client.viewmodels.MediaListViewModel
import com.github.goldy1992.mp3player.client.views.adapters.MyGenericRecyclerViewAdapter
import com.github.goldy1992.mp3player.client.views.fragments.MediaFragment
import com.github.goldy1992.mp3player.commons.MediaItemType
import kotlinx.android.synthetic.main.fragment_view_page.*
import java.util.*
import javax.inject.Inject

/**
 * Fragment to show a list of media items, has MediaBrowserAdapter injected into it using Dagger2
 * NOTE: MediaBrowserAdapter is not annotated with inject because:
 *
 * 1) we don't want to override the constructor for compatibility purposes
 * 2) This ChildViewFragment will be provided after the main injection is done
 */
abstract class MediaItemListFragment : MediaFragment(), ItemSelectedListener {
    /**
     * The parent for all the media items in this view; if null, the fragment represent a list of all available songs.
     */
    private val linearLayoutManager = LinearLayoutManager(context)

    lateinit var binding: FragmentViewPageBinding

    lateinit var parentItemType: MediaItemType
    lateinit var parentItemTypeId: String

    protected abstract fun getViewAdapter() : MyGenericRecyclerViewAdapter

    @Inject
    lateinit var viewModel: MediaListViewModel

    @Inject
    lateinit var intentMapper: IntentMapper

    @Inject
    lateinit var albumArtPainter: AlbumArtPainter
    @Inject
    lateinit var myGenericItemTouchListener: MyGenericItemTouchListener

    protected fun init(mediaItemType: MediaItemType, id: String) {
        parentItemType = mediaItemType
        parentItemTypeId = id
    }

    fun subscribeUi(adapter: MyGenericRecyclerViewAdapter, binding: FragmentViewPageBinding) {
        viewModel.items.observe(viewLifecycleOwner) { result ->
            adapter.submitList(result)
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentViewPageBinding.inflate(inflater, container, false)
        binding.recyclerView.adapter = getViewAdapter()
        binding.recyclerView.addOnItemTouchListener(myGenericItemTouchListener)
        myGenericItemTouchListener.parentView = binding.recyclerView
        binding.recyclerView.itemAnimator = DefaultItemAnimator()
        binding.recyclerView.layoutManager = linearLayoutManager
        return binding.root
    }


    override fun onViewCreated(view: View, bundle: Bundle?) {
        val preLoader = albumArtPainter
                .createPreloader(getViewAdapter()
                        as ListPreloader.PreloadModelProvider<MediaBrowserCompat.MediaItem>)
        recyclerView.addOnScrollListener(preLoader)
        recyclerView.setHideScrollbar(true)
    }

    protected fun createMediaItemListFragmentSubcomponent(listener: ItemSelectedListener, parentItemTypeId : String)
            : MediaItemListFragmentSubcomponent? {
        return  (activity as MediaActivityCompat?)
                ?.mediaActivityCompatComponent
                ?.mediaItemListFragmentSubcomponent()
                ?.create(listener, parentItemTypeId)
    }

    override fun mediaControllerListeners(): Set<Listener> {
        return Collections.emptySet()
    }
}