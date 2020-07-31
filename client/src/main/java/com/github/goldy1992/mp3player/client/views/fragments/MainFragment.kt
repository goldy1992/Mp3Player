package com.github.goldy1992.mp3player.client.views.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.databinding.FragmentMainBinding
import com.github.goldy1992.mp3player.client.listeners.MyDrawerListener
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
import javax.inject.Inject

class MainFragment : Fragment(), LogTagger {

    private var tabLayoutMediator: TabLayoutMediator? = null

    lateinit var adapter: MyPagerAdapter

    var searchFragment: SearchFragment? = null

    @Inject
    lateinit var myDrawerListener: MyDrawerListener

    val viewModel : MainFragmentViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, true)
//        val binding : FragmentMainBinding = FragmentMainBinding.inflate(inflater)
//        //    searchFragment = SearchFragment()
//        binding.appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { app: AppBarLayout?, offset: Int ->
//            Log.i(logTag(), "offset: " + offset + ", scroll range: " + app?.totalScrollRange)
//            var newOffset = offset
//            newOffset += app!!.totalScrollRange
//            binding.rootMenuItemsPager.setPadding(
//                    binding.rootMenuItemsPager.paddingLeft,
//                    binding.rootMenuItemsPager.paddingTop,
//                    binding.rootMenuItemsPager.paddingRight,
//                    newOffset)
//        })
//        adapter = MyPagerAdapter(parentFragmentManager, lifecycle)
//
//
//        viewModel.menuCategories.observe(this.viewLifecycleOwner) {
//            for (mediaItem in it) {
//                val id = MediaItemUtils.getMediaId(mediaItem)!!
//                Log.i(logTag(), "media id: $id")
//                val category = MediaItemUtils.getExtra(Constants.ROOT_ITEM_TYPE, mediaItem) as MediaItemType
//                var mediaItemListFragment: MediaItemListFragment?
//                mediaItemListFragment = when (category) {
//                    MediaItemType.SONGS -> SongListFragment.newInstance(category, id)
//                    MediaItemType.FOLDERS -> FolderListFragment.newInstance(category, id)
//                    else -> null
//                }
//                if (null != mediaItemListFragment) {
//                    adapter.pagerItems[category] = mediaItemListFragment
//                    adapter.menuCategories[category] = mediaItem
//                    adapter.notifyDataSetChanged()
//                }
//            }
//
//        }
//        binding.rootMenuItemsPager.adapter = adapter
//        tabLayoutMediator = TabLayoutMediator(binding.tabLayout, binding.rootMenuItemsPager!!, adapter)
//        tabLayoutMediator!!.attach()
//        binding.rootMenuItemsPager.adapter = adapter
//        //setSupportActionBar(titleToolbar)
//        //supportActionBar!!.setDisplayHomeAsUpEnabled(true)
//      //  supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_menu)
//
//        initNavigationView()
//        binding.drawerLayout.addDrawerListener(myDrawerListener)
//
//        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       val binding : MainFragment = view as MainFragment


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

    private fun initNavigationView() {
//        navigationView!!.setNavigationItemSelectedListener { menuItem: MenuItem -> onNavigationItemSelected(menuItem) }
//        val spinner = navigationView!!.menu.findItem(R.id.themes_menu_item).actionView as Spinner
        //      ThemeSpinnerController(applicationContext, spinner, this, componentClassMapper)
    }

    override fun logTag(): String {
        return "Main_fragment"
    }
}