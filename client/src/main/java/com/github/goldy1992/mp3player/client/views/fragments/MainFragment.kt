package com.github.goldy1992.mp3player.client.views.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.observe
import com.github.goldy1992.mp3player.client.MediaBrowserConnectionListener
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.callbacks.Listener
import com.github.goldy1992.mp3player.client.databinding.FragmentMainBinding
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
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : MediaFragment(), LogTagger {

    private var tabLayoutMediator: TabLayoutMediator? = null

    lateinit var adapter: MyPagerAdapter

    var searchFragment: SearchFragment? = null

    val viewModel : MainFragmentViewModel by viewModels()

    lateinit var binding : FragmentMainBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.binding = FragmentMainBinding.inflate(inflater)
        //    searchFragment = SearchFragment()
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
           val activity : AppCompatActivity = requireActivity() as AppCompatActivity
        activity.setSupportActionBar(binding.titleToolbar)
        val supportActionBar = activity.supportActionBar
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar.setHomeAsUpIndicator(R.drawable.ic_menu)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
            adapter = MyPagerAdapter(this)

        mediaBrowserAdapter.subscribeToRoot().observe(this.viewLifecycleOwner) {
            for (mediaItem in it) {
                val id = MediaItemUtils.getMediaId(mediaItem)!!
                Log.i(logTag(), "media id: $id")
                val category = MediaItemUtils.getExtra(Constants.ROOT_ITEM_TYPE, mediaItem) as MediaItemType

                if (!adapter.pagerItems.containsKey(category)) {
                    val mediaItemListFragment: MediaItemListFragment? = when (category) {
                        // TODO: change the use of subfragments to simply use a view pager
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

    }
        binding.rootMenuItemsPager.adapter = adapter
        tabLayoutMediator = TabLayoutMediator(binding.tabLayout, binding.rootMenuItemsPager!!, adapter)
        tabLayoutMediator!!.attach()
        binding.rootMenuItemsPager.adapter = adapter
        viewModel.menuCategories = mediaBrowserAdapter.subscribeToRoot() as MutableLiveData<List<MediaBrowserCompat.MediaItem>>
        }


    override fun onResume() {
        super.onResume()
//        if (null != searchFragment && searchFragment!!.isAdded && searchFragment!!.isVisible) {
//            fr
//                    .beginTransaction()
//                    .remove(searchFragment!!)
//                    .commit()
//        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun logTag(): String {
        return "Main_fragment"
    }
}