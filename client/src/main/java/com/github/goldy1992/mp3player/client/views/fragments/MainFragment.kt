package com.github.goldy1992.mp3player.client.views.fragments

import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.R
import com.github.goldy1992.mp3player.databinding.FragmentMainBinding
import com.github.goldy1992.mp3player.client.viewmodels.MainFragmentViewModel
import com.github.goldy1992.mp3player.client.views.adapters.MyPagerAdapter
import com.github.goldy1992.mp3player.client.views.fragments.viewpager.FolderListFragment
import com.github.goldy1992.mp3player.client.views.fragments.viewpager.MediaItemListFragment
import com.github.goldy1992.mp3player.client.views.fragments.viewpager.SongListFragment
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.LogTagger
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint(DestinationFragment::class)
class MainFragment : Hilt_MainFragment(), LogTagger {

    @Inject
    lateinit var mediaBrowserAdapter : MediaBrowserAdapter

    @Inject
    lateinit var mediaControllerAdapter: MediaControllerAdapter

    lateinit var navigationView : NavigationView

    private var tabLayoutMediator: TabLayoutMediator? = null

    lateinit var adapter: MyPagerAdapter

    private val viewModel : MainFragmentViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        setHasOptionsMenu(true)
        adapter = MyPagerAdapter(this)
        val binding = FragmentMainBinding.inflate(inflater)
        binding.appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { app: AppBarLayout?, offset: Int ->
            Log.i(logTag(), "offset: " + offset + ", scroll range: " + app?.totalScrollRange)
            var newOffset = offset
            newOffset += app!!.totalScrollRange
            binding.rootMenuItemsPager.setPadding(
                    binding.rootMenuItemsPager.paddingLeft,
                    binding.rootMenuItemsPager.paddingTop,
                    binding.rootMenuItemsPager.paddingRight,
                    newOffset)
        })
        this.toolbar = binding.titleToolbar
        binding.rootMenuItemsPager.adapter = adapter
        tabLayoutMediator = TabLayoutMediator(binding.tabLayout, binding.rootMenuItemsPager, adapter)
        tabLayoutMediator!!.attach()
        binding.rootMenuItemsPager.adapter = adapter

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.app_bar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mediaBrowserAdapter.subscribeToRoot().observe(this.viewLifecycleOwner) {
            for (mediaItem in it) {
                val id = MediaItemUtils.getMediaId(mediaItem)!!
                Log.i(logTag(), "media id: $id")
                val category = MediaItemUtils.getExtra(Constants.ROOT_ITEM_TYPE, mediaItem) as MediaItemType

                if (!adapter.pagerItems.containsKey(category)) {
                    val mediaItemListFragment: MediaItemListFragment? = when (category) {
                        MediaItemType.SONGS -> SongListFragment.newInstance(category, id)
                        MediaItemType.FOLDERS -> FolderListFragment.newInstance(category, id)
                        else -> null
                    }
                    if (null != mediaItemListFragment) {
                        adapter.pagerItems[category] = mediaItemListFragment
                        adapter.menuCategories[category] = mediaItem
                        adapter.notifyDataSetChanged()
                    }
                }
            }

        } // observe
             viewModel.menuCategories = mediaBrowserAdapter.subscribeToRoot() as MutableLiveData<List<MediaBrowserCompat.MediaItem>>
    }

    override fun lockDrawerLayout(): Boolean {
        return false
    }

    override fun logTag(): String {
        return "Main_fragment"
    }
}