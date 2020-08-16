package com.github.goldy1992.mp3player.client.views.fragments.viewpager

import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.ListPreloader
import com.github.goldy1992.mp3player.client.AlbumArtPainter
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.databinding.FragmentViewPageBinding
import com.github.goldy1992.mp3player.client.listeners.MyGenericItemTouchListener
import com.github.goldy1992.mp3player.client.listeners.MyGenericItemTouchListener.ItemSelectedListener
import com.github.goldy1992.mp3player.client.viewmodels.MediaListViewModel
import com.github.goldy1992.mp3player.client.views.adapters.MyGenericRecyclerViewAdapter
import com.github.goldy1992.mp3player.commons.LogTagger
import com.github.goldy1992.mp3player.commons.MediaItemType
import kotlinx.android.synthetic.main.fragment_view_page.*
import javax.inject.Inject

/**
 * Fragment to show a list of media items, has MediaBrowserAdapter injected into it using Dagger2
 * NOTE: MediaBrowserAdapter is not annotated with inject because:
 *
 * 1) we don't want to override the constructor for compatibility purposes
 * 2) This ChildViewFragment will be provided after the main injection is done
 */
abstract class MediaItemListFragment : Fragment(), ItemSelectedListener, LogTagger {

    companion object {
        const val MEDIA_ITEM_TYPE = "mediaItemType"
        const val PARENT_ID = "parentId"

        fun createArguments(mediaItemType: MediaItemType, id: String) : Bundle {
            val toReturn = Bundle()
            toReturn.putSerializable(MEDIA_ITEM_TYPE, mediaItemType)
            toReturn.putString(PARENT_ID, id)
            return toReturn
        }
    }

    @Inject
    lateinit var mediaBrowserAdapter : MediaBrowserAdapter

    @Inject
    lateinit var mediaControllerAdapter: MediaControllerAdapter

    fun getParentId() : String? {
        return arguments?.getString(PARENT_ID)
    }

    lateinit var binding: FragmentViewPageBinding

    protected abstract fun getViewAdapter() : MyGenericRecyclerViewAdapter

    @Inject
    lateinit var albumArtPainter: AlbumArtPainter

    lateinit var myGenericItemTouchListener : MyGenericItemTouchListener

    private fun subscribeUi(adapter: MyGenericRecyclerViewAdapter, binding: FragmentViewPageBinding) {
        viewModel().items.observe(viewLifecycleOwner) { result ->
            adapter.submitList(result)
        }
    }

    abstract fun viewModel() : MediaListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        viewModel().items = mediaBrowserAdapter.subscribe(getParentId()!!) as MutableLiveData<List<MediaBrowserCompat.MediaItem>>

        myGenericItemTouchListener = MyGenericItemTouchListener(requireContext(), this)
        binding = FragmentViewPageBinding.inflate(inflater, container, false)
        binding.recyclerView.adapter = getViewAdapter()
        binding.recyclerView.addOnItemTouchListener(myGenericItemTouchListener)
        myGenericItemTouchListener.parentView = binding.recyclerView
        binding.recyclerView.itemAnimator = DefaultItemAnimator()
        binding.recyclerView.isAttachedToWindow
        if (binding.recyclerView.layoutManager == null) {
            binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        }
        subscribeUi(getViewAdapter(), binding)
        return binding.root
    }

    override fun onViewCreated(view: View, bundle: Bundle?) {
        val preLoader = albumArtPainter
                .createPreloader(getViewAdapter()
                        as ListPreloader.PreloadModelProvider<MediaBrowserCompat.MediaItem>)
        recyclerView.addOnScrollListener(preLoader)
        recyclerView.setHideScrollbar(true)
    }
}